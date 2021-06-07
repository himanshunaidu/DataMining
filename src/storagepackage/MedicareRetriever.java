package storagepackage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class MedicareRetriever {
	
	public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/data_mining?useSSL=false";
	
    private boolean connected = false;
    
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement pstatement = null;
    private PreparedStatement rstatement = null;
    private ResultSet resultset = null;
    private ResultSetMetaData metadata = null;
    
    private final File dir = new File("<<Medicare TXT file>>");
    
    public int[] npi;
    public String[] nppes_provider_last_org_name;
    public String[] nppes_provider_first_name;
    public String[] nppes_provider_mi;
    public String[] nppes_credentials;
    public char[] nppes_provider_gender;
    public char[] nppes_entity_code;
    public String[] nppes_provider_street1;
    public String[] nppes_provider_street2;
    public String[] nppes_provider_city;
    public int[] nppes_provider_zip;	
    public String[] nppes_provider_state; 		
    public String[] nppes_provider_country; 		
    public String[] provider_type;	 				
    public char[] medicare_participation_indicator; 	
    public char[] place_of_service;
    public String[] hcpcs_code;
    public String[] hcpcs_description; 
    public char[] hcpcs_drug_indicator;
    public int[] line_srvc_cnt;	
    public int[] bene_unique_cnt;  		
    public int[] bene_day_srvc_cnt; 		
    public double[] average_Medicare_allowed_amt; 		
    public double[] stdev_Medicare_allowed_amt; 		
    public double[] average_submitted_chrg_amt; 	
    public double[] stdev_submitted_chrg_amt; 		
    public double[] average_Medicare_payment_amt; 
    public double[] stdev_Medicare_payment_amt; 
    
    public static final int length=1000;
	
	public MedicareRetriever(){
		connectDB();
		retrieveFiles(length);
	}
	
	public void connectDB(){
    	try {
    		//Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
			
			String message="";
			statement = connection.createStatement();
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
    
    private void storeFiles(int id, String name, String path, double[] array){
    	
    	try {
    		ResultSet rs = statement.executeQuery("Select * from sample_dataset limit 100");
    		
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Retrieval Failed", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
    }
    
    public void retrieveFiles(int limit){
    	int id, i=0, l=0, length1=0;
    	String path, name, array;
    	try {
    		resultset = statement.executeQuery("Select count(*) from sample_dataset;");
    		if(resultset.next()){
    		length1 = resultset.getInt(1);}
    		//System.out.println(length);
    		initializeLength(length);
			
    		resultset = statement.executeQuery("Select * from sample_dataset limit "+length+";");
    		
    		while((resultset.next())&&(i<limit)){
    			npi[i] = resultset.getInt(1);
        	    nppes_provider_last_org_name[i] = resultset.getString(2);
        	    nppes_provider_first_name[i] = resultset.getString(3);
        	    nppes_provider_mi[i] = resultset.getString(4);
        	    nppes_credentials[i] = resultset.getString(5);
        	    nppes_provider_gender[i] = resultset.getString(6).charAt(0);
        	    nppes_entity_code[i] = resultset.getString(7).charAt(0);
        	    nppes_provider_street1[i] = resultset.getString(8);
        	    nppes_provider_street2[i] = resultset.getString(9);
        	    nppes_provider_city[i] = resultset.getString(10);
        	    nppes_provider_zip[i] = resultset.getInt(11);	
        	    nppes_provider_state[i] = resultset.getString(12); 		
        	    nppes_provider_country[i] = resultset.getString(13); 		
        	    provider_type[i] = resultset.getString(14);	 				
        	    medicare_participation_indicator[i] = resultset.getString(15).charAt(0); 	
        	    place_of_service[i] = resultset.getString(16).charAt(0);
        	    hcpcs_code[i] = resultset.getString(17);
        	    hcpcs_description[i] = resultset.getString(18); 
        	    hcpcs_drug_indicator[i] = resultset.getString(19).charAt(0);
        	    line_srvc_cnt[i] = resultset.getInt(20);	
        	    bene_unique_cnt[i] = resultset.getInt(21);  		
        	    bene_day_srvc_cnt[i] = resultset.getInt(22); 		
        	    average_Medicare_allowed_amt[i] = resultset.getDouble(23); 		
        	    stdev_Medicare_allowed_amt[i] = resultset.getDouble(24); 		
        	    average_submitted_chrg_amt[i] = resultset.getDouble(25); 	
        	    stdev_submitted_chrg_amt[i] = resultset.getDouble(26); 		
        	    average_Medicare_payment_amt[i] = resultset.getDouble(27); 
        	    stdev_Medicare_payment_amt[i] = resultset.getDouble(28); 
        	    
        	    i++;
    		}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Retrieval Failed", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
    }
    
    private void initializeLength(int length){
    	npi = new int[length];
	    nppes_provider_last_org_name = new String[length];
	    nppes_provider_first_name = new String[length];
	    nppes_provider_mi = new String[length];
	    nppes_credentials = new String[length];
	    nppes_provider_gender = new char[length];
	    nppes_entity_code = new char[length];
	    nppes_provider_street1 = new String[length];
	    nppes_provider_street2 = new String[length];
	    nppes_provider_city = new String[length];
	    nppes_provider_zip = new int[length];	
	    nppes_provider_state = new String[length]; 		
	    nppes_provider_country = new String[length]; 		
	    provider_type = new String[length];	 				
	    medicare_participation_indicator = new char[length]; 	
	    place_of_service = new char[length];
	    hcpcs_code = new String[length];
	    hcpcs_description = new String[length]; 
	    hcpcs_drug_indicator = new char[length];
	    line_srvc_cnt = new int[length];	
	    bene_unique_cnt = new int[length];  		
	    bene_day_srvc_cnt = new int[length]; 		
	    average_Medicare_allowed_amt = new double[length]; 		
	    stdev_Medicare_allowed_amt = new double[length]; 		
	    average_submitted_chrg_amt = new double[length]; 	
	    stdev_submitted_chrg_amt = new double[length]; 		
	    average_Medicare_payment_amt = new double[length]; 
	    stdev_Medicare_payment_amt = new double[length];
    }
    
    public void closeDB(){
    	if(connected==true){
    		try {
				//resultset.close();
				pstatement.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Could not close database", "Error",
						JOptionPane.ERROR_MESSAGE);
			}    		
    	}
    }

}
