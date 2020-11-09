/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package footprints.individual.problemgenerator;

/**
 *
 * @author aldeidaaleti
 */
public enum ConditionTypes{
		
        IF(1L, "if");

        //IF_ELSE(2L, "IF and ELSE"),

        //IF_ELSE_IF(3L, "IF and cascade ELSE");

        private final Long value;

        private final String description;

        private ConditionTypes(Long value, String description) {
                this.value = value;
                this.description = description;
        }

        public Long getValue() {
                return value;
        }

        public String getDescription() {
                return description;
        }
}
