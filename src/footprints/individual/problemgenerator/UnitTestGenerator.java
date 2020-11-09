/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package footprints.individual.problemgenerator;

import java.io.File;
import org.apache.log4j.Logger;

/**
 *
 * @author aldeidaaleti
 */
public class UnitTestGenerator extends Thread {
		
		protected final Logger logger = Logger.getLogger("com.cadu");
		
		private int exitValue = 0;
		
		private Process p;
		
		private String method;
		
		private int seed;
		
		public UnitTestGenerator(String method, int seed) {
			super();
			this.method = method;
			this.seed = seed;
		}

		private synchronized void executeUnitTests(){
			
			logger.debug("Generating tests to the method " + method + "...");
			
			String evosuiteCommand = "java -Xms512m -Xmx2048m -jar " + System.getProperty("user.dir") + "/resources/evosuite-master-1.0.5.jar -generateSuite -Duse_separate_classloader=false -base_dir " + System.getProperty("user.dir") + "/resources/temp/com/cadu -class com.cadu.GeneratedClass -seed " + this.seed + " -projectCP " + System.getProperty("user.dir") + "/resources/temp -Dsearch_budget=60 -Dstopping_condition=MaxTime -Dtest_archive=true -Dminimize=true -Dcriterion=branch_" + method;
			
			ProcessBuilder evosuiteBuilder = new ProcessBuilder(evosuiteCommand.split("\\s"));
			
			evosuiteBuilder.redirectOutput(new File(System.getProperty("user.dir") + "/resources/temp/evosuiteOutput.txt"));
			
			p = null;
			try{
				p = evosuiteBuilder.start();
				
				int executionReturn = p.waitFor();
				
			}catch(Exception e){
				logger.error("Error executing evosuite.");
				System.exit(1);
			}
		}
		
                @Override
		public void run() {
			executeUnitTests();
		}

		public void stopNow() {
			p.destroy();
		}
		
		public int getProcessExitValue(){
			return p.exitValue();
		}

		public int getExitValue() {
			return exitValue;
		}

		public void setExitValue(int exitValue) {
			this.exitValue = exitValue;
		}
		
		public int getSeed() {
			return seed;
		}

		public void setSeed(int seed) {
			this.seed = seed;
		}
		
		public boolean isProcessAlive(){
			try{
				exitValue = p.exitValue();
			}catch(Exception e){
				exitValue = -1;
				return true;		
			}
			
			return false;
		}
	}
