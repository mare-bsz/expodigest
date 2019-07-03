package bsz.expo.digest.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Templates;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrDocumentList;

import de.jotwerk.Util;
import de.jotwerk.selekt.query.SelektQL2Solr;
import de.jotwerk.selekt.query.SelektQLErrorListener;
import de.jotwerk.selekt.query.SelektQLLexer;
import de.jotwerk.selekt.query.SelektQLParser;

/**
 * Servlet implementation class Items
 */
@WebServlet("/selekt")
public class Selekt extends Digest {
	private static final long serialVersionUID = 1L;
	
	SelektQL2Solr selektQL2Solr;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		selektQL2Solr = new SelektQL2Solr(config.getServletContext().getInitParameter("indexfelder"));
		super.init(config);
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		final SolrQuery solrQuery  = new SolrQuery(); 
		Templates templates = null;
		String[] facets = null;
				
		try {
			
			solrQuery.setStart(validNat(request.getParameter("fst"), 0));
			solrQuery.setRows(validNat(request.getParameter("len"), 10));
			solrQuery.setSort(validSort(request.getParameter("srt"), "s_entstehungszeit"), SolrQuery.ORDER.asc);
			solrQuery.setQuery(compileQuery(request.getParameter("qry"), "*:*"));
			solrQuery.setFilterQueries(compileFilter(request.getParameter("flt"),new String[]{"display:browse"}));
			if (request.getParameter("fct") != null && !request.getParameter("fct").isEmpty()) {
				solrQuery.setFacet(true);
				facets = request.getParameter("fct").split(";");
				for (String facet : facets) {
					if (getServletContext().getInitParameter("filterfelder").contains(facet)) {
						solrQuery.addFacetField("x_" + facet);
					} else {
						throw new IllegalArgumentException("Fehler beim Kompilieren der Anfrage: Filter Query Term " + facet + " nicht unter den Filterfeldern.");
					}
				}				
				solrQuery.setFacetSort("index");
				solrQuery.setFacetMinCount(1);
				solrQuery.setFacetLimit(50000);				
			}
			
			templates = getTemplates(request.getParameter("fmt") != null ? request.getParameter("fmt") : "json");
			
		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (Exception se) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, se.getMessage());
		}
							
		try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build()) { 
				
			final QueryResponse solrResponse = client.query(solrQuery);
				
			final SolrDocumentList solrDocumentList = solrResponse.getResults();								
				
				response.setContentType(templates.getOutputProperties().getProperty("media-type"));
				response.setCharacterEncoding("UTF-8");
				
				try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"))) {
				
					if ("xml".equals(templates.getOutputProperties().getProperty("method"))) {
						writer.println("<?xml version=\"1.0\" ?>");
						writer.println("<result>");
						writer.println("<head>");
						writer.println("<query>" + solrQuery.getQuery() + "</query>");	
						writer.println("<filters>" + request.getParameter("flt") + "</filters>");
						writer.println("<facets>" + request.getParameter("fct") + "</facets>");
						writer.println("<numFound>" + solrDocumentList.getNumFound() + "</numFound>");
						writer.println("<start>" + request.getParameter("fst") + "</start>");
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
						writer.println(" \"query\" : \"" + Util.toJson(solrQuery.getQuery()) + "\",");
						writer.println("\"filters\" : \"" + request.getParameter("flt") + "\",");
						writer.println("\"facets\" : \"" + request.getParameter("fct") + "\",");						
						writer.println(" \"numFound\" : \"" + solrDocumentList.getNumFound() + "\",");
						writer.println("\"start\" : \"" + request.getParameter("fst") + "\",");
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
			}
		 catch (Exception se) {
			throw new ServletException(se);
		}	
	}
	
	protected String compileQuery(final String query, String result) throws IllegalArgumentException {
		
		//System.out.println("SKK#Sammlung-online#01: Export#compileQuery: " + query);
		
		if (query != null && ! query.isEmpty()) {
			final SelektQLErrorListener selektQLErrorListener = new SelektQLErrorListener();
			SelektQLLexer lexer = new SelektQLLexer(CharStreams.fromString(query));
			SelektQLParser parser = new SelektQLParser(new CommonTokenStream(lexer));
			parser.removeErrorListeners();
			parser.addErrorListener(selektQLErrorListener);
			result = selektQL2Solr.visit(parser.anfrage());
			if (selektQLErrorListener.error) {
				throw new IllegalArgumentException("Fehler beim Kompilieren der Anfrage " + query);
			}
		} 
		return result;
	}
	
	protected String[] compileFilter(final String query, String[] result) throws IllegalArgumentException {
		
		//System.out.println("SKK#Sammlung-online#01: Export#compileQuery: " + query);
		
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
