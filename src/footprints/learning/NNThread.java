package footprints.learning;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;

public class NNThread extends Thread {
	
	private NeuralNetwork neuralNetwork;
	
	private DataSet trainingSet;
	
	boolean stopped = false;
	
	public NNThread(NeuralNetwork neuralNetwork, DataSet trainingSet) {
		super();
		this.neuralNetwork = neuralNetwork;
		this.trainingSet = trainingSet;
	}

	private synchronized void learn(){
		
		neuralNetwork.learn(trainingSet);
		
		stopped = true;
	}
	
        @Override
	public void run() {
		learn();
	}
	
	public void stopLearning(){
		neuralNetwork.stopLearning();
	}
	
	public boolean isStopped() {
		return stopped;
	}

}
