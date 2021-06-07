package bayesianpackage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.swing.JOptionPane;

import graphpackage.*;
import storagepackage.*;

public class Parameters {
	
	public GraphGenerate gg = new GraphGenerate();
	public MedicareRetriever mr = gg.getMR();
	public HashMap<Integer, HashSet<Integer>> doctors = gg.getDoctors();
	public HashMap<Integer, String> doctors2 = gg.getDoctors2();
	public HashSet<String> specs = gg.getSpecs();
	public HashSet<Integer> hcps = gg.getHcps();
	
	public HashMap<String, HashSet<Integer>> docluster;
	public HashMap<Double, HashSet<Integer>> hcpcluster;
	
	public int kprov = 0;
	public int khcps = 0;
	public int prov = 0;
	public int hcp = 0;
	
	public int[][] xij;
	public int[][][] z1ijk;
	public int[][][] z2ijl;
	public DocSpec[][] dist;
	public DocSpec[][] sortdist;
	
	public double[] pi1;
	public double[] pi2;
	public double theta = 0;
	public double alpha = 0;
	public double beta = 0;
	public double alpha1 = 0;
	public double alpha2 = 0;
	
	public HashSet<Double> hcpcentroids;
	public HashMap<Integer, Double> fraud;
	
	public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/data_mining?useSSL=false";
	
    private boolean connected = false;
    
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement pstatement1 = null;
    private PreparedStatement pstatement2 = null;
    private PreparedStatement pstatement3 = null;
    private ResultSet resultset = null;
    private ResultSetMetaData metadata = null;
    
	
	public Parameters(){
		MedicareRetriever mr = gg.getMR();
		HashMap<Integer, HashSet<Integer>> doctors = gg.getDoctors();
		HashSet<String> specs = gg.getSpecs();
		HashSet<Integer> hcps = gg.getHcps();
		prov = doctors.size();
		hcp = hcps.size();
		kprov = specs.size();
		
		hcpcentroids = getClusters(hcps);
		khcps = hcpcentroids.size();
		
		pi1 = new double[kprov];
		pi2 = new double[khcps];
		
		xij = new int[prov][hcp];
		
		getDocClusters();
		getHcpClusters(hcpcentroids);
		
		connectDB();
		getXij();
		getPossibleFraud();
		getPossibleFraud2();
		getFraudStatus();
		
		//DocStore();
		//HcpStore();
		closeDB();
	}
	
	//PART 2 OF PARAMETERS CODE
	private void getFraudStatus(){
		Random rand = new Random();
		fraud = new HashMap<Integer, Double>();
		double prob = 0.0, prob1;
		int i=0, j=0, length = sortdist.length;
		
		Set<Integer> docs = doctors2.keySet();
		Iterator<Integer> it1 = docs.iterator();
		for(i=0; i<length; i++){
			prob = 0;
			int doc = it1.next();
			for(j=0; j<10; j++){
				prob+= (doctors2.get(doc)!=sortdist[i][j].getSpec())?1:0;
			}
			prob1 = rand.nextDouble()*(prob-3)*10;
			fraud.put(doc, prob1);
			try {
				pstatement3.setInt(1, doc);
				pstatement3.setDouble(2,  prob1);
				int result = pstatement3.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	private void getPossibleFraud2(){
		sortdist = new DocSpec[dist.length][dist[0].length];
		int length=dist.length, i=0, j=0;
		DocSpec[] darray;
		
		Set<Integer> docs = doctors2.keySet();
		Iterator<Integer> it1 = docs.iterator();
		
		int k=0;
		for(i=0; i<length; i++){
			int doc = it1.next();
			darray = dist[i];
			int length1 = darray.length;
			//darray = selectionSort(darray);
			/*System.out.print(doc+" ("+doctors2.get(doc)+"): ");
			for(j=0; j<10; j++){
				System.out.print(darray[j].getSim()+"("+darray[j].getSpec()+") ");
			}
			System.out.println("");*/
			sortdist[k] = darray;
			k++;
		}
	}
	
	public DocSpec[] selectionSort(DocSpec[] arr){  
        for (int i = 0; i < arr.length - 1; i++)  
        {  
            int index = i;  
            for (int j = i + 1; j < arr.length; j++){  
                if (arr[j].getSim() > arr[index].getSim()){  
                    index = j;//searching for lowest index  
                }  
            }  
            DocSpec smallerNumber = arr[index];   
            arr[index] = arr[i];  
            arr[i] = smallerNumber;  
        }
        return arr;
	} 
	
	private void getPossibleFraud(){
		int length = xij.length, length1 = xij[0].length, i=0, j=0;
		int[] xarray, xarray2;
		System.out.println(length);
		dist = new DocSpec[length][length];
		double dist1;
		
		Set<Integer> docs = doctors2.keySet();
		Iterator<Integer> it1 = docs.iterator();
		Iterator<Integer> it2;
		while(it1.hasNext()&&(i<length)){
			xarray = xij[i];
			j=0;
			it2 = docs.iterator();
			for(j=0; j<length; j++){
				dist1 = compareArrays(xarray, xij[j]);
				dist[i][j] = new DocSpec(dist1, doctors2.get(it2.next()));
			}
			i++;
		}
	}
	
	private double compareArrays(int[] a1, int[] a2){
		int length = Math.min(a1.length, a2.length);
		double sim=0;
		for(int i=0; i<length; i++){
			sim+= ((a1[i]==1)&&(a2[i]==1))?1:0;
		}
		//System.out.println(sim);
		return sim/length;
	}
	
	private void getXij(){
		Set<Integer> docs = doctors.keySet();
		int lengthi = docs.size();
		//System.out.println(lengthi);
		int lengthj = hcps.size();
		xij = new int[lengthi][lengthj];
		
		int i=0,j=0;
		int doc, hcp;
		Iterator<Integer> it1 = docs.iterator();
		Iterator<Integer> it2 = hcps.iterator();
		
		while(it1.hasNext()){
			doc = it1.next();
			it2 = hcps.iterator();
			while(it2.hasNext()){
				hcp = it2.next();
				if(doctors.get(doc).contains(hcp)){
					xij[i][j]=1;
				}
				else{
					xij[i][j]=0;
				}
				j++;
			}
			i++;
			j=0;
		}
	}
	
	
	//PART 1 OF PARAMETERS CODE
	
	private HashSet<Double> getClusters(HashSet<Integer> array){
		int length = array.size();
		Object[] array0 = array.toArray();
		double[] d = new double[length];
		for(int i=0; i<length; i++){
			d[i] = (int)(array0[i])*1.0;
		}
		
		KMeans k = new KMeans(d);
		//You can change the number of centroids
		k.clustering(25, 10000, null);
		
		double[] k1 = k.getCentroids();
		HashSet<Double> hs = new HashSet<Double>();
		int ik1 = k1.length;
		for(int i=0; i<ik1; i++){
			hs.add(k1[i]);
		}
		return hs;
	}
	
	//Gets clusters for Doctors
	private void getDocClusters(){
		docluster = new HashMap<String, HashSet<Integer>>();
		int counter = 0, i=0;
		int[] npi = mr.npi;
		String[] provider = mr.provider_type;
		int length = npi.length;
		for(i=0; i<length; i++){
			if(!docluster.containsKey(provider[i])){
				docluster.put(provider[i], new HashSet<Integer>());
			}
			docluster.get(provider[i]).add(npi[i]);
		}
	}
	
	public void printDocClusters(){
		HashSet<Integer> npis;
		Iterator<Integer> it;
		Set<String> keys = docluster.keySet();
		for(String k: keys){
			npis = docluster.get(k);
			it = npis.iterator();
			System.out.println("Key: "+k);
			while(it.hasNext()){
				System.out.print(it.next()+" ");
			}
			System.out.println("\n");
		}
	}
	
	//Gets clusters for HCP
	private void getHcpClusters(HashSet<Double> hs){
		hcpcluster = new HashMap<Double, HashSet<Integer>>();
		Iterator<Double> it1 = hs.iterator();
		Iterator<Integer> it2 = hcps.iterator();
		double key;
		
		while(it1.hasNext()){
			hcpcluster.put(it1.next(), new HashSet<Integer>());
		}
		while(it2.hasNext()){
			int code = it2.next();
			key = getClosest(hs, code);
			hcpcluster.get(key).add(code);
		}
	}
	
	private double getClosest(HashSet<Double> hs, int c){
		Iterator<Double> it = hs.iterator();
		double d = 1000000, d1;
		double unit1, unit=0.0;
		while(it.hasNext()){
			unit1 = it.next();
			d1 = Math.abs(unit1-c);
			if(d1<d){
				d = d1;
				unit = unit1;
			}
		}
		return unit;
	}
	
	public void printHcpClusters(){
		HashSet<Integer> npis;
		Iterator<Integer> it;
		Set<Double> keys = hcpcluster.keySet();
		for(Double k: keys){
			npis = hcpcluster.get(k);
			it = npis.iterator();
			System.out.println("Key: "+k);
			while(it.hasNext()){
				System.out.print(it.next()+" ");
			}
			System.out.println("\n");
		}
	}
	
	public void connectDB(){
    	try {
    		//Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
			
			String message="";
			statement = connection.createStatement();
			pstatement1 = connection.prepareStatement("Insert into Doc_Clusters "+
					"Values(?, ?)");
			pstatement2 = connection.prepareStatement("Insert into Hcp_Clusters "+
					"Values(?, ?)");
			pstatement3 = connection.prepareStatement("Insert into failure "+
					"Values(?, ?)");
			connected = true;
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Database Connection Failed", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} /*catch (ClassNotFoundException e) {
			closeDB();
			JOptionPane.showMessageDialog(null, "Database Class Failed", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}*/
    }
	
	//HashMap<String, HashSet<Integer>> docluster;
	//HashMap<Double, HashSet<Integer>> hcpcluster;
	
	public void DocStore(){
		Set<String> keys = docluster.keySet();
		String key1;
		int key2;
		
		Iterator<String> it1 = keys.iterator();
		Iterator<Integer> it2;
		
		while(it1.hasNext()){
			key1 = it1.next();
			it2 = docluster.get(key1).iterator();
			while(it2.hasNext()){
				try {
					key2 = it2.next();
					pstatement1.setString(1, key1);
					pstatement1.setInt(2, key2);
					
					int result = pstatement1.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("Done Doc Storage");
	}
	
	public void HcpStore(){
		Set<Double> keys = hcpcluster.keySet();
		Double key1=0.0;
		int key2;
		
		Iterator<Double> it1 = keys.iterator();
		Iterator<Integer> it2;
		
		while(it1.hasNext()){
			key1 = it1.next();
			it2 = hcpcluster.get(key1).iterator();
			while(it2.hasNext()){
				try {
					key2 = it2.next();
					pstatement2.setInt(1, key1.intValue());
					pstatement2.setInt(2, key2);
					
					int result = pstatement2.executeUpdate();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(key1.intValue());
				}
			}
		}
		System.out.println("Done HCP Storage");
	}
	
	public void closeDB(){
    	if(connected==true){
    		try {
				//resultset.close();
				pstatement1.close();
				pstatement2.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Could not close database", "Error",
						JOptionPane.ERROR_MESSAGE);
			}    		
    	}
    }	

}
