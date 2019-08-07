package bsz.expo.trafo;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import nu.xom.Document;

/**
 * Ein TrafoTicket transportiert die Daten einer Verarbeitung.
 * <p>
 * Eine TrafoTicket ist im wesentlichen eine Liste von Objekten sowie eine Reihe von 
 * Methoden um auf diese Attribute komfortabel zuzugreifen. Für Dateipfad- und 
 * Xom-Documentattribute sind convenience-Methoden vorgesehen. 
 * 
 * @author christof mainberger (christof.mainberger@bsz-bw.de)
 *
 */
public class TrafoTicket {
	
	private TrafoTicket parent = null;
	
	public TrafoTicket() {
		this.parent = null;
	}	
	
	public TrafoTicket(TrafoTicket parent) {
		this.parent = parent;
	}
	
	private Map<String, Object> attributes = new HashMap<>();	
	public void put(final String key, final Object obj) { attributes.put(key, obj); }	
	
	public Object get(final String key) {
		Object result = attributes.get(key);
		if (result == null && parent != null) {
			result = parent.get(key);
		}
		return result; 
	}	
	
	public String getString(final String key) { return (String) get(key); }
	public Long getLong(final String key) { return (Long) get(key); }	
	public Integer getInteger(final String key) { return (Integer) get(key); }	
	public Boolean getBoolean(final String key) { return (Boolean) get(key); }
	public Path getPath(final String key) { return (Path) get(key); }
	public Document getDocument(final String key) { return (Document) get(key); }
		
	public boolean has(final String key) { return attributes.containsKey(key); }	
	public void remove(final String key) { attributes.remove(key); }
	
	public Path getPath() { return (Path) get("path"); }
	public void setPath(final Path path) { put("path", path); }

	public Document getDocument() { return (Document) get("document"); }
	public void setDocument(final Document document) { put("document", document); }
	
	
	
		
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String key : attributes.keySet()) {
			sb.append(key).append(": ").append(attributes.get(key)).append(" \n");
		}
		return sb.toString();
	}
}
