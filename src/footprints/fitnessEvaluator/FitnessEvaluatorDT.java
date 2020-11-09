package footprints.fitnessEvaluator;
import java.util.ArrayList;
import java.util.List;
import footprints.IO.InputData;
import footprints.individual.DataItem;
import footprints.individual.Individual;
import footprints.learning.PrincipalComponentAnalysis;
import org.uncommons.maths.binary.BitString;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Debug.Random;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;

public class FitnessEvaluatorDT implements FitnessEvaluator<BitString>{
    
    private String results="";
    private final InputData inputData;
    public FitnessEvaluatorDT(InputData dataInfoDTO) {
        super();
        this.inputData = dataInfoDTO;
    }

    public Individual evaluate(BitString candidate){
        try{
        Individual ind= new Individual(candidate,inputData.getItemList());
        if(ind.getGenotype().countSetBits()>1)
            evaluate(ind);
        else
            ind.setFitness(0.0);
        return ind;
        }
        catch(Exception ex){
            System.out.print(ex);
            return null;
        }
        
    }

    public void evaluateSpecial(Individual ind){
           
        ArrayList<Attribute> allAttributes = new ArrayList<>();
        
        for(DataItem it:ind.getSelectedItemList()){
            Attribute att = new Attribute(it.getItemName());
            allAttributes.add(att);
	}
        List<String> labels=new ArrayList<>();
        for(String label: inputData.getUniqueLabels()){
            labels.add(label);
        }
        allAttributes.add(new Attribute("Label", labels));
        PrincipalComponentAnalysis pcaLearn=new PrincipalComponentAnalysis();
        pcaLearn.eigenPCA(ind, null, true, 2);
        double[][] compsArray=ind.getFullCompsArray();
        
        Instances trainDataset = new Instances("TestInstances", allAttributes , compsArray[0].length);
        Instances dataSet = new Instances("TestInstances", allAttributes , compsArray[0].length);
        
        trainDataset.setClassIndex(trainDataset.numAttributes() - 1);
        dataSet.setClassIndex(dataSet.numAttributes() - 1);

        
        ArrayList<Instance> allInstances = new ArrayList<>();
        String[] allLabels=inputData.getLabelsStringArray();
        for(int i = 0; i < compsArray[0].length; i++){
            Instance instance = new DenseInstance(allAttributes.size());

            for(int p = 0; p < compsArray.length; p++){
                String name=ind.getSelectedItemList().get(p).getItemName();
                instance.setValue(trainDataset.attribute(name), compsArray[p][i]);
            }
           instance.setValue(trainDataset.attribute("Label"), allLabels[i]);
           allInstances.add(instance);
        }
        trainDataset.addAll(allInstances);
        
         ArrayList<Instance> allInst = new ArrayList<>();
        for(int i = 0; i < compsArray[0].length; i++){
            Instance instance = new DenseInstance(allAttributes.size());

            for(int p = 0; p < compsArray.length; p++){
                String name=ind.getSelectedItemList().get(p).getItemName();
                instance.setValue(dataSet.attribute(name), compsArray[p][i]);
            }
           instance.setValue(dataSet.attribute("Label"), allLabels[i]);
           allInst.add(instance);
        }
        dataSet.addAll(allInst);
        
       J48 classifier = new J48();         // new instance of tree
       
       //MultilayerPerceptron classifier = new MultilayerPerceptron();
      try{
            for(int i=0; i < 19; i++){
                SMOTE s = new SMOTE();
		s.setOptions(new String[]{"-K 2","-P 100","-S 1"});
		s.setInputFormat(trainDataset);
		trainDataset = Filter.useFilter(trainDataset, s);
	}	 
	}catch(Exception e){
            System.out.print(e.toString());
	}

       
        try{

            classifier.buildClassifier(trainDataset);   // build classifier
         }catch(Exception e){
            System.out.println("Error building Weka Classifier. " + e);
            System.exit(1);
         }

        Evaluation eval=null;
        
         try{
            eval = new Evaluation(dataSet);
            eval.crossValidateModel(classifier, dataSet, 10, new Random(1));
            results="\nCorrect:"+eval.correct()+"\nIncorrect:"+eval.incorrect()+"\nSummary"+eval.toSummaryString(true);
            results=results+"\nPrecision:"+eval.weightedPrecision()+"\nRecall:"+eval.weightedRecall()+"\nFmeasure:"+eval.weightedFMeasure();
         }catch(Exception e){
            System.out.println("Error executing Weka crossvalidation. " + e);
            System.exit(1);
         }
         
         double recall=eval.weightedRecall();
         double size = (1-(double)ind.getSelectedItemList().size()/((double)ind.getGenotype().getLength()));
         
         ind.setFitness(eval.weightedRecall()+0.001*size);
    

         ind.setClassifier(classifier);
    }
    public void evaluate(Individual ind){
           
        ArrayList<Attribute> allAttributes = new ArrayList<>();
        
        for(DataItem it:ind.getSelectedItemList()){
            Attribute att = new Attribute(it.getItemName());
            allAttributes.add(att);
	}
        List<String> labels=new ArrayList<>();
        for(String label: inputData.getUniqueLabels()){
            labels.add(label);
        }
        allAttributes.add(new Attribute("Label", labels));
        PrincipalComponentAnalysis pcaLearn=new PrincipalComponentAnalysis();
        pcaLearn.eigenPCA(ind, null, true, 2);
        double[][] compsArray=ind.getFullCompsArray();
        
        Instances trainDataset = new Instances("TestInstances", allAttributes , compsArray[0].length);
        Instances dataSet = new Instances("TestInstances", allAttributes , compsArray[0].length);
        
        trainDataset.setClassIndex(trainDataset.numAttributes() - 1);
        dataSet.setClassIndex(dataSet.numAttributes() - 1);

        
        ArrayList<Instance> allInstances = new ArrayList<>();
        String[] allLabels=inputData.getLabelsStringArray();
        for(int i = 0; i < compsArray[0].length; i++){
            Instance instance = new DenseInstance(allAttributes.size());

            for(int p = 0; p < compsArray.length; p++){
                String name=ind.getSelectedItemList().get(p).getItemName();
                instance.setValue(trainDataset.attribute(name), compsArray[p][i]);
            }
           instance.setValue(trainDataset.attribute("Label"), allLabels[i]);
           allInstances.add(instance);
        }
        trainDataset.addAll(allInstances);
        
         ArrayList<Instance> allInst = new ArrayList<>();
        for(int i = 0; i < compsArray[0].length; i++){
            Instance instance = new DenseInstance(allAttributes.size());

            for(int p = 0; p < compsArray.length; p++){
                String name=ind.getSelectedItemList().get(p).getItemName();
                instance.setValue(dataSet.attribute(name), compsArray[p][i]);
            }
           instance.setValue(dataSet.attribute("Label"), allLabels[i]);
           allInst.add(instance);
        }
        dataSet.addAll(allInst);
        
       J48 classifier = new J48();         // new instance of tree
       //MultilayerPerceptron classifier = new MultilayerPerceptron();
      /* try{
            for(int i=0; i < 19; i++){
                SMOTE s = new SMOTE();
		s.setOptions(new String[]{"-K 2","-P 100","-S 1"});
		s.setInputFormat(trainDataset);
		trainDataset = Filter.useFilter(trainDataset, s);
	}	 
	}catch(Exception e){
            System.out.print(e.toString());
	}*/

       
        try{

            classifier.buildClassifier(trainDataset);   // build classifier
         }catch(Exception e){
            System.out.println("Error building Weka Classifier. " + e);
            System.exit(1);
         }

        Evaluation eval=null;
        
         try{
            eval = new Evaluation(dataSet);
            eval.crossValidateModel(classifier, dataSet, 10, new Random(1));
            results="\nCorrect:"+eval.correct()+"\nIncorrect:"+eval.incorrect()+"\nSummary"+eval.toSummaryString(true);
            results=results+"\nPrecision:"+eval.weightedPrecision()+"\nRecall:"+eval.weightedRecall()+"\nFmeasure:"+eval.weightedFMeasure();
         }catch(Exception e){
            System.out.println("Error executing Weka crossvalidation. " + e);
            System.exit(1);
         }
         
         double recall=eval.weightedRecall();
         double size = (1-(double)ind.getSelectedItemList().size()/((double)ind.getGenotype().getLength()));
         
         ind.setFitness(eval.weightedRecall()+0.001*size);
    

         ind.setClassifier(classifier);
    }
    
    @Override
    public boolean isNatural() {
        return true;
    }
    @Override
    public String toString(){
        return results;
    }

    @Override
    public double getFitness(BitString candidate, List<? extends BitString> list) {
        Individual individual;
        try{
            individual=evaluate(candidate);
            return individual.getFitness();
        }
        catch(Exception ex){
            return 0.0;
        }
                
    }
}
