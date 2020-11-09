package footprints.fitnessEvaluator.footprint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import footprints.IO.InputData;
import footprints.fitnessEvaluator.FitnessEvaluatorPCA;
import footprints.IO.Plotting;
import footprints.individual.Individual;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.euclidean.twod.hull.MonotoneChain;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.jfree.data.xy.DefaultXYDataset;

public class FitnessEvaluatorFootprint extends FitnessEvaluatorPCA {

	private HashMap<Vector2DMF, List<Vector2DContainer>> distanceHash;
	private List<ClusterableNode> footprintClusterable;
	

    public FitnessEvaluatorFootprint(InputData dataInfoDTO) {
        super(dataInfoDTO);
    }

        

    @Override
    public void evaluate(Individual ind){

        HashMap<String, List<Vector2DMF>> hashVector2DList = createHashVector2DList(ind.getRedComponentsArray(), 
				getInputData().getUniqueLabels(), 
				getInputData().getLabelIntegerArray());
		
		HashMap<String, List<ClusterableNode>> hashClusterableNode = createHashClusterable(ind.getRedComponentsArray(), 
				getInputData().getUniqueLabels(), 
				getInputData().getLabelIntegerArray());
		
		List<Vector2DMF> nodesEasy = new ArrayList<>();
                String[] methods = getInputData().getUniqueLabels();
                for(int i=0; i<methods.length;i++){
                    nodesEasy.addAll(hashVector2DList.get(methods[i]));
                }
		
                double [][] footprintArray = calculateTriangleAproach(hashVector2DList.get("All"), nodesEasy);
		//double [][] footprintArray = footprintService.calculateClosestNodes(hashVector2DList.get("All"), hashVector2DList.get("SUITE"), hashVector2DList.get("RANDOM"));
	    
	    /*
		double [][] footprintArray = footprintService.calculateDBCluster(hashClusterableNode.get("RANDOM"));
		
		Polygon2D.Double polygon = footprintService.getPolygonHullPoints(footprintArray);
		HashMap<String, List<double[]>> footPrintArea = new HashMap<String, List<double[]>>();
		footPrintArea.put("Others", new ArrayList<double[]>());
		footPrintArea.put("Footprint", new ArrayList<double[]>());
		
		for(int i=0; i < dataServiceDTO.getPrincipalComponentAnalysisDTO().getReducedComponentArray()[0].length; i++){
			if(polygon.contains(dataServiceDTO.getPrincipalComponentAnalysisDTO().getReducedComponentArray()[0][i], dataServiceDTO.getPrincipalComponentAnalysisDTO().getReducedComponentArray()[1][i])){
				footPrintArea.get("Footprint").add(new double[]{dataServiceDTO.getPrincipalComponentAnalysisDTO().getReducedComponentArray()[0][i],dataServiceDTO.getPrincipalComponentAnalysisDTO().getReducedComponentArray()[1][i]});
			}else{
				footPrintArea.get("Others").add(new double[]{dataServiceDTO.getPrincipalComponentAnalysisDTO().getReducedComponentArray()[0][i],dataServiceDTO.getPrincipalComponentAnalysisDTO().getReducedComponentArray()[1][i]});
			}
		}
		
		double [][] areaFootPrint = footprintService.listToArray(footPrintArea.get("Footprint"), "Footprint");
		double [][] othersArea = footprintService.listToArray(footPrintArea.get("Others"), "Others");
		
	   */
		
            HashMap<String, double[][]> hashSeries = new HashMap<String, double[][]>();
	    
	    hashSeries.put("BFootprint", footprintArray);
	    hashSeries.put("AOthers", ind.getRedComponentsArray());
            
            DefaultXYDataset dataset = new DefaultXYDataset();
            dataset.addSeries("BFootprint", footprintArray);
            dataset.addSeries("AOthers", ind.getRedComponentsArray());
	    
	    Plotting plot=new Plotting(dataset, "PC1", "PC2");
            plot.pack();
            plot.setVisible(true);
	}

	public double[][] calculateTriangleAproach(List<Vector2DMF> nodesMF, List<Vector2DMF> nodesEasyMF){
		List<Vector2DMF> nodesEasyBackupMF = new ArrayList<>();
		nodesEasyBackupMF.addAll(nodesEasyMF);
	
		
		double [][] distanceMatrix = new double[nodesMF.size()][nodesMF.size()];
		distanceHash = new HashMap<Vector2DMF, List<Vector2DContainer>>();
		for(int i = 0; i < nodesMF.size(); i++){
			Vector2DMF node = nodesMF.get(i);
			if(distanceHash.containsKey(node) == false){
				distanceHash.put(node, new ArrayList<Vector2DContainer>());
			}
			for(int p = 0; p < nodesMF.size(); p++){
				Vector2DMF node2 = nodesMF.get(p);
				if(node.equals(node2) == false){
					distanceMatrix[i][p] = node.getCoordinates().distance(node2.getCoordinates());
					distanceHash.get(node).add(new Vector2DContainer(node2, distanceMatrix[i][p]));
				}	
			}
		}
		
		
		List<List<Vector2DMF>> listPart1 = new ArrayList<List<Vector2DMF>>();
		
		int goodNodesSelected = 0;
		while(nodesEasyBackupMF.size() > goodNodesSelected){
			Vector2DMF selectedGoodNode = null;
			while(selectedGoodNode == null){
				selectedGoodNode = nodesEasyMF.get(new Random().nextInt(nodesEasyMF.size()));
			}
			
			goodNodesSelected++;
			nodesMF.remove(selectedGoodNode);
			nodesEasyMF.remove(selectedGoodNode);
			
			List<Vector2DContainer> distanceTable = distanceHash.get(selectedGoodNode);
			Collections.sort(distanceTable);
			
			List<Vector2DMF> triangle = new ArrayList<Vector2DMF>();
			triangle.add(selectedGoodNode);
			
			Vector2DMF closestNode1 = null;
			for(Vector2DContainer vectorContainer : distanceTable){
				if(nodesMF.contains(vectorContainer.getNode())){
					closestNode1 = vectorContainer.getNode();
					break;
				}
			}
			
			Vector2DMF closestNode2 = null;
			for(Vector2DContainer vectorContainer : distanceTable){
				if(nodesMF.contains(vectorContainer.getNode())){
					closestNode2 = vectorContainer.getNode();
					break;
				}
			}
			
			if(nodesEasyBackupMF.contains(closestNode1)){
				goodNodesSelected++;
			}
			
			if(nodesEasyBackupMF.contains(closestNode2)){
				goodNodesSelected++;
			}
			
			triangle.add(closestNode1);
			triangle.add(closestNode2);
			
			nodesMF.remove(closestNode1);
			nodesMF.remove(closestNode2);
			
			nodesEasyMF.remove(closestNode1);
			nodesEasyMF.remove(closestNode2);
			
			listPart1.add(triangle);
		}	
		
		
		int triesWithNoMerge = 0;
		boolean merge = false;
		while(triesWithNoMerge < 30){
			for(int i=0; i < listPart1.size(); i++){
				List<Vector2DMF> triangle = listPart1.get(i);
				
				List<Vector2DMF> closestTriangle = null;
				Double closestDistance = null;
				
				for(List<Vector2DMF> triangle2 : listPart1){
					if(triangle.equals(triangle2) == false){
						double distance = centroid(triangle).distance(centroid(triangle2));
						if(closestTriangle == null || distance < closestDistance){
							closestTriangle = triangle2;
							closestDistance = distance;
						}
					}
				}
				
				if(closestTriangle != null && pure(triangle, closestTriangle, nodesEasyBackupMF)){
					triangle.addAll(closestTriangle);
					listPart1.remove(closestTriangle);
					merge = true;
				}
				
			}
			
			if(merge == false){
				triesWithNoMerge++;
			}
			
			merge = false;
		}	
		
		
		
		/*for(List<Vector2D> partialArea : listPart1){
			
			Vector2D closestNode = null;
			Double closestDistance = null;
			
			for(Vector2D node : nodesAll){
				
				double distance = MatildaUtils.centroid(partialArea).distance(node);
				if(closestNode == null || distance < closestDistance){
					closestNode = node;
					closestDistance = distance;
				}
				
			}
			
			List<Vector2D> newList = new ArrayList<Vector2D>();
			newList.add(node);
			
			
		}*/
		
		
		ArrayList<Vector2DMF> footprintList = new ArrayList<Vector2DMF>();
		int count = 1;
		for(List<Vector2DMF> list : listPart1){
			if(list.size() < 6){
				continue;
			}
			//System.out.println("Cluster " + count++ + ": " + list.size());
			for(Vector2DMF node : list){
				footprintList.add(node);
			}
		}
		
		//footprintSize = footprintList.size();
		
		double[][] footprintArray = new double[2][footprintList.size()];
		for(int i=0; i < footprintList.size(); i++){
			Vector2DMF node = footprintList.get(i);
			footprintArray[0][i] = node.getCoordinates().getX();
			footprintArray[1][i] = node.getCoordinates().getY();
		}
		
		return footprintArray;
	}
	
	/*public double[][] calculateClosestNodes(List<Vector2D> nodes, List<Vector2D> nodesEasy, List<Vector2D> nodesHard){
		
		double [][] distanceMatrix = new double[nodes.size()][nodes.size()];
		distanceHash = new HashMap<Vector2D, List<Vector2DContainer>>();
		for(int i = 0; i < nodes.size(); i++){
			Vector2D node = nodes.get(i);
			if(distanceHash.containsKey(node) == false){
				distanceHash.put(node, new ArrayList<Vector2DContainer>());
			}
			for(int p = 0; p < nodes.size(); p++){
				Vector2D node2 = nodes.get(p);
				if(node.equals(node2) == false){
					distanceMatrix[i][p] = node.distance(node2);
					distanceHash.get(node).add(new Vector2DContainer(node2, distanceMatrix[i][p]));
				}	
			}
		}
		
		
		Vector2D centroidHard = centroid(nodesHard);
	
		
		Vector2D fartestNode = null;
		Double fartestDistance = null; 
		for(Vector2D node : nodesEasy){
			double distance = centroidHard.distance(centroidHard);
			if(fartestNode == null || fartestDistance < distance){
				fartestNode = node;
				fartestDistance = distance;
			}
		}
		
		List<List<Vector2D>> list = new ArrayList<List<Vector2D>>();
		
		footprintNodes = new ArrayList<Vector2D>();
		
		findFootprint(fartestNode, nodesEasy, nodesHard);
		
		footprintSize = footprintNodes.size();
		
		double[][] footprintArray = new double[2][footprintNodes.size()];
		for(int i=0; i < footprintNodes.size(); i++){
			Vector2D node = footprintNodes.get(i);
			footprintArray[0][i] = node.getX();
			footprintArray[1][i] = node.getY();
		}
		
		return footprintArray;
	}
	*/
	public double[][] calculateDBCluster(List<ClusterableNode> nodesEasy){
		//footprintSize = 0;
		footprintClusterable = new ArrayList<ClusterableNode>();
		DBSCANClusterer<ClusterableNode> dbScanCluster = new DBSCANClusterer<ClusterableNode>(.6, 20);
		List<Cluster<ClusterableNode>> clustersList = dbScanCluster.cluster(nodesEasy);
		for(Cluster<ClusterableNode> cluster : clustersList){
			footprintClusterable.addAll(cluster.getPoints());
			//footprintSize += cluster.getPoints().size();
		}
		
		double[][] footprintArray = new double[2][footprintClusterable.size()];
		for(int i=0; i < footprintClusterable.size(); i++){
			ClusterableNode node = footprintClusterable.get(i);
			footprintArray[0][i] = node.getX();
			footprintArray[1][i] = node.getY();
		}
		
		return footprintArray;
	}
	/*
	private void findFootprint(Vector2D node, List<Vector2D> nodesEasy, List<Vector2D> nodesHard){
		
		footprintNodes.add(node);
		
		List<Vector2DContainer> distanceTable = distanceHash.get(node);
		Collections.sort(distanceTable);
		
		for(Vector2DContainer vectorContainer : distanceTable){
			if(footprintNodes.contains(vectorContainer.getNode()) == false){
				if(nodesEasy.contains(vectorContainer.getNode())){
					findFootprint(vectorContainer.getNode(), nodesEasy, nodesHard);
				}else{
					List<Vector2D> potentialNode = new ArrayList<Vector2D>();
					potentialNode.add(vectorContainer.getNode());
					if(purity(footprintNodes, potentialNode, nodesEasy) > 0.95){
						findFootprint(vectorContainer.getNode(), nodesEasy, nodesHard);
					}
				}
				break;
			}
		}
	}
	*/
	
	private static double purity(List<Vector2DMF> list1, List<Vector2DMF> list2, List<Vector2DMF> easyInstances){
		
		List<Vector2DMF> newList = new ArrayList<Vector2DMF>();
		newList.addAll(list1);
		newList.addAll(list2);
		
		double countGood = 0;
		for(Vector2DMF node : newList){
			if(easyInstances.contains(node)){
				countGood++;
			}
		}
		
		return countGood / newList.size();
		
	}
	
	private static boolean pure(List<Vector2DMF> list1, List<Vector2DMF> list2, List<Vector2DMF> easyInstances){
		
		List<Vector2DMF> newList = new ArrayList<Vector2DMF>();
		newList.addAll(list1);
		newList.addAll(list2);
		
		double countGood = 0;
		for(Vector2DMF node : newList){
			if(easyInstances.contains(node)){
				countGood++;
			}
		}
		
		if(countGood < newList.size()){
			System.out.println("");
		}
		
		return countGood == newList.size();
		
	}
	
	private static double density(List<Vector2D> list1, List<Vector2D> list2){
		
		List<Vector2D> newList = new ArrayList<>();
		newList.addAll(list1);
		newList.addAll(list2);
		
		MonotoneChain monotoneChain = new MonotoneChain();
		
		List<Vector2D> hullList = new ArrayList(monotoneChain.findHullVertices(newList));
		
		double density = (list1.size() + list2.size()) / calculateArea(hullList, hullList.size());
		
		return density;
		
	}
	
	/*private static Polygon2D.Double getPolygonHullPoints(double [][] footprint){
		
		List<Vector2D> nodes = new ArrayList<Vector2D>();
		for(int i=0; i < footprint[0].length; i++){
			nodes.add(new Vector2D(footprint[0][i],footprint[1][i]));
		}
		
		MonotoneChain monotoneChain = new MonotoneChain();
		
		List<Vector2D> hullList = new ArrayList(monotoneChain.findHullVertices(nodes));
		Polygon2D.Double polygon = new Polygon2D.Double();
		for(Vector2D coordinates : hullList){
			polygon.lineTo(coordinates.getX(), coordinates.getY());
		}
		
		polygon.closePath();
		
		return polygon;
	}*/
	
	private static double calculateArea(List<Vector2D> nodesList, int n) {

		int i, j;
		double area = 0;

		for (i = 0; i < n; i++) {
			j = (i + 1) % n;
			area += nodesList.get(i).getX() * nodesList.get(j).getY();
			area -= nodesList.get(i).getY() * nodesList.get(j).getX();
		}

		area /= 2.0;
		return (Math.abs(area));
	}
	
	
	public static synchronized HashMap<String, List<Vector2DMF>> createHashVector2DList(double [][] pcaComponents, String [] labelString, int [] labelInteger){
		
		
		HashMap<String, List<Vector2DMF>> hashNodeList = new HashMap<String, List<Vector2DMF>>();
		
		hashNodeList.put("All", new ArrayList<Vector2DMF>());
		
		for(int p=0; p < labelString.length; p++){
			hashNodeList.put(labelString[p], new ArrayList<Vector2DMF>());
		}
		int idGenerator = 0;
		for(int i =0; i < pcaComponents[0].length; i++){
			
			for(int p=0; p < labelString.length; p++){
				if(labelInteger[i] == p){
					Vector2D node = new Vector2D(new double[]{pcaComponents[0][i], pcaComponents[1][i]});
					hashNodeList.get("All").add(new Vector2DMF(idGenerator, labelString[p], node));
					hashNodeList.get(labelString[p]).add(new Vector2DMF(idGenerator, labelString[p], node));
					idGenerator++;
				}
			}
		}	
		
		return hashNodeList;
				
	}
	
	public synchronized HashMap<String, List<ClusterableNode>> createHashClusterable(double [][] pcaComponents, String [] labelString, int [] labelInteger){
		
		
		HashMap<String, List<ClusterableNode>> hashNodeList = new HashMap<String, List<ClusterableNode>>();
		
		hashNodeList.put("All", new ArrayList<ClusterableNode>());
		
		for(int p=0; p < labelString.length; p++){
			hashNodeList.put(labelString[p], new ArrayList<ClusterableNode>());
		}
		
		for(int i =0; i < pcaComponents[0].length; i++){
			for(int p=0; p < labelString.length; p++){
				if(labelInteger[i] == p){
					Vector2D node = new Vector2D(new double[]{pcaComponents[0][i], pcaComponents[1][i]});
					hashNodeList.get("All").add(new ClusterableNode(node.getX(), node.getY(), "All"));
					hashNodeList.get(labelString[p]).add(new ClusterableNode(node.getX(), node.getY(), labelString[p]));
				}
			}
		}	
		
		return hashNodeList;
				
	}
	
	public static Vector2D centroid(List<Vector2DMF> nodeList)  {
	    double centroidX = 0, centroidY = 0;

	        for(Vector2DMF node : nodeList) {
	            centroidX += node.getCoordinates().getX();
	            centroidY += node.getCoordinates().getY();
	        }
	    return new Vector2D(centroidX / nodeList.size(), centroidY / nodeList.size());
	}
	
	public double [][] listToArray(List<double[]> values, String label){
		double[][] array = new double[2][values.size()];
		for(int i=0; i < values.size(); i ++){
			array[0][i] = values.get(i)[0];
			array[1][i] = values.get(i)[1];
			
			System.out.println(array[0][i] + "," + array[1][i] + "," + label);
		}
		
		return array;
	}

}
