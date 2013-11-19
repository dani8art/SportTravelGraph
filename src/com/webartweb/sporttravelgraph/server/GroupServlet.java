package com.webartweb.sporttravelgraph.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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

public class GroupServlet extends HttpServlet {
		
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
					Entity e = DataStoreManager.searchEntity("Group", "id", pathinfo.substring(1), datastore, res);
					if(e != null){
						Group aux = DataStoreManager.entityToGroup(e);
						out.println(g1.toJson(aux));
					}else{
						res.sendError(HttpServletResponseWrapper.SC_NOT_FOUND,"source not found");
					}
				}else{
					if(req.getParameter("idliga")!= null && req.getParameter("idliga")!= ""){
						List<Group> groups = DataStoreManager.getGroups("idliga",req.getParameter("idliga"),datastore, res);
						if(groups != null){
							out.println(g1.toJson(groups));
						}else{
							res.sendError(HttpServletResponseWrapper.SC_NOT_FOUND,"source not found");
						}
					}else{
						List<Group> groups = DataStoreManager.getGroups("","", datastore, res);
						if(groups != null){
							out.println(g1.toJson(groups));
						}else{
							res.sendError(HttpServletResponseWrapper.SC_NOT_FOUND,"source not found");
						}
					}
				}
				break;
				
			case "POST":
				if (pathinfo != null ){
					res.sendError(HttpServletResponseWrapper.SC_METHOD_NOT_ALLOWED,"Method not allowed");
				}else{
					Group aux =(Group) DataStoreManager.serializedToClass(req, res, datastore,Group.class);
					Entity e = DataStoreManager.searchEntity("Group","id",aux.getName(), datastore,res);
					if(e == null){
						datastore.put(DataStoreManager.GroupToEntity(aux));
					}else{
						res.sendError(res.SC_BAD_REQUEST, "Please rewrite your request, The Team just exist.");
					}
				}
				break;
				
			case "PUT":
				if (pathinfo != null ){
					Entity aux = DataStoreManager.searchEntity("Group", "id",pathinfo.substring(1), datastore,res);
					if(aux != null){
						Group saux = (Group) DataStoreManager.serializedToClass(req, res, datastore,Group.class);
						datastore.put(DataStoreManager.updateGroup(aux, DataStoreManager.GroupToEntity(saux)));
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
					Entity aux = DataStoreManager.searchEntity("Group", "id", pathinfo.substring(1) , datastore,res);
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
