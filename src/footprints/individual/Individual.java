/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package footprints.individual;

import Jama.Matrix;
import footprints.IO.InputData;
import footprints.IO.Plotting;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.uncommons.maths.binary.BitString;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

/**
 *
 * @author aldeidaaleti
 */
public class Individual implements Comparable<Object>{
    
    protected BitString genotype;
    protected List<DataItem> selectedFeaturesItems; 
    private double fitness;
    private Matrix data;	
    private double[][] fullComponentsArray;  
        private double[][] redComponentsArray;
    
    

    private Matrix eigenVectors;
    private Classifier classifier;

        public Matrix getEigenVectors() {
        return eigenVectors;
    }

    public void setEigenVectors(Matrix eigenVectors) {
            this.eigenVectors = eigenVectors;
    }

    public void setRedComponentsArray(double[][] componentsArray) {
        this.redComponentsArray = componentsArray;
    }

    public double[][] getRedComponentsArray() {
        return redComponentsArray;
    }
    
    public void setClassifier(Classifier classi){
        classifier=classi;
    }
    public Classifier getClassifier(){
        return classifier;
    }
    
    public List<DataItem> getSelectedItemList() {
            return selectedFeaturesItems;
    }

    public double[][] getFullCompsArray()
    {
        return fullComponentsArray;
    }
    
    public void setFullCompsArray(double[][] newcomps){
        fullComponentsArray=newcomps;
    }
    public void setItemList(List<DataItem> itemList) {
            this.selectedFeaturesItems = itemList;
    }
    
    public void setGenotype(BitString gene,List<DataItem> allItems){
       genotype=gene;
       selectedFeaturesItems.clear();
       for(int i=0, x = allItems.size() - 1; i < allItems.size(); i++, x--){
            DataItem item = allItems.get(i);
            if(gene.getBit(x)){
                selectedFeaturesItems.add(item);
            }
        }
       convertData();
    }
    public Individual(List<DataItem> selItems){
        super();
        selectedFeaturesItems=selItems;
        convertData();
        
    }
    public void initialiseValue() {
        Random random = new Random();
        for(int i=0; i < genotype.getLength(); i++){
           if(random.nextDouble()>0.5)
               genotype.setBit(i, true);
               
           else
               genotype.setBit(i, false);
            
        }

    } 
    public Individual(){
        super();
    }

    public Matrix getData() {
        return data;
    }

    public void setData(Matrix data) {
            this.data = data;
    }
    
    public Individual(BitString gene, List<DataItem> allItems){
        super();
        genotype=gene;
        selectedFeaturesItems = new ArrayList<>();
        for(int i=0, x = allItems.size() - 1; i < allItems.size(); i++, x--){
            DataItem item = allItems.get(i);
                if(gene.getBit(x)){
                    selectedFeaturesItems.add(item);
                }
            }
        convertData();
    }
     public void convertData(){
        double[][] dat = new double[selectedFeaturesItems.size()][((DataItem) selectedFeaturesItems.get(0)).getItemValues().length];
        for(int i=0; i < selectedFeaturesItems.size(); i++){
            dat[i] = Arrays.copyOf(selectedFeaturesItems.get(i).getItemValues(), selectedFeaturesItems.get(i).getItemValues().length);
        }
        data=new Matrix(dat);
    }
    public double getFitness(){
        return fitness;
    }
    public void setFitness(double fit){
        fitness=fit;
    }
    public BitString getGenotype(){
        return genotype;
    }
    
    public void setGenotype(BitString gen){
        genotype=gen;
    }
        @Override
    public int compareTo(Object other)  throws ClassCastException{
        Double otherFitness=((Individual)other).getFitness();
        Double thisFitness=fitness;
        return thisFitness.compareTo(otherFitness);
    }

    public void plotAllAlgorithmFootprint(InputData inputD){   
        
        DefaultXYDataset dataset = createXYDataset(inputD.getUniqueLabels(), inputD.getLabelIntegerArray()); 
        Plotting plot1=new Plotting(dataset, "PC1", "PC2");
        //plot1.pack();
        //plot1.setVisible(true);
       plot1.saveChartAsPng(System.getProperty("user.dir") + "/data/graphs/","allFootprints.png");
    }
    public void plotAllAlgorithmFootprint2(InputData inputD){   
        
        DefaultXYDataset dataset = createXYDataset(inputD.getUniqueLabels2(), inputD.getLabelIntegerArray2()); 
        Plotting plot1=new Plotting(dataset, "PC1", "PC2");
        //plot1.pack();
        //plot1.setVisible(true);
       plot1.saveChartAsPng(System.getProperty("user.dir") + "/data/graphs/","allFootprintsIndividualAlgos.png");
    }
    
    public void plotSingleAlgorithmFootprint(InputData inputD){   
        for (String label: inputD.getUniqueLabels()){
            DefaultXYDataset dataset = createSingleXYDataset(label, inputD.getUniqueLabels(), inputD.getLabelIntegerArray()); 
            Plotting plot=new Plotting(dataset, "PC1", "PC2");
            //plot1.pack();
            //plot1.setVisible(true);
            plot.saveChartAsPng(System.getProperty("user.dir") + "/data/graphs/SingleFootprint_",label+".png");
        }
    }
    public void plotFeaturesFootprint(InputData inputD){  
        int nFeatures=selectedFeaturesItems.size();
        //nFeatures=1;
        for(int selectedFeature=0;selectedFeature<nFeatures;selectedFeature++){
            DefaultXYZDataset dataset=createXYZDataset(selectedFeature);
        
            Plotting plot2=new Plotting(dataset, dataset.getSeriesKey(0).toString(), "PC1", "PC2");
            //plot2.pack();
            //plot2.setVisible(true);
            plot2.saveChartAsPng(System.getProperty("user.dir") + "/data/graphs/", dataset.getSeriesKey(0).toString()+".png");
        }
    }
    
    public void plotDatasetFootPrint(InputData inputD){ 
        
        String[] stringL=inputD.getUniqueDatasets();
        int[] intL=inputD.getDatasetIntLabels();
        
        DefaultXYDataset dataset = createXYDataset(stringL, intL); 
        Plotting plot1=new Plotting(dataset, "PC1", "PC2");
        //plot1.pack();
        //plot1.setVisible(true);
       plot1.saveChartAsPng(System.getProperty("user.dir") + "/data/graphs/","footprintDataset.png");
    }
    //working 
    public DefaultXYDataset createSingleXYDataset(String label, String[] labelString, int[] labelInteger){	
        HashMap<String, List<double[]>> hashNodeList = new HashMap<>();
        
        hashNodeList.put(label, new ArrayList<>());
        hashNodeList.put("Other methods", new ArrayList<>());

        for(int i = 0; i < redComponentsArray[0].length; i++){
            for(int p=0; p < labelString.length; p++){
                if(labelInteger[i] == p){
                    if(labelString[p].equals(label)){
                        hashNodeList.get(label).add(new double[]{redComponentsArray[0][i],redComponentsArray[1][i]});
                        break;
                    }
                    else{
                        hashNodeList.get("Other methods").add(new double[]{redComponentsArray[0][i],redComponentsArray[1][i]});
                        break;
                    }
                }
            }
        }
        DefaultXYDataset dataset = new DefaultXYDataset();


        List<double[]> genericList = hashNodeList.get(label);
        double[][] genericArray = new double[2][genericList.size()];
        for(int i = 0; i < genericList.size(); i++){
            genericArray[0][i] = genericList.get(i)[0];
            genericArray[1][i] = genericList.get(i)[1];
        }
        dataset.addSeries(label, genericArray);
        
        List<double[]> genericList2 = hashNodeList.get("Other methods");
        double[][] genericArray2 = new double[2][genericList2.size()];
        for(int i = 0; i < genericList2.size(); i++){
            genericArray2[0][i] = genericList2.get(i)[0];
            genericArray2[1][i] = genericList2.get(i)[1];
        }
        dataset.addSeries("Other methods", genericArray2);
        return dataset;
    }
    public DefaultXYDataset createXYDataset(String [] labelString, int [] labelInteger){	
        HashMap<String, List<double[]>> hashNodeList = new HashMap<>();
        
        for(int p=0; p < labelString.length; p++){
            hashNodeList.put(labelString[p], new ArrayList<>());
        }

        for(int i = 0; i < redComponentsArray[0].length; i++){
            for(int p=0; p < labelString.length; p++){
                if(labelInteger[i] == p){
                    hashNodeList.get(labelString[p]).add(new double[]{redComponentsArray[0][i],redComponentsArray[1][i]});
                    break;
                }
            }
        }

        DefaultXYDataset dataset = new DefaultXYDataset();

        for(String key : hashNodeList.keySet()){
            List<double[]> genericList = hashNodeList.get(key);
            double[][] genericArray = new double[2][genericList.size()];
            for(int i = 0; i < genericList.size(); i++){
                genericArray[0][i] = genericList.get(i)[0];
                genericArray[1][i] = genericList.get(i)[1];
            }
            dataset.addSeries(key, genericArray);
        }
        return dataset;
    }
    public DefaultXYZDataset createXYZDataset(int selectedFeature){	
        DefaultXYZDataset dataset = new DefaultXYZDataset();
        double[] zvalues =selectedFeaturesItems.get(selectedFeature).getItemValues();
        
        double max=0.0;
        for(int i=0;i<zvalues.length;i++){
            if(max<zvalues[i]){
                max=zvalues[i];
            }
            
        }
        double[] normalisedzvalues=new double [zvalues.length];
        for(int i=0;i<zvalues.length;i++){
            normalisedzvalues[i]=zvalues[i]/max;
        }
        
        double[] xvalues = new double[zvalues.length];
        double[] yvalues = new double[zvalues.length]; 

        String feature=selectedFeaturesItems.get(selectedFeature).getItemName();
        for(int i=0;i<zvalues.length;i++){
            xvalues[i]=redComponentsArray[0][i];
            yvalues[i]=redComponentsArray[1][i];       
        }
        double[][] data = new double[][] {xvalues, yvalues, normalisedzvalues};
        dataset.addSeries(feature, data);
        return dataset;
    }
    public void visualiseTree(){
        final javax.swing.JFrame jf = new javax.swing.JFrame("Weka Classifier Tree Visualizer: J48");
        jf.setSize(500,400);
        jf.getContentPane().setLayout(new BorderLayout());
        TreeVisualizer tv=null;
        try{
            tv= new TreeVisualizer(null,((J48)classifier).graph(),  new PlaceNode2());
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        jf.getContentPane().add(tv, BorderLayout.CENTER);
        jf.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    jf.dispose();
                }
        });

     jf.setVisible(true);
     tv.fitToScreen();
    }
    public void mutate() {
        Random random = new Random();
        genotype.flipBit(random.nextInt(genotype.getLength()));   
    }
      @Override
    public String toString(){ 
        String indS=super.toString()+": SELECTED FEATURES: ";
        for(DataItem dt:selectedFeaturesItems)
        indS=indS+dt.getItemName()+",";
        return indS;
    }
    

    


    @Override
    public boolean equals(Object obj) {
        if (genotype.equals(obj))
            return true;
        return false;
    }
}
