/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package footprints.fitnessEvaluator.footprint;

import java.io.Serializable;
import org.apache.commons.math3.ml.clustering.Clusterable;

/**
 *
 * @author aldeidaaleti
 */
  public class ClusterableNode implements Clusterable, Serializable {
	
	private double x;
	
	private double y;
	
	private String label;
	
	public ClusterableNode(double x, double y, String label) {
		super();
		this.x = x;
		this.y = y;
		this.label = label;
	}

	public double[] getPoint() {
		// TODO Auto-generated method stub
		return new double[]{x, y};
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
  }