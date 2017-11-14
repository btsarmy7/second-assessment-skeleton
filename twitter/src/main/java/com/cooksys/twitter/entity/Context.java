package com.cooksys.twitter.entity;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class Context {

	private Integer id;
	private Tweet target;
	private List<Tweet> before;
	private List<Tweet> after;
	
	public Context() {
		
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Tweet getTarget() {
		return target;
	}
	
	public void setTarget(Tweet target) {
		this.target = target;
	}
	
	public List<Tweet> getBefore() {
		return before;
	}
	
	public void setBefore(List<Tweet> before) {
		this.before = before;
	}
	
	public List<Tweet> getAfter() {
		return after;
	}
	
	public void setAfter(List<Tweet> after) {
		this.after = after;
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
		Context other = (Context) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
}
