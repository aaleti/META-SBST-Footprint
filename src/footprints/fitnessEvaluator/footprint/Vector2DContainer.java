package footprints.fitnessEvaluator.footprint;

public class Vector2DContainer implements Comparable<Vector2DContainer> {

	private Vector2DMF node;
	
	private Double distance;
	
	public Vector2DContainer(Vector2DMF node, double distance) {
		super();
		this.node = node;
		this.distance = distance;
	}

	public Vector2DMF getNode() {
		return node;
	}

	public void setNode(Vector2DMF node) {
		this.node = node;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public int compareTo(Vector2DContainer o) {
		return this.getDistance().compareTo(o.getDistance());
	}
	
}