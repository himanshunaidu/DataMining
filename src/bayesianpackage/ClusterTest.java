package bayesianpackage;

import java.util.*;

public class ClusterTest {
	
	/*public static void main(String[] args){
		Random rand = new Random();
		double[] array = new double[100];
		for(int i=0; i<100; i++){
			array[i] = 25*rand.nextDouble();
			System.out.print(array[i]+" ");
		}
		System.out.println("");
		KMeans k = new KMeans(array);
		k.clustering(10, 100, null);
		double[] centroids = k.getCentroids();
		for(int i=0; i<centroids.length; i++){
			System.out.println(centroids[i]+" ");
		}
	}*/
	
	public static void main(String[] args){
		Parameters param = new Parameters();
		DocSpec[][] dist = param.dist;
		/*for(int i=0; i<dist.length; i++){
			for(int j=0; j<dist[i].length; j++){
				System.out.print(dist[i][j].getSim()+"("+dist[i][j].getSpec()+") ");
			}
			System.out.println("");
		}*/
		HashMap<Integer, Double> fraud = param.fraud;
		Set<Integer> docs = fraud.keySet();
		Iterator<Integer> it = docs.iterator();
		while(it.hasNext()){
			int d = it.next();
			System.out.println(d + " :Probability of Failure = "+fraud.get(d)+"%");
		}
	}

}
