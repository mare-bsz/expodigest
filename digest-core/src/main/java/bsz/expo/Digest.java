package bsz.expo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;

/**
 * Servlet implementation class Items
 */
public abstract class Digest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	TransformerFactory tFactory = null;
	String defaultSortierung = null;
	
	@Override
	public void init(ServletConfig config) throws ServletException {		
		tFactory = TransformerFactory.newInstance();
		if (config.getServletContext().getAttribute("templates") == null) {
			config.getServletContext().setAttribute("templates", new HashMap<String, Templates>());
		}
		defaultSortierung = config.getInitParameter("defaultSortierung") != null ? config.getInitParameter("defaultSortierung") : "entstehungszeit";
		super.init(config);
	}

	protected void transformRecords(final PrintWriter writer, final SolrDocumentList solrDocumentList, final Transformer transformer) 
		throws Exception {
		for (SolrDocument doc : solrDocumentList) {
			transformRecord(writer, doc, transformer);		
			transformer.reset();
			transformer.setParameter("trenner", ",");
		}		
	}
	
	protected void transformRecord(final Writer writer, final SolrDocument doc, final Transformer transformer) 
			throws Exception {					
		try {
			transformer.transform(
				new StreamSource(new StringReader(doc.getFieldValue("payload").toString())), 
				new StreamResult( writer ));
		} catch (TransformerException e) {
			throw new Exception("Datensatz mit id " + doc.getFieldValue("id") + " lässt sich nicht transformieren");
		}				
	}	
	
	protected Templates getTemplates(final String name, final String vorgabe) 
			throws TransformerConfigurationException, IOException, IllegalArgumentException {
		return (name == null || name.isEmpty()) ? getTemplates(vorgabe) : getTemplates(name);
	}
	
	protected Templates getTemplates(final String name) 
		throws TransformerConfigurationException, IOException, IllegalArgumentException {	
		@SuppressWarnings("unchecked")
		Map<String, Templates> templates = (Map<String, Templates>) getServletContext().getAttribute("templates");
		if (!templates.containsKey(name)) {
			templates.put(name, createTemplate(new File(getServletContext().getRealPath("WEB-INF/xsl/" + name + ".xsl"))));			
		} 		
		return templates.get(name);
	}
	
	private Templates createTemplate(File xsl) 
		throws TransformerConfigurationException, IOException, IllegalArgumentException  {
		try (InputStream streamSource = new FileInputStream(xsl)) {
			return tFactory.newTemplates(new StreamSource(streamSource));
		} catch (FileNotFoundException fnfe) {
			throw new IllegalArgumentException("Format " + xsl.getName() + " exisitiert nicht!");
		}
	}
	
	protected int validNat(final String parameter, int result) throws IllegalArgumentException {
		return validNat(parameter, result, "Parameter img: " + parameter + " muss eine Zahl größer Null sein!");
	}
	
	protected int validNat(final String parameter, int result, final String message) throws IllegalArgumentException {
		if (parameter != null) {
			try {
				result = Integer.parseInt(validRequired(parameter));
				if (result < 0) {					
					throw new IllegalArgumentException(message);
				}				
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(message);
			}
		} 
		return result;
	}
		
	protected String validRequired(final String parameter) throws IllegalArgumentException {
		return validRequired(parameter, "Parameter id darf nicht null oder leer sein!");
	}
	
	protected String validRequired(final String parameter, final String message) 
		throws IllegalArgumentException {
		if (parameter != null && !parameter.isEmpty()) {
			return parameter;
		} else {
			throw new IllegalArgumentException(message);
		}
	}
	
	protected String validSort(final String parameter) throws IllegalArgumentException {
		if (parameter == null) {
			return defaultSortierung;
		} else if (getServletContext().getInitParameter("sortierfelder").contains(parameter)) {
			return parameter;
		} else {
			throw new IllegalArgumentException("Parameter " + parameter + " ist nicht in den Sortierfeldern!");
		}
	}
	
	protected String validLang(final String parameter, String result) throws IllegalArgumentException {
		if (parameter != null) {
			if ("de".equals(parameter) || "en".equals(parameter) || "fr".equals(parameter)) {
				result = parameter;
			} else {
				throw new IllegalArgumentException("Lang-Parameter darf nur de, en oder fr sein.");
			}
		} 
		return result;
	}
	
	protected String validFacet(final String parameter) throws IllegalArgumentException {
		if (getServletContext().getInitParameter("filterfelder").contains(parameter)) {
			 return "x_" + parameter;
		} else {
			throw new IllegalArgumentException("Fehler beim Kompilieren der Anfrage: Facet Query Term " + parameter + " nicht unter den Filterfeldern.");
		}
	}
	
	protected String validFormat(final String parameter, final String vorgabe) throws IllegalArgumentException { 
		if (parameter != null) {
			if (new File(getServletContext().getRealPath("WEB-INF/xsl/" + parameter + ".xsl")).exists()) {
				return parameter;
			} else {
				throw new IllegalArgumentException("Fehler beim Kompilieren der Anfrage: Format " + parameter + " ist nicht vorhanden.");
			}
		}
		return vorgabe;		
	}	
	
	protected void writeToResponse(HttpServletResponse response, final SolrParams solrQuery, final Transformer transformer)
			throws IOException, Exception, UnsupportedEncodingException {
		
		try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build()) {
			
			response.setContentType(transformer.getOutputProperties().getProperty("media-type"));
			response.setCharacterEncoding("UTF-8");
			
			final SolrDocumentList solrDocumentList = client.query(solrQuery).getResults();
			
			try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"))) {
				
				if ("xml".equals(transformer.getOutputProperties().getProperty("method"))) {
					writer.println("<?xml version=\"1.0\" ?>");
					writer.println("<result>");
					writer.println("<head>");
					writer.println("<query>" + solrQuery.toString() + "</query>");
					writer.println("<numFound>" + solrDocumentList.getNumFound() + "</numFound>");
					writer.println("</head>");
					transformRecords(writer, solrDocumentList, transformer);					
					writer.println("</result>");
				} else {
					writer.println("{ \"head\" : {");
					writer.println(" \"query\" : \"" + Util.toJson(solrQuery.toString()) + "\",");
					writer.println(" \"numFound\" : \"" + solrDocumentList.getNumFound() + "\"");
					writer.println("},");
					writer.println("\"records\" : ");
					writer.println("[");
					transformRecords(writer, solrDocumentList, transformer);
					writer.println("]");
					writer.println("}");
				}				
			}
		} catch (SolrServerException se) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, se.getMessage());
		}
	}
	
	protected void writeEmptyJson(HttpServletResponse response)
			throws IOException, Exception, UnsupportedEncodingException {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
			
		try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"))) {
			writer.println("{ \"head\" : {");
			writer.println(" \"numFound\" : \"0\"");
			writer.println("}");				
		} 
	}
	
}
