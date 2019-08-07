package bsz.expo.trafo.servlet;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import bsz.expo.trafo.TrafoContext;

/**
 * ServletTrafoContext implementiert den Transformationskontext in einer JSF-Anwendnung.
 * <p>
 * ServletTrafoContext implementiert den Zugriff auf absolute Pfade zu eienr Datei, das konfigurierte Datenverzeichnis
 * sowie andere InitParameter. 
 * 
 * @author Christof Mainberger (christof.mainberger@bsz-bw.de) 
 *
 */
public class ServletTrafoContext extends TrafoContext {
	
	private final ServletContext servletContext; 
	private final String remoteUser;
	
	public ServletTrafoContext(HttpServletRequest req) {
		this.servletContext = req.getServletContext();
		this.remoteUser = req.getRemoteUser();
	}
	
	@Override
	public String getRealPath(final String path) { 
		return servletContext.getRealPath(path);
	}
	
	@Override
	public String getDataPath(final String path) {
		return servletContext.getRealPath("/WEB-INF/data/" + remoteUser + File.separator + path);
	}
	
	@Override
	public String getPipelinePath(final String path) {
		return servletContext.getRealPath("/WEB-INF/pipelines/" + path);
	}
	
	@Override
	public String getTempPath(final String path) {
		return servletContext.getRealPath("/WEB-INF/temp/" + remoteUser + File.separator + path);
	}
	
	@Override
	public String getInitParameter(final String key) {
		return servletContext.getInitParameter(key);
	}
	
	@Override
	public Object getAttribute(final String key) {
		return super.getAttribute(key) != null ? super.getAttribute(key) : servletContext.getAttribute(key);
	}
	
}
