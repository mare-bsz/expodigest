package bsz.expo.trafo.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;

import bsz.expo.trafo.TrafoConfig;
import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoPipeline;
import bsz.expo.trafo.TrafoTicket;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;

public class RunSession extends HttpServlet {
		
		private static final long serialVersionUID = 1L;
									
		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		
			System.out.println(":::::::::::::::::::::::::::::::::" + req.getPathInfo());
			
			final Writer wrt = resp.getWriter();
			final HttpSession session = req.getSession();
			
			
			try {
				if (req.getPathInfo().endsWith("init")) {
					try (BufferedReader reader = req.getReader()) {
//						sendNote("ExpoTransfer gestartet", "TrafoPipeline " + req.getParameter("pipeline") + " " + reader.readLine());
					}
					String pipelineDefinition = req.getParameter("pipeline");
					System.out.println("RunSession: init: " + pipelineDefinition);
					final File pipelineFile = new File(getServletContext().getRealPath("/WEB-INF/pipelines/" + req.getParameter("pipeline")));
					final TrafoPipeline trafoPipeline = TrafoPipeline.createTrafoPipeline(pipelineFile, new ServletTrafoContext(req));
					configPipeline(trafoPipeline, req);	
					trafoPipeline.init();					
					session.setAttribute("pipeline", trafoPipeline);
					wrt.append(session.getId());					
				} else if (req.getPathInfo().endsWith("finit")) {
					try (BufferedReader reader = req.getReader()) {
						final TrafoPipeline trafoPipeline = (TrafoPipeline) session.getAttribute("pipeline");
						if (trafoPipeline != null) {
							trafoPipeline.finit();
							req.getSession().removeAttribute("pipeline");
							//sendSFTP();
//							sendNote("ExpoTransfer beendet: " + reader.readLine(), "TrafoPipeline");
						} else {
							System.out.println("Expotransfer beenden: trafoPipeline konnte nicht ermittelt werden!");
						}
					} 
				} else if (req.getPathInfo().endsWith("process")){
					System.out.println("RunSession: process: " + session.getId());
					final TrafoPipeline trafoPipeline = (TrafoPipeline) session.getAttribute("pipeline");
					if (trafoPipeline != null) {
						try (InputStream inputStream = req.getInputStream()) {							
							final Builder parser = new Builder();
							final Document doc = parser.build(inputStream);											
							final TrafoTicket trafoticket = new TrafoTicket();
							trafoticket.setDocument(doc);
							trafoPipeline.process(trafoticket);												
						} catch (ParsingException ex) {
							  System.err.println("Expotransfer Verarbeitung: trafoPipeline konnte nicht ermittelt werden!");
						}
					} else {
						System.out.println("Expotransfer Verarbeitung: trafoPipeline konnte nicht ermittelt werden!");
					}
				} else {
					System.out.println("Expotransfer Verarbeitung: Unbekannter Aufruf!" + req.getPathInfo());
				}
				resp.flushBuffer();
				wrt.close();
			} catch (Exception e) {
				System.out.println("Exeption beim Verarbeitung von " + req.getPathInfo().substring(1));
				e.printStackTrace();
			}					
		}
		
//		private void sendNote(final String subject, final String text) throws MessagingException {
//			Properties props = new Properties();
//			InternetAddress fromAddress = new InternetAddress("transfer@swbtrafo.bsz-bw.de");
//	        props.put("mail.smtp.host", getServletContext().getInitParameter("smtpHost"));
//			Session session = Session.getInstance(props, null);
//	        MimeMessage msg = new MimeMessage(session);
//	        msg.setFrom(fromAddress);        
//	        msg.setRecipients(Message.RecipientType.TO, getServletContext().getInitParameter("admin"));
//	        msg.setSubject(subject);
//	        msg.setSentDate(new Date());	        	        
//	        msg.setText(text);
//            Transport.send(msg);
//		}
		
//		private void sendSFTP() throws Exception {			
//				
//			JSch jsch = new JSch();
//			com.jcraft.jsch.Session session = null;
//	        try {
//	        	session = jsch.getSession("blm", "bszftp.bsz-bw.de", 22);
//	            session.setConfig("StrictHostKeyChecking", "no");
//	            session.setPassword("Start#BLM");
//	            session.connect();
//
//	            Channel channel = session.openChannel("sftp");
//	            channel.connect();
//	            ChannelSftp sftpChannel = (ChannelSftp) channel;            	
//		    	try (InputStream fis = new FileInputStream(getServletContext().getRealPath("out/aib.xml"))) {
//		    	  sftpChannel.put(fis, "aib.xml");
//		    	}		    
//	            sftpChannel.exit();
//	            session.disconnect();
//	        } catch (JSchException e) {
//	        	throw new TrafoException(e.getMessage());
//	        } catch (SftpException e) {
//	        	throw new TrafoException(e.getMessage());
//	        } catch (IOException io) {
//	        	throw new TrafoException(io.getMessage());
//	        } 
//	 	}
	
		
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
