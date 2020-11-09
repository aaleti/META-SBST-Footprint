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
public enum ConditionConnectorType{
		
        AND(1L, "&&"),

        OR(2L, "||");

        private final Long value;

        private final String description;

        private ConditionConnectorType(Long value, String description) {
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
