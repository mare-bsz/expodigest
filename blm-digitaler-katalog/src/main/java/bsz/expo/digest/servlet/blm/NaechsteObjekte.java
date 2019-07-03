package bsz.expo.digest.servlet.blm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Templates;

import org.apache.solr.common.params.MapSolrParams;

import bsz.expo.Digest;

/**
 * Servlet implementation class Items
 */
@WebServlet("/naechste")
public class NaechsteObjekte extends Digest {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {									
			
			final Map<String, String> queryParamMap = new HashMap<String, String>();
			queryParamMap.put("q", "*:*");
			queryParamMap.put("fq", "{!geofilt}");
			queryParamMap.put("sfield", "geo");
			queryParamMap.put("pt", request.getParameter("geo")!= null?request.getParameter("geo"):"47.66033,9.17582");
			queryParamMap.put("sort", "geodist() asc");
			queryParamMap.put("d", request.getParameter("rad")!= null?request.getParameter("rad"):"100");
			queryParamMap.put("rows", request.getParameter("len")!= null?request.getParameter("len"):"10");
			queryParamMap.put("start", request.getParameter("fst")!= null?request.getParameter("fst"):"0");
			MapSolrParams queryParams = new MapSolrParams(queryParamMap);			
			
			final Templates templates = getTemplates(request.getParameter("fmt"), "json");
			
			writeToResponse(response, queryParams, templates.newTransformer());							
			
		} catch (Exception se) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, se.getMessage());
		}	
		
	}
	
}
