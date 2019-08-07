package bsz.expo.trafo;

import java.io.Serializable;
import java.nio.file.Path;

/**
 * Ein TrafoResult beschreibt das Ergebnis einer Transformation, sofern es in einer Datei vorliegt.
 * <p>
 * Eine {@link TrafoPipeline} enthält eine Liste von TrafoResults, die während der Transformation 
 * angelegt werden und nach Abschluss zum Download angeboten werden. 
 * 
 * @author christof mainberger (christof.mainberger@bsz-bw.de) *
 */
public class TrafoResult implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name = null;		
	public String getName() { return name; }
	public void setName(final String name) { this.name = name; }
	
	private String mime = null;
	public String getMime() { return mime; }
	public void setMime(final String mime) { this.mime = mime; }
	
	private String encoding = null;
	public String getEncoding() { return encoding; }
	public void setEncoding(final String encoding) { this.encoding = encoding; }
	
	private Path path = null;
	public Path getPath() { return path; }
	public void setPath(final Path path) { this.path = path; }	

}
