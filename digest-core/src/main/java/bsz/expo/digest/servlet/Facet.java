package bsz.expo.digest.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;

import bsz.expo.digest.Digest;

import org.apache.solr.client.solrj.response.QueryResponse;

/**
 * Servlet implementation class Items
 */
@WebServlet("/facet")
public class Facet extends Digest {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build()) {
			
			final SolrQuery solrQuery  = new SolrQuery(); 
			solrQuery.setQuery("*:*");
			solrQuery.setFacet(true);
			solrQuery.addFacetField(validFacet(validRequired(request.getQueryString())));
			solrQuery.setFacetSort("index");
			solrQuery.setFacetMinCount(1);
			solrQuery.setFacetLimit(50000);
							
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
				
			final QueryResponse res = client.query(solrQuery);		
				
			FacetField ff = res.getFacetField(validFacet(request.getQueryString()));
			
			try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"))) {
				writer.println("[");
				boolean first = true;
				for (Count c : ff.getValues()) {
					if (! first) writer.println(", ");
					writer.println("{\"" + request.getQueryString() + "\":\"" + c.getName() + "\"}");
					if (first) first = false;
				}					
				writer.println("]");
			} 		
		} catch (IllegalArgumentException iae) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, iae.getMessage());
		} catch (SolrServerException se) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, se.getMessage());
		} catch (Exception e) {
			throw new ServletException(e);		
		} 	
	}	
	
}
