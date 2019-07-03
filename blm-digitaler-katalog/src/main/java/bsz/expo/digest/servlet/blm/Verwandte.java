package bsz.expo.digest.servlet.blm;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;

import bsz.expo.Digest;

/**
 * Servlet implementation class Aehnliche
 */
@WebServlet("/verwandte")
public class Verwandte extends Digest {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Verwandte() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build()) {
			
			final SolrDocument solrDocument = client.getById(validRequired(request.getParameter("id")));		
			
			StringBuilder solrQuery = new StringBuilder();
			for (String kriterium : getServletContext().getInitParameter("verwandschaft").split(",")) {				
				String[] paar = kriterium.split(":"); 
				String terme = fold(solrDocument.getFieldValues("f_" + paar[0]));
				if (terme != null) {
					if (solrQuery.length() > 0) {
						solrQuery.append(" OR ");
					}
					solrQuery.append("(f_" + paar[0] + ":" + terme + ")^" + paar[1]);
				}
			}				
			
			if (solrQuery.length() > 0) {
				final SolrQuery query2 = new SolrQuery();
				query2.setQuery("(" + solrQuery.toString() + ") NOT (id:" + request.getParameter("id") + ")");
				
				writeToResponse(response, query2, getTemplates("json").newTransformer());				
				
			} else {
				writeEmptyJson(response);
			}	
			
		} catch (IllegalArgumentException iae) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, iae.getMessage());
		} catch (TransformerConfigurationException tce) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, tce.getMessage());
		} catch (Exception se) {
			throw new ServletException(se);
		}
	}
	
	private String fold(Collection<Object> results) {
		if (results != null && results.size() > 0) {
			StringBuilder ergebnis = new StringBuilder();
			for (Object result : results) {
				ergebnis.append(ClientUtils.escapeQueryChars(result.toString()) + " ");
			}
			return ergebnis.toString();
		} else {
			return null;
		}
	}
	
	
}
