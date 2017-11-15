package com.cooksys.twitter.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Hashtag {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(unique=true)
	private String hashtagName;

	@ManyToMany(mappedBy="hashtags")
	private Set<Tweet> tweets = new HashSet<>();

	
	public Hashtag() {
		
	}
	
	public Hashtag(String hashtag) {
		this.hashtagName = hashtag;
	}
	
	public Set<Tweet> getTweets() {
		return tweets;
	}

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHashtag() {
		return hashtagName;
	}

	public void setHashtag(String hashtag) {
		this.hashtagName = hashtag;
	}
	
	public void setTweets(Set<Tweet> tweets) {
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Hashtag)) {
			return false;
		}
		Hashtag other = (Hashtag) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Hashtag [id=" + id + ", hashtagName=" + hashtagName + "]";
	}
}
