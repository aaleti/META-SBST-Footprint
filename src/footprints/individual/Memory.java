
package footprints.individual;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Memory implements Serializable{	
	private List<Individual> listIndividuals;
	private double media;
	protected int generationsWithNoImprovement = 0;

	public Memory(List<Individual> lstIndividuals){
            listIndividuals =lstIndividuals;
            sort();
	}
	
        @Override
        public String toString(){
            String strSol="Population \n";
            for(Individual ind:listIndividuals){
                strSol=strSol+ind.toString();
            }
            return strSol;
        }
	public void clear(){
            listIndividuals.clear();
	}
	public void addIndividual(Individual ind){
            if(listIndividuals.contains(ind)){
                return;
            }
            listIndividuals.add(ind);
	}	
	public List<Individual> getListIndividuals() {
		return listIndividuals;
	}

	public void setListaIndividuals(List<Individual> listaIndividuo) {
		this.listIndividuals = listaIndividuo;
	}

	public void sort(){
		Collections.sort(this.listIndividuals);
	}
        	public double calculaMean(){
		double valorTotalFitness = 0;
		for(Individual individuo : listIndividuals){
			valorTotalFitness = valorTotalFitness+individuo.getFitness();
		}
		
		return valorTotalFitness/this.listIndividuals.size();
	}
        public Individual getBestSolution() {
		return listIndividuals.get(0);
	}

	public void setMean(double media) {
		this.media = media;
	}
	
	public double getMean() {
		return media;
	}

}
