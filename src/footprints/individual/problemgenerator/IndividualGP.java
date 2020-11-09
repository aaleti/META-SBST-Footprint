package footprints.individual.problemgenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import footprints.IO.InputData;
import footprints.IO.CEFileUtils;
import footprints.individual.Individual;
import footprints.individual.Memory;
import org.apache.commons.io.FileUtils;

public class IndividualGP extends Individual{
	private ConditionTree [] conditionTree;
        private final double mutationRate=0.7;
        private String className;
        private final String hollowClass = "package com.cadu; public class GeneratedClass { public void method1 #1 }";
        
        public String getHollowClass(){
            return hollowClass;
        }
	public ConditionTree[] getConditionTree() {
            return conditionTree;
	}

	public void setConditionTree(ConditionTree[] valor) {
            this.conditionTree = conditionTree.clone();
	}


	@Override
	public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(conditionTree);
            return result;
	}


	public void initialiseValue(InputData inputData)  {
		
            int numberOfConditions =  1;

            List<ConditionTree> solutionList = new ArrayList<>();
            //for (int i = 0; i < numberOfConditions; i++) {
            int conditionTypeId = new Random().nextInt(ConditionTypes.values().length);
            int conditionArity = 1;
            int conditionDepth =  3;

            ConditionTree condition = new ConditionTree(conditionDepth, conditionArity);
            condition.generate();

            solutionList.add(condition);
            //}

            this.conditionTree = new ConditionTree[numberOfConditions];

            for(int i=0; i < numberOfConditions; i++){
                    this.conditionTree[i] = solutionList.get(i);
            }
	}
	

	public void mutate() {
		conditionTree[0].mutate(mutationRate);
	}
	
	public void plot(Memory memoria, int generation, long currentTime){
		Individual bestIndividual = (Individual) memoria.getListIndividuals().get(0);
		System.out.println("<<<<<<<<<<<<" + bestIndividual.getFitness() + ">>>>>>>>>>>>>");
		
		CEFileUtils.writeFile(String.valueOf(generation), System.getProperty("user.dir") + "/resources/temp/", "generation.txt");
	}
	
        @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndividualGP other = (IndividualGP) obj;
		if (!Arrays.equals(conditionTree, other.conditionTree))
			return false;
		return true;
	}


    @Override
    public Individual clone() {
        IndividualGP ind = new IndividualGP();
        ConditionTree [] newcon = new ConditionTree [conditionTree.length];
        for(int i=0; i<conditionTree.length;i++){
           newcon[i]=conditionTree[i].clone();
            
        }
        ind.className=className;
        return ind;
    }
    
    public void saveResults(){
        try{
            FileUtils.cleanDirectory(new File(System.getProperty("user.dir") + "/resources/generatedClasses/" + className + "/"));
        }catch(IOException e){
            System.exit(1);
        }
        String generatedClass = hollowClass.replace("#1", conditionTree[0].generate());
        CEFileUtils.writeFile(generatedClass, System.getProperty("user.dir") + "/resources/generatedClasses/" + className+ "/", "[" + getFitness() + "].java");

    }
}
