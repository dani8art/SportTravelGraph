package com.webartweb.sporttravelgraph.server;



import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

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

public class TeamServlet extends HttpServlet {
		
		private static Gson g1 = new Gson();
		private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
			process_req(req,res);		
		}
		
		public void doPost (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
			process_req(req, res);
		}
		
		public void doPut (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
			process_req(req, res);
		}
		
		public void doDelete (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
			process_req(req, res);
		}
		
		public void process_req (HttpServletRequest req, HttpServletResponse res)throws ServletException,IOException{
			
			
			PrintWriter out = res.getWriter();
			String pathinfo = req.getPathInfo();
					
			switch (req.getMethod()) {
			case "GET":
				if (pathinfo != null ){
					Entity e = DataStoreManager.searchEntity("Team", "name",pathinfo.substring(1), datastore,res);
					if(e != null){
						Team aux = DataStoreManager.entityToTeam(e);
						out.println(g1.toJson(aux));
					}else{
						res.sendError(HttpServletResponseWrapper.SC_NOT_FOUND,"source not found");
					}
				}else{
					String idliga = null;
					Integer idgroup = null;
					if(req.getParameter("idliga") != null && req.getParameter("idgroup") != null){
						idliga = req.getParameter("idliga");
						idgroup= new Integer( req.getParameter("idgroup"));
					}
					if( idliga != null && idliga != ""
							&& idgroup != null && idgroup != 0){
						List<Team> l  = DataStoreManager.getTeams(idliga, idgroup, datastore, res);
						out.println(g1.toJson(l));
					}else{
						List<Team> l  = DataStoreManager.getTeams("", 0, datastore, res);
						out.println(g1.toJson(l));
					}
				}
				break;
				
			case "POST":
				if (pathinfo != null ){
					res.sendError(HttpServletResponseWrapper.SC_METHOD_NOT_ALLOWED,"Method not allowed");
				}else{
					Team aux = (Team) DataStoreManager.serializedToClass(req, res, datastore, Team.class);
					Entity e = DataStoreManager.searchEntity("Team","name",aux.getName(), datastore,res);
					if(e == null){
						datastore.put(DataStoreManager.TeamToEntity(aux));
					}else{
						res.sendError(res.SC_BAD_REQUEST, "Please rewrite your request, The Team just exist.");
					}
				}
				break;
				
			case "PUT":
				if (pathinfo != null ){
					Entity aux = DataStoreManager.searchEntity("Team", "name",pathinfo.substring(1), datastore,res);
					if(aux != null){
						Team saux = (Team) DataStoreManager.serializedToClass(req, res, datastore, Team.class);
						datastore.put(DataStoreManager.updateTeam(aux, DataStoreManager.TeamToEntity(saux)));
						out.println(g1.toJson(saux));
					}else{
						res.sendError(res.SC_NOT_FOUND, "Source don't exist");
					}			
				}else{
					res.sendError(HttpServletResponseWrapper.SC_METHOD_NOT_ALLOWED,"Method not allowed");
				}
				break;
				
			case "DELETE":
				if (pathinfo != null){
					Entity aux = DataStoreManager.searchEntity("Team", "name", pathinfo.substring(1) , datastore,res);
					if(aux != null){
						datastore.delete(aux.getKey());
					}else{
						res.sendError(res.SC_NOT_ACCEPTABLE, "The Source not be remove because this don't exist");
					}
				}else{
					//por implementar borrar todas.
					res.sendError(HttpServletResponseWrapper.SC_METHOD_NOT_ALLOWED,"Method not allowed");
				}
				break;
			default:
				res.sendError(HttpServletResponseWrapper.SC_METHOD_NOT_ALLOWED,"Method not allowed");
				break;
			}
			
			out.close();
		}
}
