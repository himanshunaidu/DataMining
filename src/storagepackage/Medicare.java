package storagepackage;

public class Medicare {
	
	public static void main(String[] args){
		//MedicareStorage ms= new MedicareStorage();
		MedicareRetriever mr = new MedicareRetriever();
		int length = 10;
		for(int i=0; i<length; i++){
			System.out.println(mr.npi[i]);
		}
	}
    
    

}
