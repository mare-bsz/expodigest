package bsz.expo.trafo.pipes;

import java.util.HashMap;
import java.util.Map;

import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoPipe;
import bsz.expo.trafo.TrafoTicket;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;

/** 
 * @author Christof Mainberger (christof.mainberger@bsz-bw.de) 
 */
public class JoinPipe extends TrafoPipe {
	
	Element container;
	Element root;
	String defaultNamespace = "";
	
	@Override
	public void init() throws TrafoException {
		final String path[] = getParameter("container").split("#");
		root = prepareElement(path[0]);
		container = root;
		for (int i = 1; i < path.length; i++) {			
			final Element current = prepareElement(path[i]);
			container.appendChild(current);
			container = current;
		}
		super.init();
	}	
	
	private Element prepareElement(String path) {
		
		Element result = null;
		String name;
		Map<String, String> attributes = new HashMap<>();
		Map<String, String> namespaces = new HashMap<>();		
		int at = path.indexOf('@');
		if (at == -1) {
			name = path.trim();
		} else {
			name = path.substring(0, at).trim();
			for (String att : path.substring(at+1).split(",")) {
				int eq = att.indexOf('=');
				final String attName = att.substring(0,eq).trim();
				final String attValue = att.substring(eq+1).trim();
				if (attName.startsWith("xmlns")) {
					int colon = attName.indexOf(':');
					if (colon == -1) {
						defaultNamespace = attValue;
					} else {
						namespaces.put(attName.substring(colon+1), attValue);
					}
				} else {
					attributes.put(attName, attValue);
				}				
			}			
		}
		if (defaultNamespace.isEmpty()) {
			result = new Element(name);
		} else {
			result = new Element(name, defaultNamespace);
		}
		for (String ns : namespaces.keySet()) {
			result.addNamespaceDeclaration(ns, namespaces.get(ns));
		}
		for (String att : attributes.keySet()) {
			result.addAttribute(new Attribute(att, attributes.get(att)));
		}		
		return result;
	}
		
	@Override
	public void process(TrafoTicket ticket) throws TrafoException {			
		container.appendChild((Element)ticket.getDocument().getRootElement().copy());
	}
	
	@Override
	public void finit() throws TrafoException {	
		TrafoTicket ticket = new TrafoTicket();
		ticket.setDocument(new Document(root));
    	super.process(ticket);
		super.finit();
	}	

}
