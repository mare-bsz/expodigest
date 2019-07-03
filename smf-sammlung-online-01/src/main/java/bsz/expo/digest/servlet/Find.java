package bsz.expo.digest.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Templates;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocumentList;

/**
 * Servlet implementation class Items
 */
@WebServlet("/find")
public class Find extends Digest {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException {
		
		final SolrQuery solrQuery  = new SolrQuery(); 		
		Templates templates = null;
				
		try {			
			solrQuery.setStart(validNat(request.getParameter("fst"), 0));
			solrQuery.setRows(validNat(request.getParameter("len"), 10));
			solrQuery.setQuery("id:"+request.getParameter("id"));
			templates = getTemplates(request.getParameter("fmt") != null ? request.getParameter("fmt") : "json");
		} catch (Exception se) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, se.getMessage());
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
				
		try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build();
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"))) {		
			
			SolrDocumentList docs = client.query(solrQuery).getResults();			
			if (! docs.isEmpty()) {					
				transformRecord(writer, docs.get(0), templates.newTransformer());
			} else {
				writer.append("{ \"result\" : \"leer\" }");
			}			
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}	
		
}
