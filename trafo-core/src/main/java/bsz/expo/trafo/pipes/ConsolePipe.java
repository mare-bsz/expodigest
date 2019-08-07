package bsz.expo.trafo.pipes;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;

import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoPipe;
import bsz.expo.trafo.TrafoResult;
import bsz.expo.trafo.TrafoTicket;
import nu.xom.Serializer;

/**
 * ConsolePipe ist eine {@link TrafoPipe}, die ein {@link TrafoTicket} auf die Console oder in eine Textdatei ausgibt.
 * <p>
 * Ggf. wird der absolute Pfad der Textdatei dazu unter dem Schlüssel "outFile" in der Konfiguration festgelegt. 
 * 
 * @author Christof Mainberger (christof.mainberger@bsz-bw.de) *
 */
public class ConsolePipe extends TrafoPipe {
	
	Serializer serializer;
	PrintStream out = null;
	
	@Override
	public void init() throws TrafoException {		
		try {
			final String encoding = getParameter("outCoding") != null ? getParameter("outCoding") : "UTF-8";
			if (getParameter("absolutePath") != null) {
				out = new PrintStream(getParameter("absolutePath"), encoding);
				final TrafoResult result = new TrafoResult();
				result.setName(getParameter("absolutePath"));
				result.setPath(Paths.get(getParameter("absolutePath")));
				trafoPipeline.addResult(result);
			} else if (getParameter("outFile") != null) {
				out = new PrintStream(trafoPipeline.getTempPath(getParameter("outFile")), encoding);
				final TrafoResult result = new TrafoResult();
				result.setName(getParameter("outFile"));
				result.setMime(getParameter("mimetype") != null ? getParameter("mimetype") : "application/xml");
				result.setEncoding(encoding);
				result.setPath(Paths.get(trafoPipeline.getTempPath(getParameter("outFile"))));
				trafoPipeline.addResult(result);
			} else {
				out = System.out;
			}
			serializer = new Serializer(out, encoding);
			if (getParameter("indent") != null) {
				serializer.setIndent(Integer.parseInt(getParameter("indent")));
			} 
			if (getParameter("length") != null) {
				serializer.setMaxLength(Integer.parseInt(getParameter("indent")));
			} 
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
		for (String msg : trafoPipeline.getMessages()) {
			System.out.println(msg);
		}
		out.close();
		super.finit();
	}
}
