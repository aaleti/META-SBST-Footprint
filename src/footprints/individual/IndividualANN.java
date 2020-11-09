package footprints.individual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import footprints.learning.NNThread;
import java.util.List;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.nnet.Kohonen;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.UnsupervisedHebbianNetwork;
import org.neuroph.nnet.learning.KohonenLearning;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.TransferFunctionType;
import org.uncommons.maths.binary.BitString;

public class IndividualANN extends Individual{
	
	protected NeuralNetwork neuralNetwork;
	private NeuralNetworkType type=NeuralNetworkType.MULTI_LAYER_PERCEPTRON;
	protected DataSet trainingSet;
	protected DataSet testSet;
	protected Integer inputNeurons=2;
	protected Integer hiddenNeurons=4;
	
	protected Integer outputNeurons=2;
	
	protected HashMap<Integer, Integer> hashArrayOutput;
	
    /**
     *
     */
    public IndividualANN (){
		super();
		initialise();
		
		
	}
    
     public IndividualANN (BitString gene, List<DataItem> allItems){
	super();
		initialise();
                 
        genotype=gene;
        selectedFeaturesItems = new ArrayList<>();
        for(int i=0, x = allItems.size() - 1; i < allItems.size(); i++, x--){
            DataItem item = allItems.get(i);
                if(gene.getBit(x)){
                    selectedFeaturesItems.add(item);
                }
            }
        convertData();
		
		
	}
    
    public void initialise(){
        switch (type) {
		
		case UNSUPERVISED_HEBBIAN_NET:
			
                    neuralNetwork = new UnsupervisedHebbianNetwork(inputNeurons, outputNeurons);

                    break;
		case MULTI_LAYER_PERCEPTRON:
			
			neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,inputNeurons, hiddenNeurons, hiddenNeurons, outputNeurons);
			
			MomentumBackpropagation momentumBackpropagation = new MomentumBackpropagation();
			momentumBackpropagation.setMaxError(0.01);
			momentumBackpropagation.setLearningRate(0.2);
			momentumBackpropagation.setMomentum(0.7);
			momentumBackpropagation.setMaxIterations(100);
			
			neuralNetwork.setLearningRule(momentumBackpropagation);
			
			break;
			
		case KOHONEN:
			
			neuralNetwork = new Kohonen(inputNeurons, outputNeurons);
			
			KohonenLearning kohonenLearning = new KohonenLearning();
			kohonenLearning.setLearningRate(.6);
			
			neuralNetwork.setLearningRule(kohonenLearning);
			
			break;	

		default:
			break;
		}
    }
	
	public void createTrainingDataSet(double [][] arrayComponents, double [][] arrayOutput){
		
		if(arrayComponents[0].length != this.inputNeurons){
			System.out.println("Number of Input Neurons is different from the Training Array");
			System.exit(0);
		}
		
		this.trainingSet = new DataSet(this.inputNeurons, this.outputNeurons); 
		for(int i=0; i < arrayComponents.length; i++){
			double [] arrayRowInput = new double[this.inputNeurons];
			for(int l=0; l < arrayRowInput.length; l++){
				arrayRowInput[l] = arrayComponents[i][l];
			}
			double [] arrayRowOutput = new double[this.outputNeurons];
			for(int l=0; l < arrayRowOutput.length; l++){
				arrayRowOutput[l] = arrayOutput[i][l];
			}
				
			
			this.trainingSet.addRow(new DataSetRow (arrayRowInput, arrayRowOutput)); 
		}
		
	}
	
	public void createTestDataSet(double [][] arrayComponents, double [][] arrayOutput){
		
		if(arrayComponents[0].length != this.inputNeurons){
			System.out.println("Number of Input Neurons is different from the Training Array");
			System.exit(0);
		}
		
		this.testSet = new DataSet(this.inputNeurons, this.outputNeurons); 
		for(int i=0; i < arrayComponents.length; i++){
			double [] arrayRowInput = new double[this.inputNeurons];
			for(int l=0; l < arrayRowInput.length; l++){
				arrayRowInput[l] = arrayComponents[i][l];
			}
			double [] arrayRowOutput = new double[this.outputNeurons];
			for(int l=0; l < arrayRowOutput.length; l++){
				arrayRowOutput[l] = arrayOutput[i][l];
			}
				
			
			this.testSet.addRow(new DataSetRow (arrayRowInput, arrayRowOutput)); 
		}
		
	}
	
	public void trainNetwork(){
		
		NNThread learnigService = new NNThread(this.neuralNetwork, this.trainingSet);

		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.execute(learnigService);
		try{
			executor.awaitTermination(30, TimeUnit.SECONDS);
			learnigService.stopLearning();
			//System.out.println("stopped");
			
		}catch(InterruptedException e){
			System.out.println("Error on Stop training");
		}

	}
	
	public double testNetwork(){
		
		double error = 0;
		
		int stopTraining = this.testSet.size();
		
		try{
		
			for(int i=0; i < stopTraining; i++){
				
				DataSetRow dataRow = testSet.getRowAt(i);
				
				neuralNetwork.setInput(dataRow.getInput());
				neuralNetwork.calculate();
				
				double[] networkOutput = neuralNetwork.getOutput();
				
				for(int p=0; p < dataRow.getDesiredOutput().length; p++){
					error += Math.pow((dataRow.getDesiredOutput()[p] - networkOutput[p]),2);
				}
			}
			
		}catch(VectorSizeMismatchException e){
			System.out.println(e.getMessage());
		}
		
		return 1/error;
	}
	
	
	public synchronized Object[] createHashANN(int [] groupArray){
		
		int sizeTen = getRedComponentsArray()[0].length / 10;
		
		double [][] trainMatrix = new double[sizeTen * 9][getRedComponentsArray().length];
		double [][] trainPerformance = new double[sizeTen * 9][getRedComponentsArray().length];
		
		double [][] testMatrix = new double[sizeTen][getRedComponentsArray().length];
		double [][] testPerformance = new double[sizeTen][getRedComponentsArray().length];
		
		ArrayList<Integer> numbersList = new ArrayList<Integer>();
		for(int i=0; i < getRedComponentsArray()[0].length; i++){
			numbersList.add(i);
		}
		
		
		for(int i=0; i < trainMatrix.length; i++){
			
			Integer randomNumber = new Random().nextInt(numbersList.size());
			
			Integer selectedNumber = numbersList.remove(randomNumber.intValue());
			
			for(int p=0; p <getRedComponentsArray().length; p++){
				trainMatrix[i][p] = getRedComponentsArray()[p][selectedNumber];
			}
			
			trainPerformance[i][0] = groupArray[selectedNumber];
			trainPerformance[i][1] = groupArray[selectedNumber] == 0 ? 1 : 0;
		}
		
		for(int i =0; i < testMatrix.length; i++){
			
			int randomNumber = new Random().nextInt(numbersList.size());
			
			int selectedNumber = numbersList.remove(randomNumber);
			
			for(int p=0; p < getRedComponentsArray().length; p++){
				testMatrix[i][p] = getRedComponentsArray()[p][selectedNumber];
			}	
			
			testPerformance[i][0] = groupArray[selectedNumber];
			testPerformance[i][1] = groupArray[selectedNumber] == 0 ? 1 : 0;
			
		}
		
		Object [] obj = new Object[]{trainMatrix, trainPerformance, testMatrix, testPerformance};
		
		return obj;
	}
	
	
}
