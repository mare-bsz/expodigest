package bsz.expo.trafo.servlet;

import static java.lang.Math.toIntExact;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bsz.expo.trafo.TrafoException;

public class Delivery extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
					
			Path temp = findTempFile(req.getParameter("result"), req.getRemoteUser());				
			
			setResponseHeaders(resp, temp, req.getParameter("encoding"));
			
			writeTempToReponse(resp, temp);			
			
		} catch (TrafoException e) {
			resp.sendError(400, e.getMessage());		
		}
	}

	private void setResponseHeaders(final HttpServletResponse resp, final Path temp, final String encoding) throws IOException {
		resp.setContentType(Files.probeContentType(temp));
		resp.setHeader("Content-Disposition", "attachment; filename=\"" + temp.getFileName() + "\"");	
		resp.setContentLength(toIntExact(Files.size(temp)));
		resp.setCharacterEncoding(encoding);
	}

	private void writeTempToReponse(HttpServletResponse resp, Path temp) throws IOException, FileNotFoundException {
		final OutputStream out = resp.getOutputStream();
		final FileInputStream input = new FileInputStream(temp.toFile());
		final FileChannel channel = input.getChannel();
		byte[] buffer = new byte[256 * 1024];
		final ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		try {
		    for (int length = 0; (length = channel.read(byteBuffer)) != -1;) {
		       out.write(buffer, 0, length);
		       byteBuffer.clear();
		    }
		} finally {
		    input.close();
		}
	}	
	
	private Path findTempFile(final String name, final String remoteUser) throws TrafoException {
		if (name != null && !name.isEmpty()) {			
			return Paths.get(getServletContext().getRealPath("/WEB-INF/temp/" + remoteUser + "/" + name));			
		} else {
			throw new TrafoException("Name des Tempfiles ist Null oder leer");
		}		
	}
}
