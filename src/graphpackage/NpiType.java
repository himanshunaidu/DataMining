package graphpackage;

public class NpiType {
	
	private int npi;
	private String type;
	
	public NpiType(int np, String ty){
		npi = np;
		type = ty;
	}
	
	public int getNpi(){
		return npi;
	}
	
	public String getType(){
		return type;
	}
	
	public void setNpi(int np){
		npi = np;
	}
	
	public void setType(String ty){
		type = ty;
	}

}
