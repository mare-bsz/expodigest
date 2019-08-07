package bsz.expo.trafo.pipes;

import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoPipe;
import bsz.expo.trafo.TrafoTicket;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;

public class SplitPipe extends TrafoPipe {
	
		
	@Override
	public void process(TrafoTicket ticket) throws TrafoException {
		Document doc = ticket.getDocument();
		Nodes parts = doc.query(getParameter("xpath"));
		for (int i = 0; i < parts.size(); i++) {
			Document newDoc = new Document(new Element((Element)parts.get(i)));
			TrafoTicket newTicket = new TrafoTicket();
			newTicket.setDocument(newDoc);
			super.process(newTicket);
		}
	}	

}
