package bsz.expo.digest.servlet.blm;

import java.io.IOException;
import java.net.URLDecoder;

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
@WebServlet("/blm_dk/*")
public class BLM_DK extends Digest {
	private static final long serialVersionUID = 1L;
		
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		
		try  {
			
			final SolrQuery solrQuery  = new SolrQuery(); 
			solrQuery.setQuery("systematik:\"" + URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8") + "\"");
			solrQuery.setRows(5000);
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

