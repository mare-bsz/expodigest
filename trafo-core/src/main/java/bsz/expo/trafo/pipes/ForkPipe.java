package bsz.expo.trafo.pipes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoPipe;
import bsz.expo.trafo.TrafoTicket;
import nu.xom.Serializer;

public class ForkPipe extends TrafoPipe {
	
	CloseableHttpClient httpclient;	
	String sessionId;
	String url;
	Serializer serializer;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	
	@Override
	public void init() throws TrafoException {
		
		url = getParameter("url");
		
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url + "/init");
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("pipeline", getParameter("pipeline")));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			CloseableHttpResponse response = httpclient.execute(httpPost);

			try {
			    System.out.println("ForkPipe: Statusline für " + url + "/init: " + response.getStatusLine());
			    HttpEntity entity = response.getEntity();
			    sessionId = EntityUtils.toString(entity);
			    EntityUtils.consume(entity);
			    System.out.println("ForkPipe: Folgende SessionID wurde ermittelt: " + sessionId);
			} finally {
			    response.close();
			}	
			serializer = new Serializer(out, "UTF-8");
		} catch (IOException e) {
			throw new TrafoException(e);
		}		
		System.out.println("ForkPipe wurde initialisiert und ergab für " + url + "die Session-Id " + sessionId);
		super.init();
	}	
		
	@Override
	public void process(TrafoTicket ticket) throws TrafoException {
		try {
			HttpPost httpPost = new HttpPost(url + "/process;jsessionid=" + sessionId);
			out.reset();
			serializer.write(ticket.getDocument());
			serializer.flush();
			httpPost.setEntity(new ByteArrayEntity(out.toByteArray()));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
			    System.out.println("ForkPipe: Statusline für " + url + "/process;jsessionid=" + sessionId + ": " + response.getStatusLine());
			    HttpEntity entity = response.getEntity();
			    String result = EntityUtils.toString(entity);
			    EntityUtils.consume(entity);
			    System.out.println(result);
			} finally {
			    response.close();
			}
		} catch (IOException e) {
			throw new TrafoException(e);
		}
		super.process(ticket);
	}
	
	@Override
	public void finit() throws TrafoException {
		try {
			httpclient.execute(new HttpPost(url + "/finit;jssessionid=" + sessionId));
		} catch (IOException e) {
			throw new TrafoException(e);
		}
		super.finit();
	}

}
