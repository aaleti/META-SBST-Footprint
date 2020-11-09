package footprints;
import footprints.learning.Learning;
import footprints.learning.GeneticAlgorithm;

import org.apache.commons.cli.ParseException;


public class Main{
    private static final String INPUTFILE=System.getProperty("user.dir") + "/data/programRepair/bothGranularMultiple.csv";
    public static void main(String [] args) throws ParseException{

        Learning learningAlgorithm = new GeneticAlgorithm();
        learningAlgorithm.init(INPUTFILE);
        learningAlgorithm.learn();
        learningAlgorithm.finalise();
    }  
		
}
