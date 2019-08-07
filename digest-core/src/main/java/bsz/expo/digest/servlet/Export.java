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
import javax.xml.transform.TransformerConfigurationException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocumentList;

import bsz.expo.Digest;
import bsz.expo.Util;
import bsz.expo.digest.query.SelektQL2Solr;
import bsz.expo.digest.query.SelektQLErrorListener;
import bsz.expo.digest.query.SelektQLLexer;
import bsz.expo.digest.query.SelektQLParser;

/**
 * Servlet implementation class Items
 */
@WebServlet("/export/*")
public class Export extends Digest {
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
		
		try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build()) {
			
			final int fst = validNat(request.getParameter("fst"), 0);
			final int len = validNat(request.getParameter("len"), 10);
			final String srt = validSort(request.getParameter("srt"));
			final String fmt = validFormat(request.getParameter("fmt"), "json");			
			
			final SolrQuery solrQuery = new SolrQuery();
			solrQuery.setStart(fst);
			solrQuery.setRows(len);
			solrQuery.setSort("s_" + srt, SolrQuery.ORDER.asc);
			solrQuery.setQuery(compileQuery(request.getParameter("qry"), "*:*"));
			
			final Templates templates = getTemplates(fmt);
			
			response.setContentType(templates.getOutputProperties().getProperty("media-type"));
			response.setCharacterEncoding("UTF-8");
			
			final SolrDocumentList  solrDocumentList = client.query(solrQuery).getResults();
			
			try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"))) {				
				if ("xml".equals(templates.getOutputProperties().getProperty("method"))) {
					writer.println("<?xml version=\"1.0\" ?>");
					writer.println("<result>");
					writer.println("<head>");
					writer.println("<numFound>" + solrDocumentList.getNumFound() + "</numFound>");
					writer.println("<qry>" + request.getParameter("qry") + "</qry>");
					writer.println("<srt>" + srt + "</srt>");
					writer.println("<fst>" + fst + "</fst>");
					writer.println("<len>" + len + "</len>");
					writer.println("<fmt>" + fmt + "</fmt>");
					writer.println("</head>");
					transformRecords(writer, solrDocumentList, templates.newTransformer());					
					writer.println("</result>");
				} else {
					writer.println("{ \"head\" : {");
					writer.println(" \"numFound\" : \"" + solrDocumentList.getNumFound() + "\",");
					writer.println(" \"qry\" : \"" + Util.toJson(request.getParameter("qry")) + "\",");
					writer.println(" \"srt\" : \"" + srt + "\",");
					writer.println("\"fst\" : \"" + fst + "\",");
					writer.println("\"len\" : \"" + len + "\",");
					writer.println("\"fmt\" : \"" + fmt + "\"");
					writer.println("},");
					writer.println("\"records\" : ");
					writer.println("[");
					transformRecords(writer, solrDocumentList, templates.newTransformer());
					writer.println("]");
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
	
	protected String compileQuery(final String query, String result) throws IllegalArgumentException {
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
	
		
}
