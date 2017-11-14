package com.cooksys.twitter.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "hashtag")
public class Hashtag {


	@Id
	@GeneratedValue
	private Integer id;
	
	@Column (name = "hashtag_label", nullable = false)
	private String label;
	
	@Column (name = "firstUsed", nullable = false)
	private Timestamp firstUsed;
	
	@Column (name = "lastUsed", nullable = false)
	private Timestamp lastUsed;
	
	@ManyToMany
	private List<Tweet> tweets = new ArrayList<>();
	
	public Hashtag() {
		
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public Timestamp getFirstUsed() {
		return firstUsed;
	}
	
	public void setFirstUsed(Timestamp firstUsed) {
		this.firstUsed = firstUsed;
	}
	
	public Timestamp getLastUsed() {
		return lastUsed;
	}
	
	public void setLastUsed(Timestamp lastUsed) {
		this.lastUsed = lastUsed;
	}
	
	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
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
		Hashtag other = (Hashtag) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}

