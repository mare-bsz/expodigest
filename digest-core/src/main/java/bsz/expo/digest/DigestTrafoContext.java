package bsz.expo.digest;

import javax.servlet.ServletContext;

import bsz.expo.trafo.TrafoContext;

public class DigestTrafoContext extends TrafoContext {
	
	private final ServletContext servletContext;
	
	public DigestTrafoContext(final ServletContext context) {
		this.servletContext = context;
	}

	@Override
	public String getRealPath(String path) {
		return servletContext.getRealPath(path);
	}

	@Override
	public String getDataPath(String path) {
		return servletContext.getRealPath("/WEB-INF/data/" + path);
	}

	@Override
	public String getPipelinePath(String path) {
		return servletContext.getRealPath("/WEB-INF/pipelines/" + path);
	}

	@Override
	public String getTempPath(String path) {
		return servletContext.getRealPath("/WEB-INF/temp/" + path);
	}

	@Override
	public String getInitParameter(String key) {
		return servletContext.getInitParameter(key);
	}

}
