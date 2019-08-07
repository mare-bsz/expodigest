package bsz.expo.trafo.pipes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoPipe;
import bsz.expo.trafo.TrafoResult;

public class SftpPipe  extends TrafoPipe {
	
	@Override
	public void finit() throws TrafoException {	
		
		JSch jsch = new JSch();
        Session session = null;
        try {
        	final int port = getParameter("ftpport") != null ? Integer.parseInt(getParameter("ftpport")) : 22;
            session = jsch.getSession(getParameter("ftpuser"), getParameter("ftpserver"), port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(getParameter("ftppassword"));
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            for (TrafoResult result : trafoPipeline.getResults()) {
            	  System.out.println(result.getPath());
		    	  try (InputStream fis = Files.newInputStream(result.getPath())) {
		    		  sftpChannel.put(fis, result.getName());
		    	  }
		    }
            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException e) {
        	throw new TrafoException(e.getMessage());
        } catch (SftpException e) {
        	throw new TrafoException(e.getMessage());
        } catch (IOException io) {
        	throw new TrafoException(io.getMessage());
        } 
 		super.finit();
 	}
}
