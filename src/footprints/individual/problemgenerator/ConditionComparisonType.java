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
public enum ConditionComparisonType{
		
		EQUAL(1L, "Equal"),
		
		NOT_EQUAL(2L, "Not equal"),
		
		LESS_THAN(3L, "Less than"),
		
		LESS_EQUAL_THAN(4L, "Less or Equal than"),
		
		HIGHER_THAN(5L, "Higher than"),
		
		HIGHER_EQUAL_THAN(6L, "Higher or Equal than");
		
		private final Long value;

		private final String description;

		private ConditionComparisonType(Long value, String description) {
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
