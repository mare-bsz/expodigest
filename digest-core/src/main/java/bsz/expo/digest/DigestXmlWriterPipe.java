package bsz.expo.digest;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoPipe;
import bsz.expo.trafo.TrafoTicket;

/**
 * ConsolePipe ist eine {@link TrafoPipe}, die ein {@link TrafoTicket} auf die Console oder in eine Textdatei ausgibt.
 * <p>
 * Ggf. wird der absolute Pfad der Textdatei dazu unter dem Schlüssel "outFile" in der Konfiguration festgelegt. 
 * 
 * @author Christof Mainberger (christof.mainberger@bsz-bw.de) *
 */
public class DigestXmlWriterPipe extends TrafoPipe {
	
	PrintWriter writer = null;
	
	@Override
	public void init() throws TrafoException {		
		try {
			HttpServletResponse response = (HttpServletResponse) this.trafoPipeline.getAttribute("response");
			response.setContentType("application/xml");
			response.setCharacterEncoding("UTF-8");
			writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
			writer.print("<?xml version=\"1.0\" ?>");
			writer.print("<result>");
			writer.print("<head>");
			writer.print("<numFound>" + getParameter("numfound") + "</numFound>");
			writer.print("<qry>" + getParameter("qry") + "</qry>");
			writer.print("<srt>" + getParameter("srt") + "</srt>");
			writer.print("<fst>" + getParameter("fst") + "</fst>");
			writer.print("<len>" + getParameter("len") + "</len>");
			writer.print("<fmt>" + getParameter("fmt") + "</fmt>");
			writer.print("</head>");				
		} catch (Exception e) {
			throw new TrafoException(e);
		}
		super.init();
	}
	
	@Override
	public void process(TrafoTicket ticket) throws TrafoException {
		if (ticket.getDocument() != null) {
			writer.print(ticket.getDocument().getRootElement().toXML());						
		}		
		super.process(ticket);
	}	
	
	@Override
	public void finit() throws TrafoException {	
		writer.println("</result>");
		writer.close();
		super.finit();
	}
}
