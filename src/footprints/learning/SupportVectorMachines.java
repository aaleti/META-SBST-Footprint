package footprints.learning;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import footprints.IO.CEFileUtils;

public class SupportVectorMachines{
	
	public double svmEasyMode(double [][] inputTrain, double[] outputTrain, double [][] inputTest, double[] outputTest){
		
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < inputTrain.length; i++){
                    sb.append(outputTrain[i]).append(" ");
                    for(int p=0; p < inputTrain[0].length; p++){
                            sb.append((p+1) + ":" + inputTrain[i][p]+" ");
                    }		
                    sb.append("\n");
		}
		
		CEFileUtils.writeFile(sb.toString(), System.getProperty("user.dir") + "/libsvm-3.20/", "train");
		
		sb = new StringBuilder();
		for(int i=0; i < inputTest.length; i++){
			sb.append(outputTest[i]+ " ");
			for(int p=0; p < inputTest[0].length; p++){
				sb.append((p+1) + ":" + inputTest[i][p]+" ");
			}
			
			sb.append("\n");
		}
		
		CEFileUtils.writeFile(sb.toString(), System.getProperty("user.dir") + "/libsvm-3.20/", "test");
		
		String testCommand = "python " + System.getProperty("user.dir") + "/libsvm-3.20/tools/easy.py " + System.getProperty("user.dir") + "/libsvm-3.20/train " + System.getProperty("user.dir") + "/libsvm-3.20/test";
		
		ProcessBuilder builder = new ProcessBuilder(testCommand.split("\\s"));
		
		builder.redirectOutput(new File(System.getProperty("user.dir") + "/libsvm-3.20/test.result"));	
		
		Process p = null;
		try{
			p = builder.start();
			
			p.waitFor();
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		BufferedReader file = CEFileUtils.openFile(System.getProperty("user.dir") + "/libsvm-3.20/test.result");
		String line = CEFileUtils.readLine(file);
		String [] results = line.split("\\s");
		Double accuracy = Double.valueOf(results[2].split("%")[0]);
		
		return accuracy;
		
	}
	
	public double svmGridMode(double [][] inputTrain, double[] outputTrain){
		
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < inputTrain.length; i++){
			sb.append(outputTrain[i]).append(" ");
			for(int p=0; p < inputTrain[0].length; p++){
				sb.append((p+1) + ":" + inputTrain[i][p]+" ");
			}
			
			sb.append("\n");
		}
		
		CEFileUtils.writeFile(sb.toString(), System.getProperty("user.dir") + "/libsvm-3.20/", "train");
		
		String testCommand = "python " + System.getProperty("user.dir") + "/libsvm-3.20/tools/grid.py -v 10 " + System.getProperty("user.dir") + "/libsvm-3.20/train";
		
		
		ProcessBuilder builder = new ProcessBuilder(testCommand.split("\\s"));
		
		builder.redirectOutput(new File(System.getProperty("user.dir") + "/libsvm-3.20/test.result"));	
		
		Process p = null;
		try{
			p = builder.start();
			
			p.waitFor();
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		BufferedReader file = CEFileUtils.openFile(System.getProperty("user.dir") + "/libsvm-3.20/test.result");
		String line = CEFileUtils.readLine(file);
		while(line != null){
			if(line.contains("local") == false){
				break;
			}
			line = CEFileUtils.readLine(file);
		}
		String [] results = line.split("\\s");
		Double accuracy = Double.valueOf(results[2]);
		
		return accuracy;
		
	}
	
	public Double svmTrain(double [][] input, double[] output, boolean svm10FoldCrossValidation){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < output.length; i++){
			sb.append(output[i] + " ");
			for(int p=0; p < input[0].length; p++){
				sb.append((p+1) + ":" + input[i][p]+" ");
			}
			
			sb.append("\n");
		}
		
		CEFileUtils.writeFile(sb.toString(), System.getProperty("user.dir") + "/libsvm-3.20/", "train");
		
		String scaleCommand = System.getProperty("user.dir") + "/libsvm-3.20/svm-scale -l -1 -u 1 -s " + System.getProperty("user.dir") + "/libsvm-3.20/range " + System.getProperty("user.dir") + "/libsvm-3.20/train";
		
		ProcessBuilder builder = new ProcessBuilder(scaleCommand.split("\\s"));
		
		builder.redirectOutput(new File(System.getProperty("user.dir") + "/libsvm-3.20/train.scale"));	
		builder.redirectError();
		Process p = null;
		try{
			p = builder.start();
			
			p.waitFor();
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		String trainCommand = System.getProperty("user.dir") + "/libsvm-3.20/svm-train " + (svm10FoldCrossValidation ? "-v 10 " : "") +  System.getProperty("user.dir") + "/libsvm-3.20/train.scale " + System.getProperty("user.dir") + "/libsvm-3.20/train.model";
		
		builder = new ProcessBuilder(trainCommand.split("\\s"));
		
		builder.redirectOutput(new File(System.getProperty("user.dir") + "/libsvm-3.20/train.result"));	
		
		p = null;
		try{
			p = builder.start();
			
			p.waitFor();
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(0);
		}	
		Double accuracy = null;
		if(svm10FoldCrossValidation){
		
			BufferedReader file = CEFileUtils.openFile(System.getProperty("user.dir") + "/libsvm-3.20/train.result");
			String line = CEFileUtils.readLine(file);
			while(line != null){
				if(line.contains("Accuracy")){
					break;
				}
				line = CEFileUtils.readLine(file);
			}
			
			String [] results = line.split("\\s");
			accuracy = Double.valueOf(results[results.length - 1].split("%")[0]);
		}
		return accuracy;
		
	}
	
	public double svmTest(double [][] input, double[] output){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < output.length; i++){
			sb.append(output[i] + " ");
			for(int p=0; p < input[0].length; p++){
				sb.append((p+1) + ":" + input[i][p]+" ");
			}
			
			sb.append("\n");
		}
		
		CEFileUtils.writeFile(sb.toString(), System.getProperty("user.dir") + "/libsvm-3.20/", "test");
		
		String scaleCommand = System.getProperty("user.dir") + "/libsvm-3.20/svm-scale -r " + System.getProperty("user.dir") + "/libsvm-3.20/range " + System.getProperty("user.dir") + "/libsvm-3.20/test";
		
		ProcessBuilder builder = new ProcessBuilder(scaleCommand.split("\\s"));
		
		builder.redirectOutput(new File(System.getProperty("user.dir") + "/libsvm-3.20/test.scale"));	
		Process p = null;
		try{
			p = builder.start();
			
			p.waitFor();
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		String testCommand = System.getProperty("user.dir") + "/libsvm-3.20/svm-predict " + System.getProperty("user.dir") + "/libsvm-3.20/test.scale " + System.getProperty("user.dir") + "/libsvm-3.20/train.model " + System.getProperty("user.dir") + "/libsvm-3.20/test.predict";
		
		builder = new ProcessBuilder(testCommand.split("\\s"));
		
		builder.redirectOutput(new File(System.getProperty("user.dir") + "/libsvm-3.20/test.result"));	
		
		p = null;
		try{
			p = builder.start();
			
			p.waitFor();
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		BufferedReader file = CEFileUtils.openFile(System.getProperty("user.dir") + "/libsvm-3.20/test.result");
		String line = CEFileUtils.readLine(file);
                double accuracy=0;
                if(line==null)
                    return 0;
                try{
                    String [] results = line.split("\\s");
                    accuracy = Double.valueOf(results[2].split("%")[0]);
                }
                catch(NumberFormatException ex){
                    return 0;
                }
		return accuracy;
	}
	
	public Object[] createHashSVM(double [][] pcaReducedArray, int [] groupArray){
            int sizeTen=0;

		sizeTen = pcaReducedArray[0].length / 10;
         
		double [][] trainMatrix = new double[sizeTen * 9][pcaReducedArray.length];
		double [] trainPerformance = new double[sizeTen * 9];
		
		double [][] testMatrix = new double[sizeTen][pcaReducedArray.length];
		double [] testPerformance = new double[sizeTen];
		
		ArrayList<Integer> numbersList = new ArrayList<>();
		for(int i=0; i < pcaReducedArray[0].length; i++){
			numbersList.add(i);
		}
		
		
		for(int i=0; i < trainMatrix.length; i++){
			
			Integer randomNumber = new Random().nextInt(numbersList.size());
			
			Integer selectedNumber = numbersList.remove(randomNumber.intValue());
			
			for(int p=0; p < pcaReducedArray.length; p++){
				trainMatrix[i][p] = pcaReducedArray[p][selectedNumber];
			}
			
			trainPerformance[i] = groupArray[selectedNumber];
			
		}
		
		for(int i =0; i < testMatrix.length; i++){
			
			Integer randomNumber = new Random().nextInt(numbersList.size());
			
			Integer selectedNumber = numbersList.remove(randomNumber.intValue());
			
			for(int p=0; p < pcaReducedArray.length; p++){
				testMatrix[i][p] = pcaReducedArray[p][selectedNumber];
			}	
			
			testPerformance[i] = groupArray[selectedNumber];
			
		}
		
		Object [] obj = new Object[]{trainMatrix, trainPerformance, testMatrix, testPerformance};
		
		return obj;
	}
	
	public synchronized Object[] createHashSVMEqual(double[][] redComps, double [] groupArray){
		
		int sizeTen = redComps[0].length / 10;
		
		int sizeTrain = sizeTen * 9;
		int sizeTest = sizeTen;
		
		double [][] trainMatrix = new double[sizeTrain][redComps.length];
		double [] trainPerformance = new double[sizeTrain];
		
		double [][] testMatrix = new double[sizeTest][redComps.length];
		double [] testPerformance = new double[sizeTest];
		
		int limit = trainMatrix.length / 2;
		
		ArrayList<Integer> numbersListA = new ArrayList<Integer>();
		for(int i=0; i < redComps[0].length / 2; i++){
			numbersListA.add(i);
		}
		
		ArrayList<Integer> numbersListB = new ArrayList<Integer>();
		for(int i=redComps[0].length / 2; i < redComps[0].length; i++){
			numbersListB.add(i);
		}
		
		int count = 0;
		
		for(; count < limit; count++){
			
			Integer randomNumber = new Random().nextInt(numbersListA.size());
			
			Integer selectedNumber = numbersListA.remove(randomNumber.intValue());
			
			for(int p=0; p < redComps.length; p++){
				trainMatrix[count][p] = redComps[p][selectedNumber];
			}
			
			trainPerformance[count] = groupArray[selectedNumber];
		}
		
		for(; count < trainMatrix.length; count++){
			
			Integer randomNumber = new Random().nextInt(numbersListB.size());
			
			Integer selectedNumber = numbersListB.remove(randomNumber.intValue());
			
			for(int p=0; p < redComps.length; p++){
				trainMatrix[count][p] = redComps[p][selectedNumber];
			}
			
			trainPerformance[count] = groupArray[selectedNumber];
		}
		
		count = 0;
		
		for(;numbersListA.isEmpty() == false; count++){
			
			Integer randomNumber = new Random().nextInt(numbersListA.size());
			
			Integer selectedNumber = numbersListA.remove(randomNumber.intValue());
			
			for(int p=0; p < redComps.length; p++){
				testMatrix[count][p] = redComps[p][selectedNumber];
			}	
			
			testPerformance[count] = groupArray[selectedNumber];
		}
		
		for(;numbersListB.isEmpty() == false; count++){
			
			Integer randomNumber = new Random().nextInt(numbersListB.size());
			
			Integer selectedNumber = numbersListB.remove(randomNumber.intValue());
			
			for(int p=0; p < redComps.length; p++){
				testMatrix[count][p] = redComps[p][selectedNumber];
			}	
			
			testPerformance[count] = groupArray[selectedNumber];
		}
		
		Object [] obj = new Object[]{trainMatrix, trainPerformance, testMatrix, testPerformance};
		
		return obj;
	}
	
	public synchronized Object[] createHashTrainSVM(double[][] redComps, int [] groupArray){
		
		int size = redComps[0].length;
		
		double [][] trainMatrix = new double[size][redComps.length];
		double [] trainPerformance = new double[size];
		
		for(int i=0; i < trainMatrix.length; i++){
			
			for(int p=0; p < redComps.length; p++){
				trainMatrix[i][p] = redComps[p][i];
			}
			
			trainPerformance[i] = groupArray[i];
			
		}
		
		Object [] obj = new Object[]{trainMatrix, trainPerformance};
		
		return obj;
	}
	
	public static synchronized Object[] createHashTrainSVMShort(double [][] dataArray, int [] groupArray){
		
		int size = dataArray[0].length;
		
		double [][] trainMatrix = new double[size][dataArray.length];
		double [] trainPerformance = new double[size];
		
		for(int i=0; i < trainMatrix.length; i++){
			
			for(int p=0; p < dataArray.length; p++){
				trainMatrix[i][p] = dataArray[p][i];
			}
			
			trainPerformance[i] = groupArray[i];
			
		}
		
		Object [] obj = new Object[]{trainMatrix, trainPerformance};
		
		return obj;
	}
	
	private static String createJobFile(String id){
		
		String path = System.getProperty("user.dir") + "/resources/";
		
		BufferedReader file = CEFileUtils.openFile(path + "job_svm_template.sh");
		
		String line = CEFileUtils.readLine(file);
		
		StringBuilder jobStr = new StringBuilder();
		
		while(line != null){
			if(line.contains("@1")){
				line = line.replace("@1",id);
			}
			
			jobStr.append(line);
			jobStr.append("\n");
			
			line = CEFileUtils.readLine(file);
		}
		
		return jobStr.toString();
	}
	
}