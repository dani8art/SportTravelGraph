package com.webartweb.sporttravelgraph.server;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;



public class GeoCodeServlet extends HttpServlet {
		
		private static Gson g1 = new Gson();
		private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
			process_req(req,res);		
		}
		
		
		@SuppressWarnings("unused")
		public void process_req (HttpServletRequest req, HttpServletResponse res)throws ServletException,IOException{
			String urlgeo = "https://maps.googleapis.com/maps/api/geocode/json?address=";
			
			PrintWriter out = res.getWriter();
			String pathinfo = req.getPathInfo();
					
			switch (req.getMethod()) {
			case "GET":
				if (pathinfo != null ){
					Entity e = DataStoreManager.searchEntity("Team", "town", pathinfo.substring(1), datastore, res);
					
					if(false){
						Team t = DataStoreManager.entityToTeam(e);
						out.println(t.getLocation());
					}else{
						urlgeo += pathinfo.substring(1)+"&sensor=false";
						
						Text json = null;
						InputStream is = null;
						try{
							URL url = new URL(urlgeo);
							is = url.openStream();
						
							json= new Text(new Scanner( is ).useDelimiter("\\Z").next());
														
							if(!json.getValue().contains("OVER_QUERY_LIMIT") && 
									!json.getValue().contains("ZERO_RESULTS") &&
									!json.getValue().contains("REQUEST_DENIED") &&
									!json.getValue().contains("REQUEST_DENIED")){
								
							}
							out.println(json.getValue());
							
						}catch(Exception ex){
							ex.printStackTrace();
						}finally{
							try{ is.close();}catch(IOException ex){}
						}
					}
				}else{
					
				}
				break;
			}
			
			out.close();
		}
}
