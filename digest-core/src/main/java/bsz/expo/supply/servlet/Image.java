package bsz.expo.supply.servlet;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;
import org.imgscalr.Scalr;
	
	@WebServlet("/image")
	public class Image extends Pdf {
		private static final long serialVersionUID = 1L;
		
		private final Map<String, ByteArrayOutputStream> imageCache = Collections.synchronizedMap(new LRUMap<String, ByteArrayOutputStream>(100));		
		   	
	    /**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
						
			try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build()) {				
				
				final String id = validRequired(request.getParameter("id"));
				final int pos = validNat(request.getParameter("img"), 0);
				final int width = validNat(request.getParameter("width"), 100);
				final char mode = validMode(request.getParameter("mode"), 'x');
														
				if (!imageCache.containsKey(id + ":" + pos + ":" + width + ":" + mode)) {	
					ByteArrayOutputStream image = new ByteArrayOutputStream();
					SolrDocument doc = client.getById(id);			
					if (doc != null) {								
						final Collection<Object> bildpfade = doc.getFieldValues("images");
						if (bildpfade != null && bildpfade.size() >= pos) {
							String bildPfad = getServletContext().getInitParameter("imagePath") + ((String) bildpfade.toArray()[pos]).replaceAll("\\\\", "/");
							if (width > 0) {
								ImageIO.write(Scalr.resize(ImageIO.read(new FileInputStream(bildPfad)), getMode(mode), width), "JPEG", image);
							} else {
								copyFile(image, bildPfad);
							}
						} else {
							throw new IllegalArgumentException("Keine Bilder zu " + id + " und Position " + pos + " gefunden.");
						}
					} else {
						throw new IllegalArgumentException("Keine Daten zu " + id + " gefunden.");
					}
					imageCache.put(id + ":" + pos + ":" + width + ":" + mode, image);
				}
				
				response.setContentType("image/jpeg");	
				response.getOutputStream().write(imageCache.get(id + ":" + pos + ":" + width + ":" + mode).toByteArray());
				
			} catch (IllegalArgumentException iae) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, iae.getMessage());
			} catch (SolrServerException se) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, se.getMessage());
			} catch (Exception e) {
				throw new ServletException(e);		
			}
		}	
		
		private char validMode(final String parameter, final char vorgabe) throws IllegalArgumentException {
			if (parameter != null && !parameter.isEmpty()) {
				if ("whx".indexOf(parameter.charAt(0)) != -1) {
					return parameter.charAt(0);
				} else {
					throw new IllegalArgumentException("Parameter mode w, h oder x oder leer!");
				}
			} else {
				return vorgabe;
			}
		}	
				
		private Scalr.Mode getMode(char imgMode) {
			switch (imgMode) {
			case 'w' : return Scalr.Mode.FIT_TO_WIDTH;
			case 'h' : return Scalr.Mode.FIT_TO_HEIGHT;
			default : return Scalr.Mode.AUTOMATIC;
			}			
		}		
		
	}


