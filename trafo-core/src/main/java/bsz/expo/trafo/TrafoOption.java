package bsz.expo.trafo;

public class TrafoOption {
	
	public TrafoOption(String value, String label) {
		this.value = value;
		this.label = label;
	}
	
	private final String value;
	public String getValue() { return this.value; }
	
	private final String label;
	public String getLabel() { return this.label; }	

}
