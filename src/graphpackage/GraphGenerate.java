package graphpackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.*;

import storagepackage.MedicareRetriever;

public class GraphGenerate {
	public static final int length = 1000;
	private int i1=0;
	private MedicareRetriever mr = new MedicareRetriever();
	
	private boolean readyToProcess = true;
	//private HashMap<NpiType, HashSet<Integer> >doctors = new HashMap<NpiType, HashSet<Integer>>();
	private HashMap<Integer, HashSet<Integer> >doctors = new HashMap<Integer, HashSet<Integer>>();
	private HashMap<Integer, String >doctors2 = new HashMap<Integer, String>();
	private HashSet<Integer> hcpcs = new HashSet<Integer>();
	private HashSet<String> specialties = new HashSet<String>();
	private int npiPosition = -1;
	private int specPosition = -1;
	private int hcpcsPosition = -1;
	
	public GraphGenerate(){
		createDict();
	}
	
	
	public void createDict(){
		
		while(readyToProcess&&(i1<length)) {
			//System.out.println(i1);
	        if (!doctors.containsKey(mr.npi[i1])) {
	            System.out.println("Not present");
	        	//doctors.put(new NpiType(mr.npi[i1], mr.provider_type[i1]),(new HashSet<Integer>()));
	            doctors.put(mr.npi[i1],(new HashSet<Integer>()));
	            doctors2.put(mr.npi[i1], mr.provider_type[i1]);
	        }
	        if (!mr.hcpcs_code[i1].matches("\\d+")) {
	            String newHcpcs = "";
	            for (int i = 0; i < mr.hcpcs_code[i1].length(); i++) {
	                if (String.valueOf(mr.hcpcs_code[i1].charAt(i)).matches("\\d")) {
	                    newHcpcs = newHcpcs + mr.hcpcs_code[i1].charAt(i);
	                }
	                else{
	                    newHcpcs = newHcpcs + ((int)(mr.hcpcs_code[i1].charAt(i)));
	                }
	            }
	            mr.hcpcs_code[i1] = newHcpcs;
	        }
	        specialties.add(mr.provider_type[i1]);
	        int hcpcsValue = Integer.parseInt(mr.hcpcs_code[i1]);
	        hcpcs.add(hcpcsValue);
	        doctors.get(mr.npi[i1]).add(hcpcsValue);
	        
	        i1++;
	    }
		System.out.println("Done");
		i1=0;
	}
	

	public void createGraph(){
		
	}
	
	public HashMap<Integer, HashSet<Integer> > getDoctors(){
		return this.doctors;
	}
	
	public HashMap<Integer, String> getDoctors2(){
		return this.doctors2;
	}
	
	public HashSet<String> getSpecs(){
		return this.specialties;
	}
	
	public HashSet<Integer> getHcps(){
		return this.hcpcs;
	}
	
	public MedicareRetriever getMR(){
		return mr;
	}
	
	public void printSpecs(){
		Iterator<String> it = specialties.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
	}

}
