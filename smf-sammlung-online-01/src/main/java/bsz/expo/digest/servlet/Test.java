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

/**
 * Servlet implementation class Items
 */
@WebServlet("/test")
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Servlet Test: doGet start");
		
		try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build()) {	
			
			System.out.println("Servlet Test: SolrClient geöffnet");
								
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
				
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
			
			System.out.println("Servlet Test: writer erzeugt");
				
			final SolrQuery solrQuery  = new SolrQuery(); 
			solrQuery.setQuery("*:*");
			solrQuery.setRows(10);
			solrQuery.setStart(0);	
			
			System.out.println("Servlet Test: Query erzeugt");
							
			if (!client.query(solrQuery).getResults().isEmpty()) {
				
				writer.println("{ \"solrConnection\" : \"OK\" }");				
			
			} else {
				
				writer.println("{ \"solrConnection\" : \"NOT OK\" }");	
				
			}
			
			System.out.println("Servlet Test: Query abgesetzt");
				
			writer.close();
			writer.flush();	
			
			System.out.println("Servlet Test: doGet Ende");
		} catch (Exception se) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, se.getMessage());
		}	
		
	}
	
}
