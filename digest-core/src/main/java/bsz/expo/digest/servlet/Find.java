package bsz.expo.digest.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;

import bsz.expo.Digest;

@WebServlet("/find")
public class Find extends Digest {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException {
		
		try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build()) {
			
			final Templates templates = getTemplates(request.getParameter("fmt"), "json");				
			
			response.setContentType(templates.getOutputProperties().getProperty("media-type"));		
			response.setCharacterEncoding("UTF-8");
			
			final SolrDocument doc = client.getById(validRequired(request.getParameter("id")));
			
			try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"))) {					
							
				if (doc != null) {
					transformRecord(writer, doc, templates.newTransformer());
				} else {
					if ("application/json".equals(templates.getOutputProperties().getProperty("media-type"))) {
						writer.append("{ \"result\" : \"leer\" }");
					} else {
						writer.append("<result></result>");
					}					
				}				
			} 			
		} catch (IllegalArgumentException iae) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, iae.getMessage());
		} catch (SolrServerException se) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, se.getMessage());
		} catch (TransformerConfigurationException tce) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, tce.getMessage());
		} catch (Exception e) {
			throw new ServletException(e);		
		}
	}		
}

