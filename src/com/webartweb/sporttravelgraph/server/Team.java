package com.webartweb.sporttravelgraph.server;



public class Team {
	private String name, gamehour, gameday, idliga,town,location;
	private Integer idgroup;
	public Team ( String name, String jsonLocation, String gameHour, String gameDay, String idliga, Integer idgroup, String town ){
		this.name = name;
		this.location = jsonLocation;
		this.gamehour = gameHour;
		this.gameday = gameDay;
		this.idliga= idliga;
		this.idgroup = idgroup;
		this.town = town;
	}
	
	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public Team(){
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public Integer getIdgroup() {
		return idgroup;
	}

	public void setIdgroup(Integer idgroup) {
		this.idgroup = idgroup;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getGamehour() {
		return gamehour;
	}

	public void setGamehour(String gamehour) {
		this.gamehour = gamehour;
	}

	public String getGameday() {
		return gameday;
	}

	public void setGameday(String gameday) {
		this.gameday = gameday;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Team other = (Team) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getIdliga() {
		return idliga;
	}

	public void setIdliga(String idliga) {
		this.idliga = idliga;
	}

	
}
