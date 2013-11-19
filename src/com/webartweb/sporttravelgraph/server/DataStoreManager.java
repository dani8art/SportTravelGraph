package com.webartweb.sporttravelgraph.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.*;
import com.google.gson.Gson;

public class DataStoreManager {
	
	public  static <T> Object serializedToClass(HttpServletRequest req,  HttpServletResponse res,DatastoreService  datastore, Class<T> classIn) throws IOException{
		Object ret = null;
		
		Gson gson = new Gson();
		StringBuilder  sb =  new StringBuilder();
		BufferedReader br = req.getReader();
		
		String jsonString;
		while ((jsonString = br.readLine()) != null){
			sb.append(jsonString);
		}
		jsonString = sb.toString();
		
		try{
			ret = gson.fromJson(jsonString, classIn);
			
		}catch(Exception e){
			res.sendError(res.SC_BAD_REQUEST, "Please rewrite your request" + e.getMessage());
			System.out.println(e.getMessage());
		}
		return ret;
	}
	
	public static Entity TeamToEntity(Team s){
		Entity ret = new Entity("Team");
		ret.setProperty("name", s.getName());
		ret.setProperty("location",s.getLocation());
		ret.setProperty("gameday", s.getGameday());
		ret.setProperty("gamehour", s.getGamehour());
		ret.setProperty("idliga", s.getIdliga());
		ret.setProperty("idgroup", s.getIdgroup());
		ret.setProperty("town", s.getTown());
		return ret;
	}
	public static Entity LeagueToEntity(League s){
		Entity ret = new Entity("League");
		ret.setProperty("name", s.getName());
		ret.setProperty("id", s.getId());
		return ret;
	}
	
	public static Entity GroupToEntity(Group s){
		Entity ret = new Entity("Group");
		ret.setProperty("name", s.getName());
		ret.setProperty("id", s.getId());
		ret.setProperty("idliga", s.getIdliga());
		return ret;
	}

	public static Entity searchEntity( String EntityType, String property, Object value, DatastoreService datastore, HttpServletResponse res ) throws IOException{
		Entity ret = null;
		try{
			Query q = new Query(EntityType)
					.setFilter(new Query.FilterPredicate(property, Query.FilterOperator.EQUAL, value));
			PreparedQuery pq = datastore.prepare(q);
			ret = pq.asSingleEntity();
		}catch(Exception ex){
			res.sendError(res.SC_BAD_REQUEST, "Please rewrite your request" + ex.getMessage());
		}
		return ret;
	}
	
	public static List<League> getLeagues(DatastoreService datastore, HttpServletResponse res ) throws IOException{
		List<League> ret = new ArrayList<>();
		try{
			Query q = new Query("League");
			PreparedQuery pq = datastore.prepare(q);
			Iterator<Entity> it = pq.asIterator();
			while(it.hasNext()){
				Entity eaux = it.next();
				if(eaux != null){
					League saux = DataStoreManager.entityToLeague(eaux);
					ret.add(saux);
				}
			}
		}catch(Exception ex){
			res.sendError(res.SC_BAD_REQUEST, "Please rewrite your request" + ex.getMessage());
		}
		return ret;
	}
	
	public static List<Group> getGroups(String property, Object value,DatastoreService datastore,  HttpServletResponse res ) throws IOException{
		List<Group> ret = new ArrayList<>();
		try{
			Query q = new Query("Group");
			if(property != ""){
				q.setFilter(new Query.FilterPredicate(property, Query.FilterOperator.EQUAL, value));
			}
			PreparedQuery pq = datastore.prepare(q);
			Iterator<Entity> it = pq.asIterator();
			while(it.hasNext()){
				Entity eaux = it.next();
				if(eaux != null){
					Group saux = DataStoreManager.entityToGroup(eaux);
					ret.add(saux);
				}
			}
		}catch(Exception ex){
			res.sendError(res.SC_BAD_REQUEST, "Please rewrite your request" + ex.getMessage());
		}
		return ret;
	}
	public static List<Team> getTeams(String idliga, Integer idgroup,DatastoreService datastore,  HttpServletResponse res ) throws IOException{
		List<Team> ret = new ArrayList<>();
		try{
			Query q = new Query("Team");
			if(idliga != ""){
				q.setFilter(new Query.FilterPredicate("idliga", Query.FilterOperator.EQUAL, idliga));
			}
			PreparedQuery pq = datastore.prepare(q);
			Iterator<Entity> it = pq.asIterator();
			while(it.hasNext()){
				Entity eaux = it.next();
				if(eaux != null){
					Team saux = DataStoreManager.entityToTeam(eaux);
					if(saux.getIdgroup().equals(idgroup) || idgroup == 0)
						ret.add(saux);
				}
			}
		}catch(Exception ex){
			res.sendError(res.SC_BAD_REQUEST, "Please rewrite your request" + ex.getMessage());
		}
		return ret;
	}
	public static Team entityToTeam(Entity e){
		Team ret = new Team((String) e.getProperty("name"),(String) e.getProperty("location"),
				(String) e.getProperty("gamehour"),(String) e.getProperty("gameday"),
				(String) e.getProperty("idliga"), (Integer)(int)(long) e.getProperty("idgroup"),
				(String) e.getProperty("town"));		
		return ret;
	}
	public static League entityToLeague (Entity e){
		return new League( (String) e.getProperty("name"), (String) e.getProperty("id"));
	}
	public static Group entityToGroup (Entity e){
		return new Group( (String) e.getProperty("name"), (Integer)(int)(long) e.getProperty("id"),(String) e.getProperty("idliga"));
	}
	public static Entity updateTeam(Entity aux, Entity saux){
		aux.setProperty("name", saux.getProperty("name"));
		aux.setProperty("location", saux.getProperty("location"));
		aux.setProperty("gameday", saux.getProperty("gameday"));
		aux.setProperty("gamehour", saux.getProperty("gamehour"));
		aux.setProperty("idliga", saux.getProperty("idliga"));
		aux.setProperty("idgroup", saux.getProperty("idgroup"));
		aux.setProperty("town", saux.getProperty("town"));
		return aux;
	}
	public static Entity updateLeague(Entity aux, Entity saux){
		aux.setProperty("name", saux.getProperty("name"));
		aux.setProperty("id", saux.getProperty("id"));
		return aux;
	}
	public static Entity updateGroup(Entity aux, Entity saux){
		aux.setProperty("name", saux.getProperty("name"));
		aux.setProperty("id", saux.getProperty("id"));
		aux.setProperty("idliga", saux.getProperty("idliga"));
		return aux;
	}
}
