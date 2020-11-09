/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package footprints.learning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import footprints.individual.Individual;
import footprints.individual.Memory;

/**
 *
 * @author aldeidaaleti
 */
public class PopulationBasedOptimisation extends Learning{

    protected Memory memory;
    protected final int populationSize=100;
    protected final int numberOfGenerations =20;
    protected final int numberOfClones=2;
    @Override
    public void finalise() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void learn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Individual rouletteWheelSelection(ArrayList<Individual> listInds) {  

        Collection<Object[]> roleta = new ArrayList<>(); 
    	double fitnessSum = 0;  
        double fitnessAcumulado = 0; 
        Iterator<Individual> i = listInds.iterator();  
    	while (i.hasNext()) {  
            fitnessSum += i.next().getFitness();  
        }  
    	Iterator<Individual> i2 = listInds.iterator();  
	while (i2.hasNext()) {  
	   Individual individuo = i2.next();  
	   
	   roleta.add(new Object[] {  
               fitnessAcumulado, fitnessAcumulado + individuo.getFitness()
                       / fitnessSum, individuo });  
	   
	    		fitnessAcumulado += individuo.getFitness() / fitnessSum;  
	    	}  
	
    	
    	Individual selecionado = null;   	
    	double sorteio = Math.random();  
  
    	Iterator<Object[]> it3 = roleta.iterator();  
    	while (it3.hasNext()) {  
            Object[] atual = it3.next();  

            double limiteInferior = ((Double) atual[0]);  
            double limiteSuperior = ((Double) atual[1]);  
            Individual individuo = (Individual) atual[2];  

            if (sorteio >= limiteInferior && sorteio < limiteSuperior) {  
                    selecionado = individuo;  
                    break;  
            }  
    	}   	
    	return selecionado;
    }
}
