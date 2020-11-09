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
public class ConditionValue implements Serializable{

            private ConditionValueType type;
            private Double doubleValue;
            private Integer integerValue;
            private Boolean booleanValue;
            private Long longValue;

            public ConditionValue(ConditionValueType type) {
                    super();
                    this.type = type;
            }	

            public ConditionValue(ConditionValueType type, String value) {
                    super();
                    this.type = type;

                    switch(type){
                            case BOOLEAN:
                                    this.booleanValue = Boolean.parseBoolean(value);
                                    break;
                            case DOUBLE:
                                    this.doubleValue = Double.parseDouble(value);
                                    break;
                            case LONG:
                                    this.longValue = Long.parseLong(value);
                                    break;
                            case INTEGER:
                                    this.integerValue = Integer.parseInt(value);
                                    break;
                    }
            }

            public String getValue(){
                    switch(type){
                            case BOOLEAN:
                                    return String.valueOf(this.booleanValue);
                            case DOUBLE:
                                    return String.valueOf(this.doubleValue) + "D";
                            case LONG:
                                    return String.valueOf(this.longValue) + "L";
                            case INTEGER:
                                    return String.valueOf(this.integerValue);
                    }

                    return null;
            }

            public void newValue (){

                    Random random = new Random();

                    switch(type){
                            case BOOLEAN:
                                    this.booleanValue = random.nextBoolean();
                                    break;
                            case DOUBLE:
                                    this.doubleValue = Double.MAX_VALUE * random.nextDouble();
                                    break;
                            case LONG:
                                    this.longValue = random.nextLong();
                                    break;
                            case INTEGER:
                                    this.integerValue = random.nextInt(Integer.MAX_VALUE);
                                    break;
                    }
            }

    }