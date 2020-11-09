package footprints.learning;

import footprints.IO.InputData;

/**
 *
 * @author aldeidaaleti
 */
public abstract class Learning extends Thread{
    protected InputData inputData;
    
    public void init(String inputFile){
        inputData=new InputData();
        inputData.readInputDataFromFile(inputFile);
        inputData.removeCorrelatedData();
        inputData.removeNoVarianceAndZeroMean();
        }
    public abstract void finalise();
    public abstract void learn();
}
