/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package footprints.learning;

import java.util.ArrayList;
import footprints.fitnessEvaluator.FitnessEvaluatorGP;
import footprints.individual.Individual;
import footprints.individual.Memory;
import footprints.individual.problemgenerator.IndividualGP;

/**
 *
 * @author aldeidaaleti
 */
public class GeneticProgramming extends PopulationBasedOptimisation{
    
    FitnessEvaluatorGP fitnessEval = new FitnessEvaluatorGP();
    @Override
    public void learn(){  	
        ArrayList<Individual> listIndividuals = new ArrayList<>();
	for(int i=0; i<populationSize; i++){
            IndividualGP indiv = new IndividualGP();
            indiv.initialiseValue(inputData);
            fitnessEval.evaluate(indiv);
            listIndividuals.add(indiv);
        }
        memory = new Memory(listIndividuals);
		
        for(int x=0;x<=numberOfGenerations;x++){
            for(int i=0; i < listIndividuals.size(); i++){
                IndividualGP sol = (IndividualGP) rouletteWheelSelection(listIndividuals);
                for(int p=0; p < numberOfClones; p++){
                    IndividualGP solCloned = (IndividualGP) sol.clone();
                    //Cell celula2Original = (Cell) celula2.clone();
                    //populacao.cruza(celula1Original, celula2Original);
                    solCloned.mutate();
                    fitnessEval.evaluate(solCloned);
                    memory.addIndividual(solCloned);
                }
               System.out.println(memory.getListIndividuals().size());
            }
            memory.sort(); 
            
            System.out.println(memory.getBestSolution().toString());
        }
    }
    
}
