package bsz.expo.digest.servlet.smf;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import bsz.expo.Util;
import bsz.expo.digest.servlet.Export;

/**
 * Servlet implementation class Items
 */
@WebServlet("/selekt")
public class Selekt extends Export {
	private static final long serialVersionUID = 1L;	
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {			
				
		try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build()) {		
			
			final SolrQuery solrQuery  = new SolrQuery();
			solrQuery.setStart(validNat(request.getParameter("fst"), 0));
			solrQuery.setRows(validNat(request.getParameter("len"), 10));			
			solrQuery.setSort("s_" + validSort(request.getParameter("srt")), ("desc".equals(request.getParameter("ord")) ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
			solrQuery.setQuery(compileQuery(request.getParameter("qry"), "*:*"));
			solrQuery.setFilterQueries(compileFilter(request.getParameter("flt"),new String[]{"display:browse"}));
			
			String[] facets = null;
			if (request.getParameter("fct") != null && !request.getParameter("fct").isEmpty()) {
				solrQuery.setFacet(true);
				facets = request.getParameter("fct").split(";");
				for (String facet : facets) {
					solrQuery.addFacetField(validFacet(facet));					
				}				
				solrQuery.setFacetSort("index");
				solrQuery.setFacetMinCount(1);
				solrQuery.setFacetLimit(50000);				
			}
			
			final Templates templates = getTemplates(request.getParameter("fmt"), "json");
							
			final QueryResponse solrResponse = client.query(solrQuery);				
			final SolrDocumentList solrDocumentList = solrResponse.getResults();								
				
			response.setContentType(templates.getOutputProperties().getProperty("media-type"));
			response.setCharacterEncoding("UTF-8");
				
			try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"))) {
				
				if ("xml".equals(templates.getOutputProperties().getProperty("method"))) {
					writer.println("<?xml version=\"1.0\" ?>");
					writer.println("<result>");
					writer.println("<head>");
					writer.println("<qry>" + request.getParameter("qry") + "</qry>");	
					writer.println("<solrqry>" + solrQuery.getQuery() + "</solrqry>");	
					writer.println("<flt>" + request.getParameter("flt") + "</flt>");
					writer.println("<fct>" + request.getParameter("fct") + "</fct>");
					writer.println("<srt>" + request.getParameter("srt") + "</srt>");
					writer.println("<ord>" + request.getParameter("ord") + "</ord>");
					writer.println("<numFound>" + solrDocumentList.getNumFound() + "</numFound>");
					writer.println("<fst>" + request.getParameter("fst") + "</fst>");
					writer.println("<len>" + request.getParameter("len") + "</len>");
					writer.println("</head>");
					writer.println("<records>");
					transformRecords(writer, solrDocumentList, templates.newTransformer());	
					writer.println("</records>");
					if (facets != null) {
						writer.println("<facets>");
						for (String facet : facets) {
							writer.println("<" + facet + ">");
							FacetField ff = solrResponse.getFacetField("x_" + facet);
							for (Count c : ff.getValues()) {
								writer.println("<term>" + c.getName() + "</term>");
								writer.println("<anzahl>" + c.getName() + "</anzahl>");
							}	
							writer.println("</" + facet + ">");
						}
						writer.println("</facets>");
					}
					writer.println("</result>");
				} else {
					writer.println("{ \"head\" : {");
					writer.println(" \"qry\" : \"" + Util.toJson(request.getParameter("qry")) + "\",");
					writer.println(" \"solrqry\" : \"" + Util.toJson(solrQuery.getQuery()) + "\",");
					writer.println("\"flt\" : \"" + request.getParameter("flt") + "\",");
					writer.println("\"fct\" : \"" + request.getParameter("fct") + "\",");	
					writer.println("\"srt\" : \"" + request.getParameter("srt") + "\",");
					writer.println("\"ord\" : \"" + request.getParameter("ord") + "\",");
					writer.println("\"numFound\" : \"" + solrDocumentList.getNumFound() + "\",");
					writer.println("\"fst\" : \"" + request.getParameter("fst") + "\",");
					writer.println("\"len\" : \"" + request.getParameter("len") + "\"");
					writer.println("},");
					writer.println("\"records\" : ");
					writer.println("[");
					transformRecords(writer, solrDocumentList, templates.newTransformer());
					writer.println("]");
					if (facets != null) {
						writer.println(",\"facets\" : {");
						boolean firstFacet = true;
						for (String facet : facets) {
							if (!firstFacet) writer.print(", ");
							writer.println("\"" + facet + "\" : [");
							FacetField ff = solrResponse.getFacetField("x_" + facet);
							boolean firstTerm = true;
							for (Count c : ff.getValues()) {
								if (! firstTerm) writer.print(", ");
								writer.println("{\"term\":\"" + c.getName() + "\", \"anzahl\" : \"" + c.getCount() + "\"}");
								if (firstTerm) firstTerm = false;
							}	
							writer.println("]");
							if (firstFacet) firstFacet = false;
						}
						writer.println("}");
					}					
					writer.println("}");
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
	
	protected String[] compileFilter(final String query, String[] result) throws IllegalArgumentException {
		
		if (query != null && ! query.isEmpty()) {
			result = query.split(";");
			for (int i = 0; i < result.length; i++) {
				int colon = result[i].indexOf(":");
				if (colon > 0 && colon < result[i].length()-1) {
					String field = result[i].substring(0, colon);
					String term = result[i].substring(colon+1);
					if (getServletContext().getInitParameter("filterfelder").contains(field)) {
						result[i] = "x_" + field + ":\"" + term + "\"";
					} else {
						throw new IllegalArgumentException("Fehler beim Kompilieren der Anfrage: Filter Query Term " + field + " nicht unter den Filterfeldern.");
					}
				} else {
					throw new IllegalArgumentException("Fehler beim Kompilieren der Anfrage: Filterklausel " + result[i] + " hat falsches Format oder der Filterterm leer.");
				}				
			}
		} 
		return result;
	}	
	
	protected void transformRecords(final PrintWriter writer, final SolrDocumentList solrDocumentList, final Transformer transformer) 
		throws Exception {
		for (SolrDocument doc : solrDocumentList) {
			transformer.setParameter("likes", doc.getFieldValue("likes"));
			transformRecord(writer, doc, transformer);		
			transformer.reset();
			transformer.setParameter("trenner", ",");
		}		
	}
	
}
