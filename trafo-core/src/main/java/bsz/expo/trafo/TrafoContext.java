package bsz.expo.trafo;

import java.util.HashMap;
import java.util.Map;

/**
 * Die abstrakte Klasse TrafoContext beschreibt den externen Kontext einer Transformation.
 * <p>
 * Die abstrakte Klasse TrafoContext beschreibt den externen Kontext (JSF-, Servlet-,etc.) einer Transformation. 
 * Neben einer Map von Attributen enthaelt sie Funktionen um exteren Ressourcen (Dateisystem,...) anzusprechen.  
 * 
 * @author Christof Mainberger *
 */
public abstract class TrafoContext {
			
	private Map<String, Object> attributes = new HashMap<>();
	/** @param key der Name, unter dem ein Objekt registriert wurde
	 * @return ein Objekt, das unter einem Key registriert wurde. */
	public Object getAttribute(final String key) { return attributes.get(key); }
	/** @param key der Name, unter dem ein Objekt registiriert wird.
	  * @param object das Objekt, das regisitriert wird. */
	public void setAttribute(final String key, Object object) { this.attributes.put(key, object); }
	
	/** @param path zum aktuellen Kontext (webapp) relativ angegebenen Pfad
	 * @return den absoluten Pfad der relativ zum aktuellen Kontext (webapp) angegebenen Pfads */
	public abstract String getRealPath(String path);	
	
	/** @param path relativ zum vereinbarten Daten-Verzeichnis angegebene Datendatei.
	 * @return den absoluten Pfad der angegebenen Daten-Datei. */
	public abstract String getDataPath(String path);
	
	/** @param path relativ zum vereinbarten PipelineVerzeichnis angegebene Pipeline-Datei.
	 * @return absoluter Pfad der Pipeline-Datei*/
	public abstract String getPipelinePath(String path);
	
	/** @param path relativ zum vereinbarten Tempverzeichnis angegebene Result-Datei.
	 * @return absoluter Pfad der Result-Datei*/
	public abstract String getTempPath(String path);
	
	/** @param key Schluessel eines Init-Parameters des umfassenden Kontexts. 
	 * @return der Wert des Ini-Parameters zum angegebenen Schluessel, falls vorhanden, sonst null. */
	public abstract String getInitParameter(String key);	
	
}
