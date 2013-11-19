package com.webartweb.sporttravelgraph.server;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
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

public class UserServlet extends HttpServlet {
		
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
					Entity e = DataStoreManager.searchEntity("User", "email", pathinfo.substring(1), datastore, res);
					if(e != null){
						User aux = DataStoreManager.entityToUser(e);
						out.println(g1.toJson(aux));
					}else{
						res.sendError(HttpServletResponseWrapper.SC_NOT_FOUND,"Don't exist any user with this email.");
					}
				}else{
					List<User> users = DataStoreManager.getUsers(datastore, res);
					if(users != null){
						out.println(g1.toJson(users));
					}else{
						res.sendError(HttpServletResponseWrapper.SC_NOT_FOUND,"There aren`t any user.");
					}
				}
				break;
				
			case "POST":
				if (pathinfo != null ){
					res.sendError(HttpServletResponseWrapper.SC_METHOD_NOT_ALLOWED,"Method not allowed");
				}else{
					User aux =(User) DataStoreManager.serializedToClass(req, res, datastore,User.class);
					Entity e = DataStoreManager.searchEntity("User","email",aux.getEmail(), datastore,res);
					if(e == null){
						datastore.put(DataStoreManager.UserToEntity(aux));
					}else{
						res.sendError(res.SC_BAD_REQUEST, "Please rewrite your request, The User just exist.");
					}
				}
				break;
				
			case "PUT":
				if (pathinfo != null ){
					Entity aux = DataStoreManager.searchEntity("User", "email",pathinfo.substring(1), datastore,res);
					if(aux != null){
						User saux = (User) DataStoreManager.serializedToClass(req, res, datastore,User.class);
						datastore.put(DataStoreManager.updateUser(aux, DataStoreManager.UserToEntity(saux)));
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
					Entity aux = DataStoreManager.searchEntity("User", "email", pathinfo.substring(1) , datastore,res);
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
