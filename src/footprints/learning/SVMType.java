package footprints.learning;

public enum SVMType{
	
	GRID_MODE(1L, "Grid Mode"),
	
	EASY_MODE(2L, "Easy Mode"),
	
	FAST_MODE(3L, "Fast Mode");
	
	private final Long value;

	private final String description;

	private SVMType(Long value, String description) {
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
