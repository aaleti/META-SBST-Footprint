package footprints.fitnessEvaluator;
import java.util.List;
import footprints.IO.InputData;
import footprints.individual.Individual;
import footprints.learning.PrincipalComponentAnalysis;

import org.uncommons.maths.binary.BitString;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

public class FitnessEvaluatorPCA implements FitnessEvaluator<BitString> {

	private final InputData dataInfoDTO;
        private final PrincipalComponentAnalysis pcaLearn=new PrincipalComponentAnalysis();
        public InputData getInputData(){
            return dataInfoDTO;
        }
	
	public FitnessEvaluatorPCA(InputData dataInfoDTO) {
            super();
            this.dataInfoDTO = dataInfoDTO;
	}

	@Override
	public double getFitness(BitString candidate,
			List<? extends BitString> population) {
            if(candidate.countSetBits()<2){
                return 0.0;
            }
            Individual individualPCA=evaluate(candidate);
                return individualPCA.getFitness();	
	}
	
	public Individual evaluate(BitString candidate){
            Individual ind = new Individual(candidate,dataInfoDTO.getItemList());
            evaluate(ind);
            return ind;
	}

        public void evaluate(Individual candidate){        
            pcaLearn.eigenPCA(candidate, null, true, 2);  
        }
 

	@Override
	public boolean isNatural() {
            return true;
	}

}
