package footprints.fitnessEvaluator;

import footprints.IO.InputData;
import footprints.individual.Individual;
import footprints.individual.IndividualANN;
import footprints.learning.PrincipalComponentAnalysis;
import java.util.List;
import marytts.util.math.MathUtils;
import org.uncommons.maths.binary.BitString;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

public class FitnessEvaluatorANN implements FitnessEvaluator<BitString> {


private InputData dataInfoDTO;

     public FitnessEvaluatorANN(InputData dataInfoDTO) {
        super();
        this.dataInfoDTO = dataInfoDTO;
    }
    public void evaluate(IndividualANN candidate){   
        
        double [] tenCrossValidation = new double[2];
        for(int i=0; i < 2; i++){
            Object[] hashANN = candidate.createHashANN(dataInfoDTO.getLabelIntegerArray());
            candidate.createTrainingDataSet((double[][]) hashANN[0], (double[][]) hashANN[1]);
            candidate.createTestDataSet((double[][]) hashANN[2], (double[][]) hashANN[3]);

            candidate.trainNetwork();

            double accuracy = candidate.testNetwork();
            tenCrossValidation[i] = accuracy;
        }	

        candidate.setFitness(MathUtils.mean(tenCrossValidation));

    }


    @Override
    public boolean isNatural() {
        return true;
    }

    @Override
    public double getFitness(BitString candidate, List<? extends BitString> list) {
       Individual individual;
            individual=evaluate(candidate);
            return individual.getFitness();

    }
    public Individual evaluate(BitString candidate){
    
            IndividualANN ind = new IndividualANN(candidate, dataInfoDTO.getItemList());
            ind.setGenotype(candidate);
                      
        if(ind.getGenotype().countSetBits()>1){
            PrincipalComponentAnalysis pcaLearn=new PrincipalComponentAnalysis();
         pcaLearn.eigenPCA(ind, null, true, 2);  
            evaluate(ind);
        }
        else
            ind.setFitness(0.0);
        return ind;
  
        
    }
}
