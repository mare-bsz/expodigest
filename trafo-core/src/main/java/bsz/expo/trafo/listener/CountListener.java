package bsz.expo.trafo.listener;

import bsz.expo.trafo.TrafoListener;
import bsz.expo.trafo.TrafoTicket;

/**
 * CountListener zählt die Anzahl der verarbeiteten Datensätze aus einer Pipeline.
 * <p>
 * Die Klasse dient eher einem proof-of-concept einer {@link TrafoListener}.
 * 
 * @author Christof Mainberger (christof.mainberger@bsz-bw.de) *
 */
public class CountListener extends TrafoListener {
	
	private int count;
	
	@Override
	protected void process(TrafoTicket ticket) {
		count++;
	}
	
	@Override
	protected void finit() {
		trafoPipe.getTrafoPipeline().addParameter("reccount", String.valueOf(count));
		trafoPipe.getTrafoPipeline().addMessage("Es wurden " + String.valueOf(count) + " verarbeitet.");
	}

}
