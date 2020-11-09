package footprints.learning;

import com.sun.tools.doclint.Entity;
import footprints.fitnessEvaluator.FitnessEvaluatorANN;
import footprints.fitnessEvaluator.FitnessEvaluatorPCA;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import footprints.fitnessEvaluator.FitnessEvaluatorDT;
import footprints.fitnessEvaluator.FitnessEvaluatorSVM;
import footprints.fitnessEvaluator.footprint.FitnessEvaluatorFootprint;
import footprints.individual.Individual;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

import org.uncommons.maths.binary.BitString;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.factories.BitStringFactory;
import org.uncommons.watchmaker.framework.operators.BitStringCrossover;
import org.uncommons.watchmaker.framework.operators.BitStringMutation;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

public class GeneticAlgorithm extends Learning {
    private BitString bestSol;
    private FitnessEvaluator<BitString> fitnessEvaluator;

    @Override
    public void learn (){

        CandidateFactory<BitString> candidateFactory = new BitStringFactory(inputData.getItemList().size());

        List<EvolutionaryOperator<BitString>> operators = new LinkedList<>();
        operators.add(new BitStringCrossover());
        operators.add(new BitStringMutation(new Probability(0.6)));

        EvolutionaryOperator<BitString> evolutionaryOperator = new EvolutionPipeline<>(operators);

        fitnessEvaluator= new FitnessEvaluatorDT(inputData);

        SelectionStrategy<Object> selectionStrategy = new RouletteWheelSelection();
        Random rng = new MersenneTwisterRNG();
        EvolutionEngine<BitString> engine = new GenerationalEvolutionEngine<BitString>(candidateFactory, evolutionaryOperator,fitnessEvaluator, selectionStrategy, rng);

        engine.addEvolutionObserver(new EvolutionObserver<BitString>()
        {
            @Override
            public void populationUpdate(PopulationData<? extends BitString> data)
            {
                System.out.printf("Generation %4d: %s : %s \n",
                                  data.getGenerationNumber(),
                                  data.getBestCandidateFitness(), 
                                  data.getBestCandidate());
            }
        });
        
        bestSol = engine.evolve(100, 20, new GenerationCount(150));

    }

    @Override
    public void finalise() {
//System.out.println(ind.toString());
 
        FitnessEvaluatorPCA pca= new FitnessEvaluatorPCA(inputData);
        Individual sol=pca.evaluate(bestSol);
        double var = sol.getFitness();
        FitnessEvaluatorDT dt=new FitnessEvaluatorDT(inputData);
        dt.evaluate(sol);
        
        
        File outF = new File(System.getProperty("user.dir") + "/data/graphs/dt.txt");
        try {
            
            FileUtils.writeStringToFile(outF, sol.getClassifier().toString()+"\n Accuracy of decision tree: "+sol.getFitness()+"\n Explained variance of PCA "+ var+dt.toString());
        } catch (IOException ex) {
            Logger.getLogger(GeneticAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dt.evaluateSpecial(sol);
        try {
            
            FileUtils.writeStringToFile(outF, sol.getClassifier().toString()+"\n Accuracy of decision tree: "+sol.getFitness()+"\n Explained variance of PCA "+ var+dt.toString());
        } catch (IOException ex) {
            Logger.getLogger(GeneticAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
        sol.plotAllAlgorithmFootprint(inputData);
        sol.plotFeaturesFootprint(inputData);
        sol.plotDatasetFootPrint(inputData);
        sol.plotSingleAlgorithmFootprint(inputData);
       // sol.plotAllAlgorithmFootprint2(inputData);
        //ind.visualiseTree();
    }
}
