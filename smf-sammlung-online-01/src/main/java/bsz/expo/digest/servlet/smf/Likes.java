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
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import bsz.expo.Digest;

@WebServlet(urlPatterns = {"/likes", "/likes/inc", "/likes/dec"})
public class Likes extends Digest {
	private static final long serialVersionUID = 1L;
			
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		
		try (SolrClient objectClient = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build();
			SolrClient likesClient = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrLikesUrl")).build()) {
			
			final String id = validRequired(request.getQueryString(), "Ohne id können Likes nicht abgerufen, angelegt oder gelöscht werden");
			
			SolrDocument doc = likesClient.getById(id);
			int likes = (doc == null) ? 0 : Integer.parseInt(doc.getFieldValue("like").toString());					
			
			if ("/likes/inc".equals(request.getServletPath())) {
				updateCore(likesClient, prepareLikesCoreInput(id, ++likes));
				updateCore(objectClient, prepareObjectCoreInput(id, likes));
			} else if ("/likes/dec".equals(request.getServletPath())) {
				if (likes > 1) {
					updateCore(likesClient, prepareLikesCoreInput(id, --likes));
					updateCore(objectClient, prepareObjectCoreInput(id, likes));				
				} else if (likes == 1) {
					likesClient.deleteById(id);
					updateCore(objectClient, prepareObjectCoreInput(id, 0));
				}				
			}		          
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"))) {		
				writer.write(String.format("{ \"id\":\"%s\",\"likes\":\"%s\"}",id, likes));
			}
				
		} catch (IllegalArgumentException iae) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, iae.getMessage());
		} catch (SolrServerException se) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, se.getMessage());
		} catch (Exception e) {
			throw new ServletException(e);		
		}	
	}

	private void updateCore(final SolrClient client, final SolrInputDocument input)
			throws SolrServerException, IOException {
		client.add(input);
		client.commit();		
	}

	SolrInputDocument prepareLikesCoreInput(final String id, int likes) {
		SolrInputDocument input = new SolrInputDocument();
		input.addField("id", id); 
		setValue("like", likes, input);
		return input;
	}	
	
	SolrInputDocument prepareObjectCoreInput(final String id, int likes) {
		SolrInputDocument input = new SolrInputDocument();
		input.addField("id", id); 
		setValue("like", likes, input);
		setValue("s_likes", likes, input);
		return input;
	}
	
	void setValue(final String name, int likes, SolrInputDocument input) {
		HashMap<String, Object> value = new HashMap<String, Object>(); 
		value.put("set",likes); 
		input.addField(name, value);
	}		
}
