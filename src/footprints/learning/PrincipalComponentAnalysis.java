package footprints.learning;
import marytts.util.math.MathUtils;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import footprints.individual.Individual;

public class PrincipalComponentAnalysis{
	
    public void eigenPCA(Individual ind, Matrix eigenVector, boolean scale, int componentsNumber){
	if(eigenVector == null){
            eigenPCA(ind, scale);
	}else{
            eigenPCA(ind, eigenVector, scale);
	}	
        double[][] components = ind.getFullCompsArray();
        double[][] dataArray = new double[componentsNumber][components[0].length];

        for(int i=0; i < dataArray[0].length; i++){
            for(int k = 0; k < componentsNumber; k++){
		dataArray[k][i] = components[k][i];
            }
	}
	ind.setRedComponentsArray(dataArray);
		
    }
    public void eigenPCA(Individual ind, boolean scale){
	    	
        Matrix mData=ind.getData();
	int M = mData.getRowDimension();
	int N = mData.getColumnDimension();
	    
	//  substract the mean for each dimension
	// if applying zscore scaling then divide by the standard deviation
	// double element[][] = data.getArrayCopy();
        for(int i=0; i<M; i++){
          double mn = MathUtils.mean(mData.getArray()[i]);
          if(mn == 0.0)
            throw new Error("eigenPCA: mean of dimension " + (i+1) + " is 0.0");
          if(scale){
            double sd = MathUtils.standardDeviation(mData.getArray()[i]);
            if(sd == 0.0)
              throw new Error("eigenPCA: variance of dimension " + (i+1) + " is 0.0");
            // divide by the standard deviation
            for(int j=0; j<N; j++)
              mData.set(i, j, ( (mData.get(i, j)-mn)/sd ));
          } else {
            // remove the mean
            for(int j=0; j<N; j++)
              mData.set(i, j, (mData.get(i, j)-mn));
          }

        }
	     
        // calculate the covariance matrix
        // covariance = 1/(N-1) * data * data'
        Matrix covariance = mData.times(mData.transpose());
        covariance = covariance.times(1.0/(N-1));

        // find the eigenvectors and eigenvalues
        // eig() returns the values not ordered
        EigenvalueDecomposition pc = covariance.eig();
        // get the diagonal values and sort them
        double values[] = new double[pc.getD().getRowDimension()];
        for(int i=0; i<pc.getD().getRowDimension(); i++){
           values[i] = pc.getD().get(i, i);
        }

        // sort is from lowest to highest
        int indices[] = MathUtils.quickSort(values);
        double [] V = new double[values.length];

        // sort the variances in decreasing order
        double d[][] = new double[pc.getV().getRowDimension()][pc.getV().getColumnDimension()];
        for(int j=0; j<values.length; j++){  
          int k = indices[values.length-1-j];
          V[j] = values[k];
          for(int i=0; i<pc.getV().getRowDimension(); i++)
            d[i][j] = pc.getV().get(i, k);
        }        

        Matrix PC = new Matrix(d);

        // project the original data
        // signals = PC' * data
        Matrix projectedData = PC.transpose().times(mData);    

        ind.setEigenVectors(PC);
        ind.setFullCompsArray(projectedData.getArray());
        if(values.length>2){
            ind.setFitness(((values[values.length - 1] + values[values.length - 2]) * 100) / MathUtils.sum(values));
        }
        else{
            ind.setFitness(values[values.length - 1] *100/ MathUtils.sum(values));
        }
// The variance for each principal component can be read off the diagonal of the covariance matrix
        // of projected_data
        //Matrix covProjectedData = projectedData.times(projectedData.transpose());
      }
	
    public void eigenPCA(Individual ind, Matrix eigenVectors, boolean scale){
	    int M = ind.getData().getRowDimension();
	    int N = ind.getData().getColumnDimension();
	    
	    //  substract the mean for each dimension
	    // if applying zscore scaling then divide by the standard deviation
	   // double element[][] = data.getArrayCopy();
	    double mn;
	    double sd;
	    for(int i=0; i<M; i++){
	      mn = MathUtils.mean(ind.getData().getArray()[i]);
	      if(mn == 0.0)
	        throw new Error("eigenPCA: mean of dimension " + (i+1) + " is 0.0");
	      if(scale){
	        sd = MathUtils.standardDeviation(ind.getData().getArray()[i]);
	        if(sd == 0.0)
	          throw new Error("eigenPCA: variance of dimension " + (i+1) + " is 0.0");
	        // divide by the standard deviation
	        for(int j=0; j<N; j++)
	          ind.getData().set(i, j, ( (ind.getData().get(i, j)-mn)/sd ));
	      } else {
	        // remove the mean
	        for(int j=0; j<N; j++)
	          ind.getData().set(i, j, (ind.getData().get(i, j)-mn));
	      }
	          
	    }
	     
	    // calculate the covariance matrix
	    // covariance = 1/(N-1) * data * data'
	    Matrix covariance = ind.getData().times(ind.getData().transpose());
	    covariance = covariance.times(1.0/(N-1));
	    
	    // find the eigenvectors and eigenvalues
	    // eig() returns the values not ordered
	    EigenvalueDecomposition pc = covariance.eig();
	    // get the diagonal values and sort them
	    double values[] = new double[pc.getD().getRowDimension()];
	    for(int i=0; i<pc.getD().getRowDimension(); i++){
	       values[i] = pc.getD().get(i, i);
	    }
	    
	    // sort is from lowest to highest
	    int indices[] = MathUtils.quickSort(values);
	    double [] V = new double[values.length];
	    
	    // sort the variances in decreasing order
	    double d[][] = new double[pc.getV().getRowDimension()][pc.getV().getColumnDimension()];
	    for(int j=0; j<values.length; j++){  
	      int k = indices[values.length-1-j];
	      V[j] = values[k];
	      for(int i=0; i<pc.getV().getRowDimension(); i++)
	        d[i][j] = pc.getV().get(i, k);
	    }        
	  
	    
	    Matrix PC = new Matrix(d);
	    // project the original data
	    // signals = PC' * data
	    Matrix projectedData = eigenVectors.transpose().times(ind.getData());    
	    
	    ind.setEigenVectors(PC);
	    ind.setFullCompsArray(projectedData.uminus().getArray());
	    ind.setFitness(((values[values.length - 1] + values[values.length - 2]) * 100) / MathUtils.sum(values));
	    
	    
	    // The variance for each principal component can be read off the diagonal of the covariance matrix
	    // of projected_data
	    //Matrix covProjectedData = projectedData.times(projectedData.transpose());
	    
	  }
}
