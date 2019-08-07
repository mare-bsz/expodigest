package bsz.expo.trafo.servlet;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import bsz.expo.trafo.TrafoConfig;
import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoPipeline;
import bsz.expo.trafo.TrafoTicket;

public class Run extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
		try {
			
			if (req.getParameter("username") != null && req.getParameter("password") != null) {
				req.login(req.getParameter("username"), req.getParameter("password"));				
			}
								
			final TrafoPipeline trafoPipeline = createPipeline(req);			
			
			if (!trafoPipeline.getConfiguration().isEmpty()) {
				req.getRequestDispatcher("/config.jsp").forward(req, resp);								
			} else {
				for (String param : req.getParameterMap().keySet()) {
					trafoPipeline.addParameter(param, req.getParameter(param));
				}
				runPipeline(trafoPipeline);
				req.getRequestDispatcher("/result.jsp").forward(req, resp);
				req.logout();
			}			
			
		} catch (TrafoException e) {
			resp.sendError(400, e.getMessage());
		}
	}	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		try {						
			
			final TrafoPipeline trafoPipeline = createPipeline(req);			
			configPipeline(trafoPipeline, req);			
			runPipeline(trafoPipeline);
			
			req.getRequestDispatcher("/result.jsp").forward(req, resp);	
			
		} catch (TrafoException e) {
			resp.sendError(400, e.getMessage());
		}
	}
	
	private TrafoPipeline createPipeline(HttpServletRequest req) throws TrafoException {
		final File pipelineFile = findPipelineFile(req.getParameter("trafo"), req.getRemoteUser());
		final TrafoPipeline trafoPipeline = TrafoPipeline.createTrafoPipeline(pipelineFile, new ServletTrafoContext(req));
		req.setAttribute("pipeline", trafoPipeline);
		req.getSession().setAttribute("results", trafoPipeline.getResults());
		return trafoPipeline;		
	}
	
	private File findPipelineFile(final String pipelineName, final String remoteUser) throws TrafoException {
		if (pipelineName != null && !pipelineName.isEmpty()) {
			return new File(getServletContext().getRealPath("/WEB-INF/pipelines/" + remoteUser + "/" + pipelineName));			
		} else {
			throw new TrafoException("Pipeline-Name ist Null oder leer");
		} 	
	}

	private void runPipeline(final TrafoPipeline trafoPipeline) throws TrafoException {
		trafoPipeline.init();
		trafoPipeline.process(new TrafoTicket());
		trafoPipeline.finit();
	}
	
	private void configPipeline(final TrafoPipeline trafoPipeline, final HttpServletRequest request) 
		throws ServletException, IOException, TrafoException {
		
		for (TrafoConfig config : trafoPipeline.getConfiguration()) {			
			if ("text".equals(config.getTyp())) {				
				config.setValue(IOUtils.toString(request.getPart(config.getName()).getInputStream(), "UTF-8"));				
			} else if ("file".equals(config.getTyp())) {
				config.setValue(request.getPart(config.getName()));				
			} else if ("datum".equals(config.getTyp())) {							
				config.setValue(request.getParameter(config.getName()));				
			} else if ("zahl".equals(config.getTyp())) {
				config.setValue(request.getParameter(config.getName()));
			} else if ("menu".equals(config.getTyp())) {
				config.setValue(request.getParameter(config.getName()));
			} else {
				throw new TrafoException("Unbekannter Trafo-Config-Typ: " + config.getTyp());
			}
		}		
	}
}
