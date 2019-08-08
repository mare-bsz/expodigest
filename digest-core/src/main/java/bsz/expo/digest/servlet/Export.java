package bsz.expo.digest.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

import bsz.expo.digest.Digest;
import bsz.expo.digest.query.SelektQL2Solr;
import bsz.expo.digest.query.SelektQLErrorListener;
import bsz.expo.digest.query.SelektQLLexer;
import bsz.expo.digest.query.SelektQLParser;
import bsz.expo.trafo.TrafoPipeline;

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
		
		try {
			
			final String fst = validNumber(request.getParameter("fst"), "0");
			final String len = validNumber(request.getParameter("len"), "10");
			final String srt = validSort(request.getParameter("srt"));
			final String fmt = validFormat(request.getParameter("fmt"), "json");			
			
			final SolrQuery solrQuery = new SolrQuery();
			solrQuery.setStart(Integer.parseInt(fst));
			solrQuery.setRows(Integer.parseInt(len));
			solrQuery.setSort("s_" + srt, SolrQuery.ORDER.asc);
			solrQuery.setQuery(compileQuery(request.getParameter("qry"), "*:*"));
					
			final TrafoPipeline pipeline = getPipeline(fmt);
			pipeline.addParameter("fst",fst);
			pipeline.addParameter("len",len);
			pipeline.addParameter("srt",srt);
			pipeline.addParameter("fmt",fmt);
			pipeline.addParameter("qry",request.getParameter("qry"));
			pipeline.addParameter("slr",solrQuery.getQuery());
			
			pipeline.setAttribute("response", response);				
			
			queryAndAnswer(solrQuery, pipeline);
							
		} catch (IllegalArgumentException iae) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, iae.getMessage());
		} catch (SolrServerException se) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, se.getMessage());
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
