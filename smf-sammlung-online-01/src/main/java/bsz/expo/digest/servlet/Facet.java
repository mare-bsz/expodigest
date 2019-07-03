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
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
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
		
		try {
			
			final SolrQuery solrQuery  = new SolrQuery(); 
			solrQuery.setQuery("*:*");
			solrQuery.setFacet(true);
			solrQuery.addFacetField("x_" + request.getQueryString());
			solrQuery.setFacetSort("index");
			solrQuery.setFacetMinCount(1);
			solrQuery.setFacetLimit(50000);
							
			try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build();
				 PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"))) {				
								
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				
				final QueryResponse res = client.query(solrQuery);
							
				writer.println("[");
								
				FacetField ff = res.getFacetField("x_" + request.getQueryString());
				boolean first = true;
				for (Count c : ff.getValues()) {
					if (! first) writer.println(", ");
					writer.println("{\"" + request.getQueryString() + "\":\"" + c.getName() + "\"}");
					if (first) first = false;
				}		
					
				writer.println("]");		
							
			} 		
		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (Exception se) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, se.getMessage());
		}	
	}	
}
