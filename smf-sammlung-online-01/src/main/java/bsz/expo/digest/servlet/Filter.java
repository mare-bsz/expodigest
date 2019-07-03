package bsz.expo.digest.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Templates;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocumentList;

import de.jotwerk.Util;

/**
 * Servlet implementation class Items
 */
@WebServlet("/filter")
public class Filter extends Digest {
	private static final long serialVersionUID = 1L;
		
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			final SolrQuery solrQuery  = new SolrQuery(); 
			solrQuery.setStart(validNat(request.getParameter("fst"), 0));
			solrQuery.setRows(validNat(request.getParameter("len"), 10));
			solrQuery.setSort(validSort(request.getParameter("srt"), "s_entstehungszeit"), SolrQuery.ORDER.asc);
			solrQuery.setFilterQueries("x_" + request.getParameter("fld")+":"+request.getParameter("trm"));
			solrQuery.setQuery("*:*");
			final Templates templates = getTemplates(request.getParameter("fmt") != null ? request.getParameter("fmt") : "json");
							
			try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build();
				 PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"))) {				
								
				response.setContentType(templates.getOutputProperties().getProperty("media-type"));
				response.setCharacterEncoding("UTF-8");
				
				final SolrDocumentList solrDocumentList = client.query(solrQuery).getResults();
				
				if ("xml".equals(templates.getOutputProperties().getProperty("method"))) {
					writer.println("<?xml version=\"1.0\" ?>");
					writer.println("<result>");
					writer.println("<head>");
					writer.println("<query>" + solrQuery.getQuery() + "</query>");
					writer.println("<numFound>" + solrDocumentList.getNumFound() + "</numFound>");
					writer.println("</head>");
					transformRecords(writer, solrDocumentList, templates.newTransformer());					
					writer.println("</result>");
				} else {
					writer.println("{ \"head\" : {");
					writer.println(" \"query\" : \"" + Util.toJson(solrQuery.getQuery()) + "\",");
					writer.println(" \"numFound\" : \"" + solrDocumentList.getNumFound() + "\"");
					writer.println("},");
					writer.println("\"records\" : ");
					writer.println("[");
					transformRecords(writer, solrDocumentList, templates.newTransformer());
					writer.println("]");
					writer.println("}");
				}				
			} 		
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			//response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (Exception se) {
			se.printStackTrace();
			//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, se.getMessage());
		}	
	}
	
	protected String validSort(final String parameter, String result) throws IllegalArgumentException {
		if (parameter != null && ! parameter.isEmpty()) {
			if (getServletContext().getInitParameter("sortierfelder").contains(parameter)) {
				result = "s_" + parameter;
			} else {
				throw new IllegalArgumentException("Parameter " + parameter + " ist nicht in den Sortierfeldern!");
			}
		}
		return result;
	}	
}
