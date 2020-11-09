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
public enum ConditionValueType {
		
		INTEGER(1L, "Integer"),
		
		LONG(2L, "Long"),
		
		DOUBLE(3L, "Double"),
		
		BOOLEAN(4L, "Boolean");
		
		private final Long value;

		private final String description;

		private ConditionValueType(Long value, String description) {
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
	
