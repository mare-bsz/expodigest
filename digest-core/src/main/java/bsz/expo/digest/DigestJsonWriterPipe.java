package bsz.expo.digest;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import bsz.expo.Util;
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
 * ConsolePipe ist eine {@link TrafoPipe}, die ein {@link TrafoTicket} auf die Console oder in eine Textdatei ausgibt.
 * <p>
 * Ggf. wird der absolute Pfad der Textdatei dazu unter dem Schlüssel "outFile" in der Konfiguration festgelegt. 
 * 
 * @author Christof Mainberger (christof.mainberger@bsz-bw.de) *
 */
public class DigestJsonWriterPipe extends TrafoPipe {
	
	Builder builder = new Builder();
	PrintWriter writer = null;
	XSLTransform transform;
	boolean first;
	
	@Override
	public void init() throws TrafoException {		
		try {	
			final File stylesheetFile = new File(trafoPipeline.getPipelinePath("json.xsl")); 
			final Document stylesheet = builder.build(stylesheetFile);
			transform = new XSLTransform(stylesheet);
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
			HttpServletResponse response = (HttpServletResponse) this.trafoPipeline.getAttribute("response");
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
			writer.print("{ \"head\" : { ");
			writer.print(" \"numFound\" : \"" + getParameter("numfound") + "\", ");
			writer.print(" \"qry\" : \"" + Util.toJson(getParameter("qry")) + "\", ");
			writer.print(" \"srt\" : \"" + getParameter("srt") + "\", ");
			writer.print("\"fst\" : \"" + getParameter("fst") + "\", ");
			writer.print("\"len\" : \"" + getParameter("len") + "\", ");
			writer.print("\"fmt\" : \"" + getParameter("fmt") + "\" ");
			writer.print("},");
			writer.print("\"records\" : ");
			writer.print("[");
			first = true;
		} catch (Exception e) {
			throw new TrafoException(e);
		}
		super.init();
	}
	
	@Override
	public void process(TrafoTicket ticket) throws TrafoException {		
		if (ticket.getDocument() != null) {
			try {
				if (first) {
					first = false;
				} else {
					writer.print(" , ");
				}
				final Nodes output = transform.transform(ticket.getDocument());
				for (int i = 0; i < output.size(); i++) {
					writer.print(output.get(i).getValue());
				}			 			
			} catch (XSLException xe) {
				throw new TrafoException(xe);
			} 						
		}		
		super.process(ticket);
	}	
	
	@Override
	public void finit() throws TrafoException {	
		writer.println("]");
		writer.println("}");
		writer.close();
		super.finit();
	}
}
