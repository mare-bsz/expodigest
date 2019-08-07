package bsz.expo.trafo.pipes;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import javax.servlet.http.Part;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoPipe;
import bsz.expo.trafo.TrafoTicket;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;

/**
 * Eine XmlReaderPipe liest ein XML-File und splittet es in einzelne Datensätze.
 * <p> 
 * Der Name der zu verarbeitenden Datei wird als TrafoTicket-Attribut "dataFile" erwartet. Die mit
 * Slashs getrennte Liste der Elemente, die von der Wurzel ausgehend ignoriert werden sollen, 
 * werden im Parameter "records" festgelegt. Für jeden Record, der so erreicht wird, wird ein 
 * eigenes Ticket an die nächste Pipe emittiert.  
 *  
 * @author Christof Mainberger (christof.mainberger@bsz-bw.de)
 */
public class XmlReaderPipe extends TrafoPipe {

	private String[] recordpath;
	private XMLReader xmlReader;
	
	@Override
	public void init() throws TrafoException {
		this.recordpath = getParameter("records").split("/");
		try {
			this.xmlReader = XMLReaderFactory.createXMLReader();
		} catch (SAXException e) {
			throw new TrafoException(e);
		}
		this.xmlReader.setContentHandler(new XmlSplitter(this));
		super.init();
	}
	
	@Override
	public void process(TrafoTicket trafoTicket) throws TrafoException {		
		try {
			InputStream inputStream = null;
			final Object xmlsource = trafoTicket.get("xmlsource");
			if (xmlsource instanceof String) {				
				inputStream = new FileInputStream(trafoPipeline.getTrafoContext().getDataPath((String)xmlsource));
			} else if (xmlsource instanceof Part) {
				inputStream = ((Part)xmlsource).getInputStream();
			} else if (xmlsource instanceof InputStream) {
				inputStream = ((InputStream)xmlsource);
			} else {
				throw new TrafoException("Parameter xmlsource kein bekannter Typ oder nicht vorhanden");
			}
			this.xmlReader.parse(new InputSource(inputStream));
		} catch (Exception e) {
			throw new TrafoException(e);
		} 
	}
	
	private void processSuper(TrafoTicket trafoTicket) throws TrafoException { super.process(trafoTicket); }
	
	private class XmlSplitter extends DefaultHandler {
		
		private final XmlReaderPipe xmlReaderPipe;
		private final Deque<String> currentPath = new ArrayDeque<String>();
		private final StringBuilder text = new StringBuilder();
		Element current = null;
		Element root = null;
		Element wrap = null;
		
		public XmlSplitter(final XmlReaderPipe xmlReaderPipe) {
			this.xmlReaderPipe = xmlReaderPipe;
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			this.currentPath.push(localName);
			if (equalsPath(currentPath) && wrap == null) {
				this.wrap = current;
			}
			Element newElement = new Element(localName);
			for (int i = 0; i < attributes.getLength(); i++) {
				newElement.addAttribute(
					new Attribute(
						attributes.getLocalName(i), 
						attributes.getValue(i)));
			}
			if (current != null) {
				current.appendChild(newElement);
			} else {
				root = newElement;
			}
			text.setLength(0);
			current = newElement;			
		}	
	
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			text.append(ch, start, length);
		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {				 
			if (text.length() > 0) {
				current.appendChild(text.toString());
				text.setLength(0);
			}
			if (equalsPath(currentPath)) {
				TrafoTicket ticket = new TrafoTicket();
				ticket.setDocument(new Document((Element)root.copy()));
				try {
					xmlReaderPipe.processSuper(ticket);
				} catch (TrafoException e) {
					throw new SAXException(e);
				}
				wrap.removeChildren();
				current = wrap;
			} else {		
				current = (Element) current.getParent();
			}				
			this.currentPath.pop();
		}
		
		protected boolean equalsPath(Deque<String> current) {
			Iterator<String> iterator = current.descendingIterator();
			for (String element : recordpath) {
				if (!iterator.hasNext()) {
					return false;
				} else {
					if (!element.equals(iterator.next())) {
						return false;
					}
				}
			}
			if (iterator.hasNext()) {
				return false;
			}
			return true;			
		}
	}	
}
