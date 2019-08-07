package bsz.expo.trafo.pipes;

import java.io.File;

import bsz.expo.trafo.TrafoConfig;
import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoPipe;
import bsz.expo.trafo.TrafoTicket;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Nodes;
import nu.xom.xslt.XSLException;
import nu.xom.xslt.XSLTransform;

/**
 * Eine XSLTPipe nimmt eine XSL-Transformation des Xom-Documents im {@link TrafoTicket} vor. 
 * <p>
 * Das erforderliche Stylesheet wird dabei im Parameter "stylesheet" übergeben. Parameter, deren Namen mit
 * "xslt" beginnen, werden dem transfomer-Objekt als Parameter übergeben.  
 * 
 * @author Christof Mainberger (christof.mainberger@bsz-bw.de)
 */
public class XSLTPipe extends TrafoPipe {
	
	Builder builder = new Builder();
	XSLTransform transform;
	
	@Override
	public void init() throws TrafoException {		
		try {
			final File stylesheetFile = new File(trafoPipeline.getPipelinePath(getParameter("stylesheet"))); 
			final Document stylesheet = builder.build(stylesheetFile);
			transform = new XSLTransform(stylesheet);
			// Dem Transformer werden Pipeline-Parameter mitgegeben
			for (String key : getParameters().keySet()) {
				if (key.startsWith("xslt")) {
					transform.setParameter(key, getParameter(key));
				}
			}
			for (TrafoConfig config : trafoPipeline.getConfiguration()) {
				if (config.getName().startsWith("xslt")) {
					transform.setParameter(config.getName(), config.getValue());
				}
			}
			transform.setParameter("pipeline", trafoPipeline);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TrafoException(e);
		} 		
		super.init();
	}
	
	@Override
	public void process(final TrafoTicket ticket) throws TrafoException {	
		try {
			final Nodes output = transform.transform(ticket.getDocument());
			if (output.size() > 0) {
				ticket.setDocument(XSLTransform.toDocument(output));
				super.process(ticket);
			}
		} catch (XSLException e) {
			System.out.println("Hier diese exception");
			throw new TrafoException(e);
		} 	
	}
	

}
