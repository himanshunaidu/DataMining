package bayesianpackage;

public class DocSpec {
	
	private double sim;
	private String spec;
	
	public DocSpec(double dist1, String s1){
		sim = dist1;
		spec = s1;
	}
	
	public void setSim(double s){
		sim = s;
	}
	
	public void setSpec(String s){
		spec = s;
	}
	
	public double getSim(){
		return sim;
	}
	
	public String getSpec(){
		return  spec;
	}

}
