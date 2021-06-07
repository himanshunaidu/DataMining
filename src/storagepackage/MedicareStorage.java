package storagepackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class MedicareStorage {
	
	public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/data_mining?useSSL=false";
    private boolean connected = false;
    
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement pstatement = null;
    private PreparedStatement rstatement = null;
    private ResultSet resultset = null;
    private ResultSetMetaData metadata = null;
    
    private String dir = "<<Medicare TXT File>>";
    
    /*public int[] npi;
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
    public int[] hcpcs_code;
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
    public double[] stdev_Medicare_payment_amt;*/ 	
	
	public MedicareStorage(){
		connectDB();
		mainStore(dir);
	}
	
	public void connectDB(){
    	try {
    		//Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
			
			String message="";
			statement = connection.createStatement();
			pstatement = connection.prepareStatement("Insert into sample_dataset "+
					"Values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?)");
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
    
    public void storeFiles(int npi1, String nppes_provider_last_org_name1, String nppes_provider_first_name1, String nppes_provider_mi1, String nppes_credentials1, char nppes_provider_gender1,
    		char nppes_entity_code1, String nppes_provider_street11, String nppes_provider_street21, String nppes_provider_city1, int nppes_provider_zip1, String nppes_provider_state1, 		
    		String nppes_provider_country1, String provider_type1, char medicare_participation_indicator1, char place_of_service1, String hcpcs_code1, String hcpcs_description1,
    		char hcpcs_drug_indicator1, int line_srvc_cnt1, int bene_unique_cnt1, int bene_day_srvc_cnt1, double average_Medicare_allowed_amt1, double stdev_Medicare_allowed_amt1, 		
    		double average_submitted_chrg_amt1, double stdev_submitted_chrg_amt1, double average_Medicare_payment_amt1, double stdev_Medicare_payment_amt1){
    	
    	try {
    		pstatement.setInt(1, npi1);
    		pstatement.setString(2, nppes_provider_last_org_name1);
    		pstatement.setString(3, nppes_provider_first_name1);
    		pstatement.setString(4, nppes_provider_mi1);
    		pstatement.setString(5, nppes_credentials1);
    		pstatement.setString(6, ""+nppes_provider_gender1);
    		pstatement.setString(7, ""+nppes_entity_code1);
    		pstatement.setString(8, nppes_provider_street11);
    		pstatement.setString(9, nppes_provider_street21);
    		pstatement.setString(10, nppes_provider_city1);
    		pstatement.setInt(11, nppes_provider_zip1);
    		pstatement.setString(12, nppes_provider_state1);
    		pstatement.setString(13, nppes_provider_country1);
    		pstatement.setString(14, provider_type1);
    		pstatement.setString(15, ""+medicare_participation_indicator1);
    		pstatement.setString(16, ""+place_of_service1);
    		pstatement.setString(17, hcpcs_code1);
    		pstatement.setString(18, hcpcs_description1);
    		pstatement.setString(19, ""+hcpcs_drug_indicator1);
    		pstatement.setInt(20, line_srvc_cnt1);
    		pstatement.setInt(21, bene_unique_cnt1);
    		pstatement.setInt(22, bene_day_srvc_cnt1);
    		pstatement.setDouble(23, average_Medicare_allowed_amt1);
    		pstatement.setDouble(24, stdev_Medicare_allowed_amt1);
    		pstatement.setDouble(25, average_submitted_chrg_amt1);
    		pstatement.setDouble(26, stdev_submitted_chrg_amt1);
    		pstatement.setDouble(27, average_Medicare_payment_amt1);
    		pstatement.setDouble(28, stdev_Medicare_payment_amt1);
			
    		int result = pstatement.executeUpdate();
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Storage Failed", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
    }
    
    public void mainStore(String directory){
    	int limit = 1000000, i=0;
    	try {
    		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(directory)));

    		String line = null;
    		
    		reader.readLine();
    		reader.readLine();
    		
    		while(((line=reader.readLine())!=null)&&(i<limit)){
    			try{
    			String[] fields = line.split("\\t");
    			/*if((i%limit)==0){
    				System.out.println(Double.parseDouble(fields[0]));
    			}*/
    			this.storeFiles(Integer.parseInt(fields[0]), fields[1], fields[2], fields[3], fields[4], 
    					fields[5].charAt(0), fields[6].charAt(0), fields[7], fields[8], fields[9], Integer.parseInt(fields[10]), fields[11], fields[12], 
    					fields[13], fields[14].charAt(0), fields[15].charAt(0), (fields[16]), fields[17], fields[18].charAt(0), Integer.parseInt(fields[19]), 
    					Integer.parseInt(fields[20]), Integer.parseInt(fields[21]), Double.parseDouble(fields[22]), Double.parseDouble(fields[23]), Double.parseDouble(fields[24]), Double.parseDouble(fields[25]), Double.parseDouble(fields[26]), 
    					Double.parseDouble(fields[27]));
    			/*this.storeFiles(Integer.parseInt(fields[0]), fields[1], fields[2], fields[3], fields[4], 
    					fields[5].charAt(0), fields[6].charAt(0), fields[7], fields[8], fields[9], Integer.parseInt(fields[10]), fields[11], fields[12], 
    					fields[13], fields[14].charAt(0), fields[15].charAt(0), Integer.parseInt(fields[16]), fields[17], fields[18].charAt(0), Integer.parseInt(fields[19]), 
    					Integer.parseInt(fields[20]), Integer.parseInt(fields[21]), 0.0, 0.0, 0.0, 0.0, 0.0, 
    					0.0);*/
    			i++;
    			}
    			catch(Exception e){
        			continue;
        		}
    		}
    		
    		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("Storage Complete!!!");
    }
    
    private void retrieveFiles(int limit){
    	
    }
    
    /*private void initializeLength(int length){
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
	    hcpcs_code = new int[length];
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
    }*/
    
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
