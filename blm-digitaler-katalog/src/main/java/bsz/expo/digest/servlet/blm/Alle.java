package bsz.expo.digest.servlet.blm;

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
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import bsz.expo.Util;

/**
 * Servlet implementation class Aehnliche
 */
@WebServlet("/alle")
public class Alle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Alle() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build()) {
			
			final SolrQuery query =new SolrQuery();
			query.setQuery("*:*");
			query.setFields("id, inventarnummer, f_objektbezeichnung, f_titel");
			query.setStart(0);
			query.setRows(10000);
					
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
				
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
			
			final SolrDocumentList solrDocumentList = client.query(query).getResults();			
							
			writer.println("{ \"head\" : {");
			writer.println(" \"query\" : \"*:*\",");
			writer.println(" \"numFound\" : \"" + solrDocumentList.getNumFound() + "\"");
			writer.println("},");
			writer.println("\"records\" : ");
			writer.println("[");
			boolean first = true;
			StringBuilder result = new StringBuilder();
			for (SolrDocument solrDocument : solrDocumentList) {				
				if (!first) result.append(",");
				result.append("{\"id\":\"").append(solrDocument.get("id")).append("\",");
				if (solrDocument.getFirstValue("f_titel") != null) {
					result.append("\"objekttitel\":\"").append(Util.toJson(solrDocument.getFirstValue("f_titel").toString())).append("\",");
				} else {
					result.append("\"objektbezeichnung\":\"").append(Util.toJson(solrDocument.getFirstValue("f_objektbezeichnung").toString())).append("\",");
				}
				result.append("\"inventarnummer\":\"").append(Util.toJson(solrDocument.get("inventarnummer").toString())).append("\"}");
				writer.println(result.toString());
				result.setLength(0);
				first = false;
			}
			writer.println("]}");				
			writer.close();
			writer.flush();	
		} catch (Exception e) {
			throw new ServletException(e);		
		} 
	}	
}
