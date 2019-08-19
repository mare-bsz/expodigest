package bsz.expo.digest.servlet.smf;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import bsz.expo.Digest;
import bsz.expo.Util;

@WebServlet("/alben")
public class Alben extends Digest {
	private static final long serialVersionUID = 1L;
			
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {	
			
			final Album album = new Album(			
				validRequired(request.getParameter("id"), "Ohne id können Alben nicht angelegt werden"),
				validRequired(request.getParameter("title"), "Ohne title können Alben nicht angelegt werden"),
				request.getParameter("text")
			);						
						
			String payload = String.format("<record><title>%s</title><text>%s</text></record>", Util.toXML(album.title), Util.toXML(album.text));
						
			registerInSolr(getServletContext().getInitParameter("solrCoreUrl"), album, payload);
			registerInSolr(getServletContext().getInitParameter("solrAlbumUrl"), album, payload);
							
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"))) {		
				writer.write(String.format("{ \"id\":\"%s\",\"title\":\"%s\",\"text\":\"%s\"}", album.id, Util.toJson(album.title), Util.toJson(album.text)));
			}
				
		} catch (IllegalArgumentException iae) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, iae.getMessage());
		} catch (SolrServerException se) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, se.getMessage());
		} catch (Exception e) {
			throw new ServletException(e);		
		}	
	}
	
	/**
	 * @author mainberger
	 *
	 */
	private static class Album { 
		public final String id; 
		public final String title; 
		public final String text; 
		
		public Album(final String id, final String title, final String text) {
			this.id = id;
			this.title = title;
			this.text = text;
		}
	}

	void registerInSolr(String solrCoreUrl, final Album album, final String payload) 
		throws SolrServerException, IOException {
		try (SolrClient client = new HttpSolrClient.Builder(solrCoreUrl).build()) {			
			SolrInputDocument input = new SolrInputDocument();
			input.addField("id", album.id); 
			input.addField("display", "album");
			input.addField("likes", 0);
			setWert(input, "f_titel", album.title); 
			setWert(input, "_text_", album.text); 
			setWert(input, "payload", payload);
			client.add(input);
			client.commit();
		}
	}

	private void setWert(SolrInputDocument input, String name, final String inhalt) {
		HashMap<String, Object> value = new HashMap<String, Object>(); 
		value.put("set",inhalt); 
		input.addField(name, value);
	}
	
}
