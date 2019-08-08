package bsz.expo.digest;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoPipe;
import bsz.expo.trafo.TrafoTicket;
import nu.xom.Serializer;

/**
 * ConsolePipe ist eine {@link TrafoPipe}, die ein {@link TrafoTicket} auf die Console oder in eine Textdatei ausgibt.
 * <p>
 * Ggf. wird der absolute Pfad der Textdatei dazu unter dem Schlüssel "outFile" in der Konfiguration festgelegt. 
 * 
 * @author Christof Mainberger (christof.mainberger@bsz-bw.de) *
 */
public class DigestXmlWriterPipe extends TrafoPipe {
	
	Serializer serializer;
	ServletOutputStream out = null;
	
	@Override
	public void init() throws TrafoException {		
		try {
			HttpServletResponse response = (HttpServletResponse) this.trafoPipeline.getAttribute("outputStream");
			response.setContentType("application/xml");
			response.setCharacterEncoding("UTF-8");
			out = response.getOutputStream();
			serializer = new Serializer(out, "UTF-8");
			out.print("<?xml version=\"1.0\" ?>");
			out.print("<result>");
			out.print("<head>");
			out.print("<numFound>" + getParameter("numfound") + "</numFound>");
			out.print("<qry>" + getParameter("qry") + "</qry>");
			out.print("<srt>" + getParameter("srt") + "</srt>");
			out.print("<fst>" + getParameter("fst") + "</fst>");
			out.print("<len>" + getParameter("len") + "</len>");
			out.print("<fmt>" + getParameter("fmt") + "</fmt>");
			out.print("</head>");				
		} catch (Exception e) {
			throw new TrafoException(e);
		}
		super.init();
	}
	
	@Override
	public void process(TrafoTicket ticket) throws TrafoException {
		if (ticket.getDocument() != null) {
			try {				
				serializer.write(ticket.getDocument());
				serializer.flush();
			} catch (IOException e) {
				throw new TrafoException(e);
			}			
		}		
		super.process(ticket);
	}	
	
	@Override
	public void finit() throws TrafoException {	
		try {
			out.println("</result>");			
		} catch (IOException e) {
			throw new TrafoException(e);
		}
		super.finit();
	}
}
