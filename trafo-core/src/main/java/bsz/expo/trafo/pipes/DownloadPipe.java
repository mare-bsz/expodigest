package bsz.expo.trafo.pipes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;

import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoPipe;
import bsz.expo.trafo.TrafoResult;

public class DownloadPipe  extends TrafoPipe {	
	
	protected Writer wrt;
	
	protected String mimetype;
	protected void setMimetype(final String mimetype) { this.mimetype = mimetype; }
	
	protected String encoding = "UTF-8";
	protected void setEncoding(final String encoding) { this.encoding = encoding; }
		
	protected String filename;
	protected void setFilename(String filename) { this.filename = filename; }
			
	@Override
	public void init() throws TrafoException {
		TrafoResult result = new TrafoResult();
		result.setName(filename);
		result.setPath(Paths.get(trafoPipeline.getTempPath(filename)));
		result.setMime(mimetype);
		result.setEncoding(encoding);
		trafoPipeline.addResult(result);
		try {
			wrt = new OutputStreamWriter(new FileOutputStream(trafoPipeline.getTempPath(filename)), encoding);
		} catch (IOException e) {
			throw new TrafoException(e);
		}	
		
		System.out.println(((OutputStreamWriter)wrt).getEncoding());
		
		super.init();
	}
	
	protected String getCurrentDate() {
		GregorianCalendar now = new GregorianCalendar();
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		return "" + now.get(Calendar.YEAR) + (month < 10?"0":"")+ month + (day < 10?"0":"") + day;
	}
	
	

}
