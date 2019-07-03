package bsz.expo.digest.servlet;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 * Servlet implementation class Items
 */
@WebServlet("/liste")
public class Liste extends Export {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException {
		
		try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build()) {
									
			final String selektQuery = (request.getParameter("term1") == null || request.getParameter("term1").isEmpty()) ? "" :
				kombiniere("", request.getParameter("index1"), request.getParameter("op1"), request.getParameter("term1"),
					kombiniere(request.getParameter("vk2"), request.getParameter("index2"), request.getParameter("op2"), request.getParameter("term2"),
						kombiniere(request.getParameter("vk3"), request.getParameter("index3"), request.getParameter("op3"), request.getParameter("term3"),"")));	
			
			
			final SolrQuery solrQuery  = new SolrQuery(); 
			solrQuery.setStart(validNat(request.getParameter("fst"), 0));
			solrQuery.setRows(validNat(request.getParameter("len"), 10));
			solrQuery.setSort(validSort(request.getParameter("srt"), "s_entstehungszeit"), SolrQuery.ORDER.asc);
			solrQuery.setQuery(compileQuery(selektQuery, "*:*"));				
									
			final SolrDocumentList solrDocumentList = client.query(solrQuery).getResults();
			final List<Treffer> treffer = new ArrayList<Treffer>();	
			final Transformer headerTransfomer = getTemplates("header").newTransformer();
			final Transformer detailTransfomer = getTemplates("detail").newTransformer();
			StringWriter header = new StringWriter();
			StringWriter detail = new StringWriter();
			for (SolrDocument doc : solrDocumentList) {
				transformRecord(header, doc, headerTransfomer);
				transformRecord(detail, doc, detailTransfomer);
				doc.getFieldValues("images");
				final List<String> images = new ArrayList<String>();
				if (doc.getFieldValues("images") != null) {
					for (Object image : doc.getFieldValues("images")) {
						images.add(image.toString());
					}
				}
				treffer.add(new Treffer(doc.getFieldValue("id").toString(), header.toString(), detail.toString(), images));
				header.getBuffer().setLength(0);
				detail.getBuffer().setLength(0);
			}
			
			request.setAttribute("reccount", solrDocumentList.getNumFound());
			request.setAttribute("treffer", treffer);
			request.setAttribute("solrQuery", solrQuery.getQuery());
			request.setAttribute("selektQuery", selektQuery);
			
			List<SimpleEntry<String, String>> templates = new ArrayList<SimpleEntry<String, String>>();
			for (String templateFilename : new File(getServletContext().getRealPath("WEB-INF/xsl")).list()) {
				templates.add(new SimpleEntry<String, String>(templateFilename.replaceFirst(".xsl$", ""), templateFilename));
			}
			request.setAttribute("templates", templates);				 	
			
			request.getRequestDispatcher("index.jsp").forward(request, response);	
			
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
	
	private String kombiniere(final String vk, final String index, final String op, final String term, final String right) {
		return (term != null && ! term.isEmpty()) ? (" " + vk + " " + klausel(index, op, term) + right).trim() : "";		
	}
	
	private String klausel(final String index, final String op, final String term) {
		return index + " " + op + " " + fmtTerm(op, term);
	}
	
	private String fmtTerm(String op, String src) {
		return ("all".equals(op) || "any".equals(op) || "adj".equals(op)) ? "\"" + src + "\"" : src;		
	}	
	
	public static class Treffer {
		
		final String id;
		final String header;
		final String detail;
		final List<String> images;
		public Treffer(final String id, final String header, final String detail, final List<String> images) {
			this.id = id;
			this.header = header;
			this.detail = detail;
			this.images = images;
		}
		
		public String getId() { return id; }
		public String getHeader() { return header; }
		public String getDetail() { return detail;}
		public List<String> getImages() { return images; }

	}
}
