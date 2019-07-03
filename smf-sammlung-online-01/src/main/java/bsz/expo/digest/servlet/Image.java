package bsz.expo.digest.servlet;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;
import org.imgscalr.Scalr;
	
	@WebServlet("/image")
	public class Image extends Digest {
		private static final long serialVersionUID = 1L;
		
		private final Map<String, ByteArrayOutputStream> imageCache = Collections.synchronizedMap(new LRUMap<String, ByteArrayOutputStream>(100));		
		   	
	    /**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
						
			try {				
				final String id = request.getParameter("id");
				final int pos = validNat(request.getParameter("img"), 0);
				final int width = validNat(request.getParameter("width"), 100);
				final char mode = request.getParameter("mode") != null ? request.getParameter("mode").charAt(0) : 'x';
								
				response.setContentType("image/jpeg");
				
				ByteArrayOutputStream image = new ByteArrayOutputStream();							
				if (imageCache.containsKey(id + ":" + pos + ":" + width + ":" + mode)) {	
					image = imageCache.get(id + ":" + pos + ":" + width + ":" + mode);
				} else {
					try (SolrClient client = new HttpSolrClient.Builder(getServletContext().getInitParameter("solrCoreUrl")).build()) {						
						SolrDocument doc = client.getById(request.getParameter("id"));			
						if (doc != null) {								
							final Collection<Object> bildpfade = doc.getFieldValues("images");
							if (bildpfade != null && bildpfade.size() >= pos) {
								String bildPfad = getServletContext().getInitParameter("imagePath") + ((String) bildpfade.toArray()[pos]).replaceAll("\\\\", "/");
								//System.out.println("SKK#Sammlung-online#01: Image:pos: " + bildPfad);
								if (width > 0) {
									ImageIO.write(Scalr.resize(ImageIO.read(new FileInputStream(bildPfad)), getMode(mode), width), "JPEG", image);
								} else {
									try (FileInputStream fis = new FileInputStream(bildPfad)) {
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
						} else {
							throw new IllegalArgumentException("Keine Daten zu " + id + " gefunden.");
						}						
					}
					imageCache.put(id + ":" + pos + ":" + width + ":" + mode, image);
				}
				response.getOutputStream().write(image.toByteArray());
				
			} catch (IllegalArgumentException se) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, se.getMessage());
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
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


