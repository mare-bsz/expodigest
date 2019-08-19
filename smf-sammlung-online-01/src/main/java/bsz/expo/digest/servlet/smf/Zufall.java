package bsz.expo.digest.servlet.smf;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletConfig;
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
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import bsz.expo.Digest;

/**
 * Servlet implementation class Items
 */
@WebServlet("/random/*")
public class Zufall extends Digest {
	private static final long serialVersionUID = 1L;
	
	Random ran;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		ran = new Random();
		super.init(config);
	}
		
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build()) {
			
			final int fst = validNat(request.getParameter("fst"), 0);
			final int len = validNat(request.getParameter("len"), 10);
			final String fmt = validFormat(request.getParameter("fmt"), "json");
			
			final SolrQuery solrQuery = new SolrQuery();
			solrQuery.setStart(fst);
			solrQuery.setRows(len);
			solrQuery.setSort("random_" + ran.nextInt(9999), SolrQuery.ORDER.desc);
			solrQuery.setQuery("images:[* TO *]");
			
			final Templates templates = getTemplates(fmt);
			
			response.setContentType(templates.getOutputProperties().getProperty("media-type"));
			response.setCharacterEncoding("UTF-8");
			
			final SolrDocumentList  solrDocumentList = client.query(solrQuery).getResults();
			
			try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"))) {				
				if ("xml".equals(templates.getOutputProperties().getProperty("method"))) {
					writer.println("<?xml version=\"1.0\" ?>");
					writer.println("<result>");
					writer.println("<head>");					
					writer.println("<fst>" + fst + "</fst>");
					writer.println("<len>" + len + "</len>");
					writer.println("<fmt>" + fmt + "</fmt>");
					writer.println("</head>");
					transformRecords(writer, solrDocumentList, templates.newTransformer());					
					writer.println("</result>");
				} else {
					writer.println("{ \"head\" : {");
					writer.println(" \"numFound\" : \"" + solrDocumentList.getNumFound() + "\",");					
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