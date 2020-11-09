package footprints.fitnessEvaluator.footprint;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Vector2DMF {

	private Integer id;
	
	private String name;
	
	private Vector2D coordinates;
	
	public Vector2DMF(Integer id, String name, Vector2D coordinates) {
		super();
		this.id = id;
		this.name = name;
		this.coordinates = coordinates;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Vector2D getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Vector2D coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector2DMF other = (Vector2DMF) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}