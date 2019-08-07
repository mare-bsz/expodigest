package bsz.expo.trafo.pipes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoPipe;
import bsz.expo.trafo.TrafoTicket;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import nu.xom.converters.SAXConverter;

public class SolrSerializerPipe extends TrafoPipe {		
		
		SolrServer solr;	
		final List<SolrInputDocument> solrInputDocuments = new ArrayList<SolrInputDocument>();		
		
		@Override
		public void init() throws TrafoException {		
			solr = new HttpSolrServer(trafoPipeline.getTrafoContext().getInitParameter("solrUrl")+getParameter("shadowCore"));
			try {
				solr.deleteByQuery("*:*");
				solr.commit();
			} catch (Exception e) {
				throw new TrafoException(e);
			}
			super.init();
		}
		
		@Override
		public void process(TrafoTicket ticket) throws TrafoException {
			try {
				final Document xom = ticket.getDocument();
				if (xom != null) {				
					final SolrInputDocument currentDocument  = new SolrInputDocument();
					final SAXConverter saxConverter = new SAXConverter(new SolrHandler(currentDocument));
					saxConverter.convert(xom);
					solrInputDocuments.add(currentDocument);
					if (solrInputDocuments.size() == 100) {
						try {
							solr.add(solrInputDocuments);
							this.solr.commit();
						} catch (Exception e) {
							throw new TrafoException(e);
						}
						solrInputDocuments.clear();
					}
				}
				super.process(ticket);
			} catch (SAXException e) {
				throw new TrafoException(e); 
			}
		}
		
		@Override
		public void finit() throws TrafoException {
			try {
				if (!solrInputDocuments.isEmpty()) {
					this.solr.add(solrInputDocuments);
					this.solr.commit();
				}
				switchCores(trafoPipeline.getTrafoContext().getInitParameter("solrUrl"), getParameter("liveCore"), getParameter("shadowCore"));
			} catch (Exception e) {
				throw new TrafoException(e);
			} 			
			super.finit();
		}
		
		private String switchCores(final String solrUrl, final String liveCore, final String shadowCore) 
			throws IOException {
			try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			
				HttpGet httpGet = new HttpGet(solrUrl + "admin/cores?action=SWAP&core=" + shadowCore + "&other=" + liveCore);
				
				ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

	                public String handleResponse(
	                        final HttpResponse response) throws ClientProtocolException, IOException {
	                    int status = response.getStatusLine().getStatusCode();
	                    if (status >= 200 && status < 300) {
	                        HttpEntity entity = response.getEntity();
	                        return entity != null ? EntityUtils.toString(entity) : null;
	                    } else {
	                        throw new ClientProtocolException("Unexpected response status: " + status);
	                    }
	                }

	            };
	            return httpclient.execute(httpGet, responseHandler);	            
			} 
		}
		
		private class SolrHandler extends DefaultHandler {
			
			final SolrInputDocument solrInput;
			String name = null;
			StringBuilder content = new StringBuilder();;
			
			public SolrHandler(SolrInputDocument solrInput) {
				this.solrInput = solrInput;
			}
			
			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes)
					throws SAXException {
				if ("solrDoc".equals(localName)) {
					//doNothing				
				} else if ("solrField".equals(localName)) {
					name = attributes.getValue("name");
				} else {
					content.append("<" + localName);
					for (int i = 0; i < attributes.getLength(); i++) {
						content.append(" " + attributes.getLocalName(i) + "=\"" + toXml(attributes.getValue(i)) + "\"");
					}
					content.append(">");
				}			
			}
			
			@Override
			public void endElement(String uri, String localName, String qName) throws SAXException {
				if ("solrDoc".equals(localName)) {
					//doNothing				
				} else if ("solrField".equals(localName)) {
					solrInput.addField(name, content.toString());
					name = null;
					content.setLength(0);
				} else {
					content.append("</" + localName + ">");
				}
			}
			
			@Override
			public void characters(char[] ch, int start, int length) throws SAXException {
				if (name != null) {
					content.append(toXml(ch, start, length));
				}
			}
			
			private String toXml(String src) {
				return toXml(src.toCharArray(), 0, src.length());
			}
			
			private String toXml(char[] ch, int start, int length) {
				StringBuilder result = new StringBuilder();
				for (int i = start; i < start + length; i++) {
					switch (ch[i]) {
					case '<': 
						result.append("&lt;");
						break;
					case '>': 
						result.append("&gt;");
						break;
					case '"': 
						result.append("&quot;");
						break;
					case '\'': 
						result.append("&apos;");
						break;
					case '&':
						result.append("&amp;");
						break;
					default:
						result.append(ch[i]);
					}
				}
				return result.toString();
			}			
		}	
		
}
