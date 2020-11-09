/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package footprints.fitnessEvaluator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import footprints.IO.CEFileUtils;
import footprints.individual.problemgenerator.ConditionTree;
import footprints.individual.problemgenerator.IndividualGP;
import footprints.individual.problemgenerator.UnitTestGenerator;
import marytts.util.math.MathUtils;
import org.uncommons.maths.binary.BitString;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

/**
 *
 * @author aldeidaaleti
 */
public class FitnessEvaluatorGP implements FitnessEvaluator<BitString>{

    private String method1;
    private String method2;
    @Override
    public double getFitness(BitString t, List<? extends BitString> list) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public double evaluate(IndividualGP candidate){
		
	       
		
		StringBuilder methodSignature = new StringBuilder();
		
		StringBuilder methodContent = new StringBuilder();
		
		for(int i=0; i < candidate.getConditionTree().length; i++){
			ConditionTree conditionTree = candidate.getConditionTree()[i];
			
			methodContent.append(conditionTree.generate());
		}
		
		String generatedClass = candidate.getHollowClass().replace("#1", methodContent.toString());
		
		CEFileUtils.writeFile(generatedClass, System.getProperty("user.dir") + "/resources/temp/com/cadu/", "GeneratedClass.java");
		
		String compileCommand = "javac " + System.getProperty("user.dir") + "/resources/temp/com/cadu/GeneratedClass.java";  
		
		ProcessBuilder javaCompilerBuilder = new ProcessBuilder(compileCommand.split("\\s"));
		
		javaCompilerBuilder.redirectOutput(new File(System.getProperty("user.dir") + "/resources/temp/compilationOutput.txt"));
		
		int compilationReturn = 0;
		
		Process p = null;
		try{
			p = javaCompilerBuilder.start();
			
			compilationReturn = p.waitFor();
			
		}catch(Exception e){
			System.exit(1);
		}
		
		if(compilationReturn != 0){
			System.exit(1);
		}
		
		double [] arrayCoverage = new double[10];
		for(int i=0; i < 10; i++){
			UnitTestGenerator unitTestGenerator = new UnitTestGenerator(method1.toLowerCase(), i);
			
			ExecutorService executor = Executors.newFixedThreadPool(1);
			executor.execute(unitTestGenerator);
			executor.shutdown();
			
			boolean normalTermination = true;
			
			String error = null;
			try{
				executor.awaitTermination(120, TimeUnit.SECONDS);
				if(unitTestGenerator.isProcessAlive()){
					unitTestGenerator.stopNow();
					normalTermination = false;
					error = "Error generation Unit Test. Generation took more than 120 seconds to execute. Process was killed.";
				}
			}catch(Throwable e){
				error = "Error generating Unit Test. " + e.getMessage();
				normalTermination = false;
			}
			
			if(normalTermination == false){
				return 0;
			}
			
			Double coverage = getCoverage();
			
			if(coverage == null){
				return 0;
			}
			
			arrayCoverage[i] = coverage;
		}
		
		double coverage1=MathUtils.mean(arrayCoverage);
		
		arrayCoverage = new double[10];
		
		for(int i=0; i < 10; i++){
			UnitTestGenerator unitTestGenerator = new UnitTestGenerator(method2.toLowerCase(),i);
	
			ExecutorService executor = Executors.newFixedThreadPool(1);
			executor.execute(unitTestGenerator);
			executor.shutdown();
			
			boolean normalTermination = true;
			
			String error = null;
			try{
				executor.awaitTermination(240, TimeUnit.SECONDS);
				if(unitTestGenerator.isProcessAlive()){
					unitTestGenerator.stopNow();
					normalTermination = false;
					error = "Error generation Unit Test. Generation took more than 240 seconds to execute. Process was killed.";
				}
			}catch(Throwable e){
				error = "Error generating Unit Test. " + e.getMessage();
				normalTermination = false;
			}
			
			if(normalTermination == false){
				return 0;
			}
			
			Double coverage = getCoverage();
			
			if(coverage == null){
				return 0;
			}
			
			arrayCoverage[i] = coverage;
		}
		
		double coverage2=MathUtils.mean(arrayCoverage);
		
		if(coverage1 == 0 || coverage2 == 0){
			return 0;
		}
		
		return (coverage1 / coverage2);
	}
	
	private Double getCoverage(){
		
		Double coverage = null;
		
		try{
                    List<String> lineList = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/resources/temp/evosuiteOutput.txt"));

                    for(String line : lineList){

                        if(line.contains("LINE:")){
                                String [] results = line.split("LINE: ");
                                coverage = Double.valueOf(results[1].split("%")[0]);
                                break;
                        }

                        if(line.contains("BRANCH:")){
                                String [] results = line.split("BRANCH: ");
                                coverage = Double.valueOf(results[1].split("%")[0]);
                                break;
                        }

                        if(line.contains("BRANCH_CLF:")){
                                String [] results = line.split("BRANCH_CLF: ");
                                coverage = Double.valueOf(results[1].split("%")[0]);
                                break;
                        }

                        if(line.contains("BRANCH_BDF:")){
                                String [] results = line.split("BRANCH_BDF: ");
                                coverage = Double.valueOf(results[1].split("%")[0]);
                                break;
                        }

                        if(line.contains("BRANCH_CFF:")){
                                String [] results = line.split("BRANCH_CFF: ");
                                coverage = Double.valueOf(results[1].split("%")[0]);
                                break;
                        }

                        if(line.contains("BRANCH_COM1:")){
                                String [] results = line.split("BRANCH_COM1: ");
                                coverage = Double.valueOf(results[1].split("%")[0]);
                                break;
                        }

                        if(line.contains("BRANCH_COM2:")){
                                String [] results = line.split("BRANCH_COM2: ");
                                coverage = Double.valueOf(results[1].split("%")[0]);
                                break;
                        }

                        if(line.contains("BRANCH_DC:")){
                                String [] results = line.split("BRANCH_DC: ");
                                coverage = Double.valueOf(results[1].split("%")[0]);
                                break;
                        }
                    }
		}catch(Exception e){
			System.exit(1);
		}
		
		return coverage;
	}

    @Override
    public boolean isNatural() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
