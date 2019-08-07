package bsz.expo.trafo;

import java.util.ArrayList;
import java.util.List;

/**
 * TrafoConfig beschreibt ein Eingabefeld fuer die Konfiguration einer Pipeline.
 * <p>
 * Ein TrafoConfig-Objekt wird mit einem &lt;config... in einer Pipeline-Konfiguration festgelegt.
 * Es enthaelt lediglich Properties zu Namen, Label, Typ und entsprechend den Wert eines Eingabefelds,  
 * das zur interaktiven Konfiguration einer Pipeline angezeigt wird. Der Wert ist dann in den 
 * konkreten Unterklassen unterschiedlich spezifiziert. 
 *  
 * @author Christof Mainberger
 * 
 */
public class TrafoConfig {
	
	protected TrafoConfig(final String typ) {
		this.typ = typ;
	}
	
	private String name;
	public String getName() { return name; }
	public void setName(final String name) { this.name = name; }	
	
	private String label;
	public String getLabel() { return label; }
	public void setLabel(final String label) { this.label = label; }	
	
	private final String typ;
	public String getTyp() { return typ; }
	
	private Object value;
	public Object getValue() { return this.value; }
	public void setValue(final Object value) { this.value = value; }
	
	private List<TrafoOption> options = new ArrayList<TrafoOption>();
	public void addOption(final String value, final String label) { this.options.add(new TrafoOption(value, label)); }
	public List<TrafoOption> getOptions() { return this.options; }	
	
}	
