/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package footprints.individual.problemgenerator;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author aldeidaaleti
 */
public class ConditionStructure implements Serializable{
		
        private ConditionTypes conditionType;
        private final int arity;
        private final int depth;
        private ConditionStructureValue[] conditionValueList;
        private ConditionConnectorType[] connectorList;
        private StringBuilder conditionString;

        @Override
        public ConditionStructure clone(){
            ConditionStructure newcs = new ConditionStructure(depth, arity);
            newcs.conditionType=conditionType;
            ConditionStructureValue[] newConditionValueList = new ConditionStructureValue[conditionValueList.length];
            for(int i=0; i<conditionValueList.length;i++){
                   ConditionStructureValue newcsv=conditionValueList[i].clone();
                   newConditionValueList[i]= newcsv;
            }
            newcs.conditionValueList=newConditionValueList;
            
            ConditionConnectorType[] newConnectorList= new ConditionConnectorType[connectorList.length];
            for(int i=0; i<connectorList.length;i++){
                   ConditionConnectorType newCT=connectorList[i];
                   newConnectorList[i]= newCT;
            }
            newcs.connectorList=newConnectorList;
                    
            return newcs;
            
        }
        
        public ConditionStructureValue[] getConditionValueList (){
            return conditionValueList;
        }
        public ConditionStructure(int depth, int arity) {
            super();
            this.arity = arity;
            this.depth = depth;
    }

        public void mutate(){
            for(int i=0; i < arity; i++){
                ConditionStructureValue condition = conditionValueList[i];
                condition.mutate();
            }
        }

        public void newValues(){

                Random random = new Random();

                conditionType = ConditionTypes.values()[random.nextInt(ConditionTypes.values().length)];

                for(int i=0; i < arity; i++){

                        ConditionStructureValue condition = new ConditionStructureValue(depth + "" + i);
                        condition.newValues();

                        conditionValueList[i]= condition;

                        if(i%2 == 0){
                                connectorList[i]= ConditionConnectorType.values()[random.nextInt(1)];
                        }
                }
        }

        public String generate(){

                conditionString = new StringBuilder(conditionType.getDescription() + "(");

                for(int i=0; i < arity; i++){

                        conditionString.append(conditionValueList[i].generate() + " ");

                        if(i!=0 && i%2 == 0){
                                conditionString.append(connectorList[i] + " ");
                        }
                }

                conditionString.append(") { System.out.println(\"1\"); #1_0 } else { System.out.println(\"1\"); #1_1}");

                return conditionString.toString();

        }

        public String generate(String id1, String id2){

            conditionString = new StringBuilder(conditionType.getDescription() + "(");

            for(int i=0; i < arity; i++){

                    conditionString.append(conditionValueList[i].generate() + " ");

                    if(i!=0 && i%2 == 0){
                            conditionString.append(connectorList[i] + " ");
                    }
            }

            conditionString.append(") { System.out.println(\"1\"); " + id1 +" } else { System.out.println(\"1\"); " + id2 +" }");
            return conditionString.toString();

        }

        public ConditionTypes getConditionType() {
                return conditionType;
        }

        public ConditionConnectorType[] getConnectorList() {
                return connectorList;
        }
}
