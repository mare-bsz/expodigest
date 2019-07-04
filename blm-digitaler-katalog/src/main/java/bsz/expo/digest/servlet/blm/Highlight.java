package bsz.expo.digest.servlet.blm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.solr.client.solrj.SolrQuery;

import bsz.expo.Digest;
	
/**
 * Bequemlichkeits-Servlet: Sucht nach der Inventarnummer
 */
@WebServlet("/highlight")
public class Highlight extends Digest {
	private static final long serialVersionUID = 1L;
		
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		
		try  {
			
			final SolrQuery solrQuery  = new SolrQuery(); 
			solrQuery.setQuery("highlight:true");
			solrQuery.setRows(2000);
			solrQuery.setStart(0);				
			
			final Templates templates = getTemplates(request.getParameter("fmt"), "json");	
			
			writeToResponse(response, solrQuery, templates.newTransformer()); 	 
			
		} catch (IllegalArgumentException iae) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, iae.getMessage());
		} catch (TransformerConfigurationException tce) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, tce.getMessage());
		} catch (Exception e) {
			throw new ServletException(e);		
		} 
	}			
}

