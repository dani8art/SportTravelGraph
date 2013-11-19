package com.webartweb.sporttravelgraph.server;


public class Group {
	private String name, idliga;
	private Integer id;
	public Group ( String name, Integer id, String idliga ){
		this.name = name;
		this.id = id;
		this.idliga = idliga;
	}
	
	public String getIdliga() {
		return idliga;
	}

	public void setIdliga(String idliga) {
		this.idliga = idliga;
	}

	public Group(){
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Group other = (Group) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
