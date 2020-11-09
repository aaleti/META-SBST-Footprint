package footprints.individual;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class IndividualCSP extends Individual{

	private HashMap<String, List<Integer>> hashLabelId = new HashMap<>();
        	private int numberOfClasses;	
	private int numberOfSelectedClasses;
        private HashMap<Integer, String> hashClassName = new HashMap<>();
	private boolean equality = true;

    public IndividualCSP(List<DataItem> selItems) {
        super(selItems);
    }

        public int getNnumberOfSelectedClasses(){
            return numberOfSelectedClasses;
        }

	public int getNumberOfClasses() {
		return numberOfClasses;
	}

	public void setNumberOfClasses(int numberOfClasses) {
		this.numberOfClasses = numberOfClasses;
	}

	public boolean isEquality() {
		return equality;
	}

	public void setEquality(boolean equality) {
		this.equality = equality;
	}

	public int getNumberOfSelectedClasses() {
		return numberOfSelectedClasses;
	}

	public void setNumberOfSelectedClasses(int numberOfSelectedClasses) {
		this.numberOfSelectedClasses = numberOfSelectedClasses;
	}
       

        @Override
	public boolean equals(Object obj) {
		if (genotype.equals(obj))
			return true;
		return false;
	
        }
	public void initialiseValue(int classes) {
		
		int classPerLabel = classes / hashLabelId.keySet().size();
		
		List<Integer> solutionList = new ArrayList<Integer>();
		
		for(String key : hashLabelId.keySet()){
			
			List<Integer> classList = new ArrayList<Integer>();
			for (int i = 0; i < hashLabelId.get(key).size(); i++) {
				classList.add(hashLabelId.get(key).get(i));
			}
			
			for (int i = 0; i < classPerLabel; i++) {
				int variable = classList.remove(new Random().nextInt(classList.size()));
				solutionList.add(variable);
			}
			
		}
		
		int [] arrayValor = new int[classes];
		
		for(int i=0; i < classes; i++){
			arrayValor[i] = solutionList.get(i);
		}
		//setGenotype(arrayValor);
	}
	

	/*public void mutate(int classes) {
		Random random = new Random();
		int classPerLabel = classes / hashLabelId.keySet().size();
		
		int initial = 0;
		int end = classPerLabel;
		
		for(String key : hashLabelId.keySet()){
			
			List<Integer> classList = new ArrayList<Integer>();
			for (int i = 0; i < hashLabelId.get(key).size(); i++) {
				classList.add(hashLabelId.get(key).get(i));
			}
			
			List<Integer> solutionList = new ArrayList<Integer>();
			for(int feature : getGenotype()){
				for(int i=0; i < classList.size(); i++){
					if(feature == classList.get(i).longValue()){
						solutionList.add(classList.remove(i));
						break;
					}
				}
			}
			
			if(solutionList.size() != 300){
				System.out.println("Size: " + solutionList.size());
			}

			
			for (int i=0;classList.size() >= 1; i++){
				
				int position = random.nextInt(end - initial) + initial;
                                int arr=classList.remove(random.nextInt(classList.size()));
				
				changeGene(position,arr);
				
			}
			
			initial = initial + classPerLabel;
			end = end + classPerLabel;
			
		}
		
		//featureList.addAll(solutionList);
	}*/

}
