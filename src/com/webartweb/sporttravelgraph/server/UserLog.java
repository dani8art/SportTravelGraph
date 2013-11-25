package com.webartweb.sporttravelgraph.server;

public class UserLog {
	
	private String user, sessionID;
	private static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-.";
	
	public UserLog (String user, String sessionID){
		this.user = user;
		this.sessionID = sessionID;
	}
	public UserLog (String user){
		this.user = user;
		this.sessionID = generateID();
	}
	public UserLog (){
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sessionID == null) ? 0 : sessionID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserLog other = (UserLog) obj;
		if (sessionID == null) {
			if (other.sessionID != null)
				return false;
		} else if (!sessionID.equals(other.sessionID))
			return false;
		return true;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	public static String generateID(){
		String ret = "";
		
		for (int i = 0; i< 56; i++){
			int index = (int) Math.round((Math.random() * 100));
			ret += chars.charAt(index % chars.length());
		}
		
		return ret;
	}
}
