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

public class LogoutServlet extends HttpServlet {
		
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
					res.sendError(HttpServletResponseWrapper.SC_METHOD_NOT_ALLOWED,"Method not allowed");
				}else{
					UserLog aux = (UserLog) DataStoreManager.serializedToClass(req, res, datastore, UserLog.class);
					Entity e = DataStoreManager.searchEntity("userlog", "user", aux.getUser(), datastore, res);
					datastore.delete(e.getKey());
					
					res.setContentType("text/html");
					Cookie[] cookies = req.getCookies();
			        if(cookies != null){
			        	for(Cookie cookie : cookies){
			        		cookie.setMaxAge(0);
			        		cookie.setPath("/");
			        		res.addCookie(cookie);
			        	}
			        }			       
			        res.sendRedirect("../../login.html");
				}
				break;
				
			case "POST":
				if (pathinfo != null ){		
					res.sendError(HttpServletResponseWrapper.SC_METHOD_NOT_ALLOWED,"Method not allowed");
				}else{
					UserLog aux = (UserLog) DataStoreManager.serializedToClass(req, res, datastore, UserLog.class);
					Entity e = DataStoreManager.searchEntity("userlog", "user", aux.getUser(), datastore, res);
					datastore.delete(e.getKey());
					
					res.setContentType("text/html");
					Cookie[] cookies = req.getCookies();
			        if(cookies != null){
			        	for(Cookie cookie : cookies){
			        		cookie.setMaxAge(0);
			        		cookie.setPath("/");
			        		res.addCookie(cookie);
			        	}
			        }			       
			        res.sendRedirect("../../login.html");
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
