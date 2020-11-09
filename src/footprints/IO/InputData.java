package footprints.IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;
import footprints.individual.DataItem;
import marytts.util.math.MathUtils;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

public class InputData {
   
    private String[] labelsStringArray; 
    private String[] labelsStringArray2; 
    private int[] labelIntegerArray;
    private int[] labelIntegerArray2;
    private String[] uniqueLabels;
    private String[] uniqueLabels2;
    private List<DataItem> itemList = new ArrayList<>();
    private int numberFeatures;
    private String[] datasetSringLabels;
    private String[] uniqueDatasets;
    private int[] datasetIntLabels;
    
    private boolean header = true;
    private boolean tail = false;
    private String columnSeparator = ",";
    
    public String[] getDatasetSringLabels(){
        return datasetSringLabels;
    }
    public String[] getUniqueDatasets(){
        return uniqueDatasets;
    }
    public int[] getDatasetIntLabels(){
        return datasetIntLabels;
    }
    public InputData() {
            super();
    }
    public int getNumberFeatures(){
        return numberFeatures;
    }
    public List<DataItem> getItemList(){
        return itemList;
    }
    public String[] getUniqueLabels() {
            return uniqueLabels;
    }
    public String[] getUniqueLabels2() {
            return uniqueLabels2;
    }

    public int[] getLabelIntegerArray2() {
            return labelIntegerArray2;
    }
    public int[] getLabelIntegerArray() {
            return labelIntegerArray;
    }

    public String[] getLabelsStringArray() {
            return labelsStringArray;
    }
    public String[] getLabels2StringArray() {
            return labelsStringArray2;
    }

    public void removeCorrelatedData(){

        PearsonsCorrelation correlation = new PearsonsCorrelation();
        List<DataItem> correlatedList = new ArrayList<>();
        for(DataItem dataItemDTO : itemList){
            for(DataItem dataItemDTO2 : itemList){

                if(Objects.equals(dataItemDTO.getItemId(), dataItemDTO2.getItemId())){
                    continue;
                }

                double correlationValue = correlation.correlation(dataItemDTO.getItemValues(), dataItemDTO2.getItemValues());

                if(correlationValue < -0.7 || correlationValue > 0.7){

                    if(correlatedList.contains(dataItemDTO) == false && correlatedList.contains(dataItemDTO2) == false){
                        correlatedList.add(dataItemDTO2);
                    }
                }
            }
        }
        itemList.removeAll(correlatedList);
    }
      public void removeNoVarianceAndZeroMean(){
          List<DataItem> noVarianceAndMeanZeroList = new ArrayList<>();
        for(DataItem dataItemDTO : itemList){
            if(MathUtils.mean(dataItemDTO.getItemValues()) == 0 || MathUtils.variance(dataItemDTO.getItemValues()) == 0){
                noVarianceAndMeanZeroList.add(dataItemDTO);
            }
        }
        itemList.removeAll(noVarianceAndMeanZeroList);
    }
    public void dataTransformation() {	
        for(DataItem dataItemDTO : itemList){
            double [] logTransformedData = logTransformation(dataItemDTO);
            double p = kolmogorovSmirnovTest(dataItemDTO.getItemValues());
            double pLog = kolmogorovSmirnovTest(logTransformedData);
            if(pLog < p){
                dataItemDTO.setItemValues(logTransformedData);
            }
        }
    }
    public double[] logTransformation(DataItem dataItemDTO){
        double [] itemArray = dataItemDTO.getItemValues();
        double min = MathUtils.min(dataItemDTO.getItemValues());
        if(min < 1){
            itemArray = MathUtils.add(dataItemDTO.getItemValues(), min + 1);
        }
        return MathUtils.log10(itemArray);
    }
    public void logTransformation(){	
        for(DataItem dataItemDTO : itemList){
            dataItemDTO.setItemValues(MathUtils.log10(dataItemDTO.getItemValues()));
        }
    }
    public double kolmogorovSmirnovTest(double [] dataSet){
        KolmogorovSmirnovTest kolmogorovSmirnovTest = new KolmogorovSmirnovTest();		
        NormalDistribution normalDistribution = new NormalDistribution();		
        double p = kolmogorovSmirnovTest.kolmogorovSmirnovTest(normalDistribution, dataSet);
        return p;
    }
    public void checkNormalDistribution(){
        for(DataItem dataItemDTO : itemList){
            double p = kolmogorovSmirnovTest(dataItemDTO.getItemValues());
            dataItemDTO.setP(p);
            dataItemDTO.setNormalDistributed(false);
            if(p < 0.1){
                    dataItemDTO.setNormalDistributed(true);
            }
        }
    }
   /* public InputData(String fearturesF, String labelF, List<String> selectedFeatures){	
	super();	
        itemList= new ArrayList<>();
	numberFeatures=selectedFeatures.size();
        
        ExtractedData exFData=extract(fearturesF);
        for(Integer key : exFData.fList.keySet()){

            if(selectedFeatures.contains(exFData.fList.get(key)) == false){
                continue;
            }

            List<String> featureSList = exFData.eFeaturesList.get(exFData.fList.get(key));
            double [] featureArray =  new double[featureSList.size()];
            for(int i=0; i < featureSList.size(); i++){
                featureArray[i] = Double.parseDouble(featureSList.get(i));
            }

            DataItem dataItemDTO = new DataItem();
            dataItemDTO.setItemId(key);
            dataItemDTO.setItemValues(featureArray);
            dataItemDTO.setItemName(exFData.fList.get(key));
            itemList.add(dataItemDTO);

        }
        ExtractedData exLData=extract(labelF);
        for(Integer key : exLData.fList.keySet()){

                if(exLData.fList.get(key).equals("Label String")){
                        List<String> labelList = exLData.eFeaturesList.get(exLData.fList.get(key));

                        List<String> labelStringList =  new ArrayList<>();
                        for(String feature : labelList){
                                if(labelStringList.contains(feature) == false){
                                    labelStringList.add(feature);
                                }
                        }

                        uniqueLabels = labelStringList.toArray(new String[labelStringList.size()]);
                        labelsStringArray = labelList.toArray(new String[labelList.size()]);

                }else if(exLData.fList.get(key).equals("Label Integer")){
                    List<String> featureSList = exLData.eFeaturesList.get(exLData.fList.get(key));
                    labelIntegerArray =  new int[featureSList.size()];
                    for(int i=0; i < featureSList.size(); i++){
                        labelIntegerArray[i] = Integer.parseInt(featureSList.get(i));
                    }

                }
			
            }
	}*/
    
    private ExtractedData extract(String fileName){
        HashMap<Integer, String> featList=new HashMap<>();
        HashMap<String, List<String>> extractedFeaturesList=new HashMap<>();
        HashMap<String, List<String>> tailList=new HashMap<>();

        Pattern pattern = Pattern.compile(columnSeparator);

        long numberOfLines=0;
        try{
            numberOfLines= Files.lines(Paths.get(fileName)).count();
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        int lineCount = 0;
        long lineLimit = tail == false ? numberOfLines : (numberOfLines - 1);


        BufferedReader reader = CEFileUtils.openFile(fileName);

        String line = CEFileUtils.readLine(reader);
        lineCount++;

        String [] lineArray = null;
        if(line != null){
          lineArray = pattern.split(line, 0);

            for(int i=1; i <= lineArray.length; i++){
                String colName = header ? lineArray[i-1] : ("col" + (i-1));

                if(featList.containsKey(i) == false){
                        featList.put(i, colName);
                        extractedFeaturesList.put(colName, new ArrayList<>(40000));
                        if(tail){
                            tailList.put(colName, new ArrayList<>());
                        }
                }
            }
        }

        if(header){
            line = CEFileUtils.readLine(reader);
            lineCount++;
        }	

        for(;line != null && lineCount <= lineLimit; lineCount++){
            lineArray = pattern.split(line, 0);
            for(int i=1; i <= lineArray.length; i++){
              extractedFeaturesList.get(featList.get(i)).add(lineArray[i-1]);
            }
            line = CEFileUtils.readLine(reader);
        }

        if(tail){
            lineArray = pattern.split(line, 0);
            for(int i=1; i <= lineArray.length; i++){
                tailList.get(featList.get(i)).add(lineArray[i-1]);
            }

        }
        ExtractedData extractedData = new ExtractedData(featList, extractedFeaturesList, tailList);
        return extractedData;
    }
    public class ExtractedData{
        HashMap<Integer, String> fList=new HashMap<>();
        HashMap<String, List<String>> eFeaturesList=new HashMap<>();
        HashMap<String, List<String>> tList=new HashMap<>();  
        public ExtractedData(HashMap<Integer, String> fL, HashMap<String, List<String>> eL,HashMap<String, List<String>> tL){
            fList=fL;
            eFeaturesList=eL;
            tList=tL;
        }
    }
    public void readInputDataFromFile(String inputFile){      
        ExtractedData exData=extract(inputFile);    
        numberFeatures=exData.fList.size();
        for(int key : exData.fList.keySet()){
            if(!exData.fList.get(key).equals("Label") && !exData.fList.get(key).equals("Instance")&& !exData.fList.get(key).equals("Label2")){
                List<String> featureSList = exData.eFeaturesList.get(exData.fList.get(key));
                double[] featureArray =  new double[featureSList.size()];
                for(int i=0; i < featureSList.size(); i++){
                    featureArray[i] = Double.parseDouble(featureSList.get(i));
  
                }
                DataItem dataItemDTO = new DataItem();
                dataItemDTO.setItemId(key);
                dataItemDTO.setItemValues(featureArray);
                dataItemDTO.setItemName(exData.fList.get(key));
                itemList.add(dataItemDTO);
            }

            else if(exData.fList.get(key).equals("Label")){
                HashMap<String, Integer> hashLabelInteger = new HashMap<>();
                int labelId = 0;
                List<String> fullLabelStringList = exData.eFeaturesList.get(exData.fList.get(key));

                List<String> labelStringArr =  new ArrayList<>();
                labelIntegerArray =  new int[fullLabelStringList.size()];

                for(int i=0; i < fullLabelStringList.size(); i++){
                    if(hashLabelInteger.get(fullLabelStringList.get(i)) == null){
                         hashLabelInteger.put(fullLabelStringList.get(i), labelId++);
                    }

                    labelIntegerArray[i] = hashLabelInteger.get(fullLabelStringList.get(i));

                    if(labelStringArr.contains(fullLabelStringList.get(i)) == false){
                            labelStringArr.add(fullLabelStringList.get(i));
                    }
                }

                uniqueLabels=labelStringArr.toArray(new String[labelStringArr.size()]);
                labelsStringArray=fullLabelStringList.toArray(new String[fullLabelStringList.size()]);

            }
             else if(exData.fList.get(key).equals("Label2")){
                HashMap<String, Integer> hashLabelInteger2 = new HashMap<>();
                int labelId = 0;
                List<String> fullLabelStringList2 = exData.eFeaturesList.get(exData.fList.get(key));

                List<String> labelStringArr2 =  new ArrayList<>();
                labelIntegerArray2 =  new int[fullLabelStringList2.size()];

                for(int i=0; i < fullLabelStringList2.size(); i++){
                    if(hashLabelInteger2.get(fullLabelStringList2.get(i)) == null){
                         hashLabelInteger2.put(fullLabelStringList2.get(i), labelId++);
                    }

                    labelIntegerArray2[i] = hashLabelInteger2.get(fullLabelStringList2.get(i));

                    if(labelStringArr2.contains(fullLabelStringList2.get(i)) == false){
                            labelStringArr2.add(fullLabelStringList2.get(i));
                    }
                }

                uniqueLabels2=labelStringArr2.toArray(new String[labelStringArr2.size()]);
                labelsStringArray2=fullLabelStringList2.toArray(new String[fullLabelStringList2.size()]);

            }
            else if(exData.fList.get(key).equals("Instance")){
           
                
                List<String> instanceLabel = exData.eFeaturesList.get(exData.fList.get(key));
                datasetSringLabels = instanceLabel.toArray(new String[instanceLabel.size()]);
                datasetIntLabels = new int[instanceLabel.size()];
                HashMap<String, Integer> hashLabelInteger = new HashMap<>();
                int labelId = 0;
                List<String> labelStringArr =  new ArrayList<>();
                
                for(int i=0; i < instanceLabel.size(); i++){
                    if(hashLabelInteger.get(instanceLabel.get(i)) == null){
                         hashLabelInteger.put(instanceLabel.get(i), labelId++);
                    }

                    datasetIntLabels[i] = hashLabelInteger.get(instanceLabel.get(i));

                    if(labelStringArr.contains(instanceLabel.get(i)) == false){
                            labelStringArr.add(instanceLabel.get(i));
                    }
                }
                uniqueDatasets=labelStringArr.toArray(new String[labelStringArr.size()]);
                    
            }
            
        }   
    }
}
