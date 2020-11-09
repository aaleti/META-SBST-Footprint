package footprints.fitnessEvaluator;
import footprints.IO.InputData;
import footprints.individual.Individual;
import footprints.learning.SVMType;
import footprints.learning.SupportVectorMachines;
import marytts.util.math.MathUtils;
import org.uncommons.maths.binary.BitString;

public class FitnessEvaluatorSVM extends FitnessEvaluatorPCA{
	
    private final SVMType svmtT= SVMType.FAST_MODE;
    protected final int[] labels;

     public FitnessEvaluatorSVM(InputData dataInfoDTO) {
        super(dataInfoDTO);
        labels=dataInfoDTO.getLabelIntegerArray();
    }

    @Override
    public Individual evaluate(BitString candidate){
        SupportVectorMachines supportVectorMachines = new SupportVectorMachines();
        Individual ind=super.evaluate(candidate);
        double[][] comps=ind.getRedComponentsArray();
        Object [] svmHash = null;

        switch (svmtT) {
        case GRID_MODE:
            svmHash = supportVectorMachines.createHashTrainSVM(comps,labels);		
            double accuracy = supportVectorMachines.svmGridMode((double [][]) svmHash[0], (double []) svmHash[1]);
            ind.setFitness(accuracy);
            break;

        case EASY_MODE:
            break;

        case FAST_MODE:
            double [] reliability = new double[10];
       
            for(int i=0; i < reliability.length; i++){
                svmHash = supportVectorMachines.createHashSVM(comps, labels);
                supportVectorMachines.svmTrain((double [][]) svmHash[0], (double []) svmHash[1], false);
                reliability[i] = supportVectorMachines.svmTest((double [][]) svmHash[2], (double []) svmHash[3]);
            }
            double meanReliabilitySVM = MathUtils.mean(reliability);
            ind.setFitness(meanReliabilitySVM);
            break;

        default:
            break;
        }
        return ind;

    }
	
}