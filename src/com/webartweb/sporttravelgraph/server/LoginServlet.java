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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;

public class LoginServlet extends HttpServlet {
		
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
					Entity e = DataStoreManager.searchEntity("userlog", "user", pathinfo.substring(1), datastore, res);
					if(e != null){
						out.println(g1.toJson(e.getProperty("sessionID")));
					}else{
						res.sendError(HttpServletResponseWrapper.SC_NOT_FOUND,"source not found");
					}
				}else{
					res.sendError(HttpServletResponseWrapper.SC_METHOD_NOT_ALLOWED,"Method not allowed");
				}
				break;
				
			case "POST":
				if (pathinfo != null ){
					res.sendError(HttpServletResponseWrapper.SC_METHOD_NOT_ALLOWED,"Method not allowed");
				}else{
					String user = req.getParameter("user");
					String password = req.getParameter("password");
					String remember = req.getParameter("remember");
					Entity e = DataStoreManager.searchEntity("User","username",user, datastore,res);
					if(e == null){
						res.sendError(res.SC_BAD_REQUEST, "Please rewrite your request, The User don't exist.");
					}else{
						Entity ea = DataStoreManager.searchEntity("userlog","user",user, datastore,res);
						if(ea == null){
							if(password.equals((String)e.getProperty("password"))){
								Entity log = new Entity("userlog");
								log.setProperty("user", e.getProperty("username"));
								log.setProperty("sessionID", UserLog.generateID());
								datastore.put(log);
								UserLog ul = new UserLog((String)log.getProperty("user"),(String) log.getProperty("sessionID"));
								Cookie coo  = new Cookie("session", g1.toJson(ul));
								
								if(remember != null && remember.equals("remember-me"))
									coo.setMaxAge(24*60*60*15);
								
								coo.setPath("/");
								res.addCookie(coo);
								res.sendRedirect("../../management/index.html");
							}
						}else{
							Entity eaux = DataStoreManager.searchEntity("userlog", "user",  e.getProperty("username"), datastore, res);
							datastore.delete(eaux.getKey());
							if(password.equals((String)e.getProperty("password"))){
								Entity log = new Entity("userlog");
								log.setProperty("user", e.getProperty("username"));
								log.setProperty("sessionID", UserLog.generateID());
								datastore.put(log);
								UserLog ul = new UserLog((String)log.getProperty("user"),(String) log.getProperty("sessionID"));
								Cookie coo  = new Cookie("session", g1.toJson(ul));
								
								if(remember != null && remember.equals("remember-me"))
									coo.setMaxAge(24*60*60*15);
								
								coo.setPath("/");
								res.addCookie(coo);
								res.sendRedirect("../../management/index.html");
							}
						}
					}
				}
				break;
				
			case "PUT":
				if (pathinfo != null ){
					Entity aux = DataStoreManager.searchEntity("League", "id",pathinfo.substring(1), datastore,res);
					if(aux != null){
						League saux = (League) DataStoreManager.serializedToClass(req, res, datastore,Team.class);
						datastore.put(DataStoreManager.updateLeague(aux, DataStoreManager.LeagueToEntity(saux)));
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
					Entity aux = DataStoreManager.searchEntity("League", "id", pathinfo.substring(1) , datastore,res);
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
