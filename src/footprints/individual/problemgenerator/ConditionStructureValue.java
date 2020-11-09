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
public class ConditionStructureValue implements Serializable{
		
        private String coditionId;
        private ConditionValueType[] valueTypeList;	
        private ConditionComparisonType[] comparisonTypeList;
        private ConditionValue[] definedValues;
        private StringBuilder conditionString;

        @Override
        public ConditionStructureValue clone(){
            ConditionStructureValue newCSV = new ConditionStructureValue(coditionId);
            newCSV.coditionId=coditionId;
            newCSV.conditionString= new StringBuilder(conditionString);

            
            return newCSV;
        }
        
        public ConditionValueType[] getValueTypeList(){
            return valueTypeList;
        }
        public ConditionStructureValue(String coditionId) {
                super();
                this.coditionId = coditionId;
        }

        public void mutate(){

                ConditionValueType type1 = ConditionValueType.values()[new Random().nextInt(ConditionValueType.values().length)];
                valueTypeList[0]= type1;

                ConditionValue conditionValue = new ConditionValue(type1);
                conditionValue.newValue();

                definedValues[0]= conditionValue;

                ConditionComparisonType comparator = null;

                if(type1 == ConditionValueType.BOOLEAN){
                        comparator = ConditionComparisonType.values()[new Random().nextInt(1)];
                }else{
                        comparator = ConditionComparisonType.values()[new Random().nextInt(ConditionComparisonType.values().length)];
                }

                comparisonTypeList[0]= comparator;
        }

        public void newValues(){

                ConditionValueType type1 = ConditionValueType.values()[new Random().nextInt(ConditionValueType.values().length)];

                valueTypeList[0]= type1;

                ConditionValue conditionValue = new ConditionValue(type1);
                conditionValue.newValue();

                definedValues[0]= conditionValue;

                ConditionComparisonType comparator = null;

                if(type1 == ConditionValueType.BOOLEAN){
                        comparator = ConditionComparisonType.values()[new Random().nextInt(1)];
                }else{
                        comparator = ConditionComparisonType.values()[new Random().nextInt(ConditionComparisonType.values().length)];
                }


                comparisonTypeList[0]= comparator;

        }

        public String generate(){

                conditionString = new StringBuilder();

                conditionString.append("( number" + coditionId + " " + generateComparator(comparisonTypeList[0]) + " " + definedValues[0].getValue() + ") \n");

                return conditionString.toString();
        }



        private String generateComparator(ConditionComparisonType comparisonType){

            switch(comparisonType){
                case EQUAL: return "==";
                case NOT_EQUAL: return "!=";
                case LESS_THAN: return "<";
                case LESS_EQUAL_THAN: return "<=";
                case HIGHER_THAN: return ">";
                case HIGHER_EQUAL_THAN: return ">=";
            }

            return null;
        }

}
