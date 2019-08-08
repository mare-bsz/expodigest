package bsz.expo.supply.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;

import bsz.expo.digest.Digest;
	
	@WebServlet("/pdf")
	public class Pdf extends Digest {
		private static final long serialVersionUID = 1L;		
		   	
	    /**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
						
			try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build()) {				
				
				final String id = validRequired(request.getParameter("id"));
				final int pos = validNat(request.getParameter("pos"), 0);				
								
				response.setContentType("application/pdf");	
										
				SolrDocument doc = client.getById(id);			
				if (doc != null) {								
					final Collection<Object> pdfs = doc.getFieldValues("docs");
					if (pdfs != null && pdfs.size() > pos) {
						String docPfad = getServletContext().getInitParameter("imagePath") + ((String) pdfs.toArray()[pos]).replaceAll("\\\\", "/");
						copyFile(response.getOutputStream(), docPfad);							
					} else {
						throw new IllegalArgumentException("Keine PDFs zu " + id + " und Position " + pos + " gefunden.");
					}
				} else {
					throw new IllegalArgumentException("Keine Daten zu " + id + " gefunden.");
				}					
				
			} catch (IllegalArgumentException iae) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, iae.getMessage());
			} catch (SolrServerException se) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, se.getMessage());
			} catch (Exception e) {
				throw new ServletException(e);		
			}
		}	
		
		protected void copyFile(OutputStream image, String pfad) throws IOException, FileNotFoundException {
			try (FileInputStream fis = new FileInputStream(pfad)) {
				final FileChannel channel = fis.getChannel();
				byte[] buffer = new byte[256 * 1024];
				final ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
				for (int length = 0; (length = channel.read(byteBuffer)) != -1;) {
			       image.write(buffer, 0, length);
			       byteBuffer.clear();
				}
			}
		}
		
	}


