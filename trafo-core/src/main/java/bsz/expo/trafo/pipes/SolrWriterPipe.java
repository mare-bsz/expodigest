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

import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoPipe;
import bsz.expo.trafo.TrafoTicket;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;

public class SolrWriterPipe extends TrafoPipe {		
		
		String solrUrl;
		SolrServer solr;	
		final List<SolrInputDocument> solrInputDocuments = new ArrayList<SolrInputDocument>();		
		
		@Override
		public void init() throws TrafoException {	
			solrUrl = getParameter("solrUrl") != null ?
				getParameter("solrUrl") :
				trafoPipeline.getTrafoContext().getInitParameter("solrUrl");			
			solr = new HttpSolrServer(solrUrl+getParameter("shadowCore"));
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
			final Document xom = ticket.getDocument();
			if (xom != null) {
				final SolrInputDocument currentDocument  = new SolrInputDocument();
				final Nodes fields = xom.query("//doc/field");
				for (int i = 0; i < fields.size(); i++) {
					final Element field = (Element) fields.get(i);
					if (! field.getValue().isEmpty()) {
						currentDocument.addField(field.getAttributeValue("name"), field.getValue().replaceAll("„", "\""));
					}
				}
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
		}
		
		@Override
		public void finit() throws TrafoException {
			try {
				if (!solrInputDocuments.isEmpty()) {
					this.solr.add(solrInputDocuments);
					this.solr.commit();
				}
				switchCores(solrUrl, getParameter("liveCore"), getParameter("shadowCore"));
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
		
		
		
		
		
}
