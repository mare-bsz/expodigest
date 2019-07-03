package bsz.expo.digest.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 * Servlet implementation class Items
 */
public abstract class Digest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	TransformerFactory tFactory = null;
	
	@Override
	public void init(ServletConfig config) throws ServletException {		
		tFactory = TransformerFactory.newInstance();
		if (config.getServletContext().getAttribute("templates") == null) {
			config.getServletContext().setAttribute("templates", new HashMap<String, Templates>());
		}
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
	
	protected Templates getTemplates(final String name) throws Exception {	
		@SuppressWarnings("unchecked")
		Map<String, Templates> templates = (Map<String, Templates>) getServletContext().getAttribute("templates");
		if (!templates.containsKey(name)) {
			File xslFile = new File(getServletContext().getRealPath("WEB-INF/xsl/" + name + ".xsl"));
			if (xslFile.exists()) {
				templates.put(name, createTemplate(xslFile));
			} else {
				throw new IllegalArgumentException("Format " + name + " exisitiert nicht!");
			}
		} 		
		return templates.get(name);
	}
	
	private Templates createTemplate(File xsl) throws Exception {
		try (InputStream streamSource = new FileInputStream(xsl)) {
			return tFactory.newTemplates(new StreamSource(streamSource));
		} catch (Exception e) {
			throw new Exception("Stylesheet-File " + xsl.getName() + ".xsl lässt sich nicht kompilieren.");
		}
	}
	
	protected int validNat(final String parameter, int result) throws IllegalArgumentException {
		if (parameter != null) {
			try {
				result = Integer.parseInt(parameter);
				if (result < 0) {					
					throw new IllegalArgumentException("Parameter img: " + parameter + " muss größer Null sein!");
				}				
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Parameter img: " + parameter + " ist keine Zahl!");
			}
		} 
		return result;
	}
	
}
