package bsz.expo.digest.servlet.smf;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import bsz.expo.Digest;

@WebServlet("/find")
public class Find extends Digest {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException {
		
		try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build()) {
			
			final SolrQuery solrQuery  = new SolrQuery(); 				
			solrQuery.setStart(validNat(request.getParameter("fst"), 0));
			solrQuery.setRows(validNat(request.getParameter("len"), 10));
			solrQuery.setQuery("id:"+validRequired(request.getParameter("id")));
			
			final Templates templates = getTemplates(request.getParameter("fmt"), "json");	
		
			response.setContentType(templates.getOutputProperties().getProperty("media-type"));
			response.setCharacterEncoding("UTF-8");
			
			final SolrDocumentList docs = client.query(solrQuery).getResults();			
			
			try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"))) {					
							
				if (! docs.isEmpty()) {					
					transformRecord(writer, docs.get(0), templates.newTransformer());
				} else {
					writer.append("{ \"result\" : \"leer\" }");
				}
				
			} 
			
		} catch (IllegalArgumentException iae) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, iae.getMessage());
		} catch (SolrServerException se) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, se.getMessage());
		} catch (TransformerConfigurationException tce) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, tce.getMessage());
		}catch (Exception e) {
			throw new ServletException(e);		
		}
	}	
		
	protected void transformRecord(final Writer writer, final SolrDocument doc, final Transformer transformer) 
			throws Exception {					
		try {
			transformer.setParameter("likes", doc.getFieldValue("likes"));
			transformer.transform(
				new StreamSource(new StringReader(doc.getFieldValue("payload").toString())), 
				new StreamResult( writer ));
		} catch (TransformerException e) {
			throw new Exception("Datensatz mit id " + doc.getFieldValue("id") + " lässt sich nicht transformieren");
		}				
	}
}
