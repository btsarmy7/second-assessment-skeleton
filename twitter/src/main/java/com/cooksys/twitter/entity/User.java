package com.cooksys.twitter.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.cooksys.twitter.embedded.Credentials;
import com.cooksys.twitter.embedded.Profile;


@Entity 
public class User {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	private String username;
	
	@Embedded
	private Profile profile;
	
	private Timestamp joined;
	
	private boolean status; // true if user is active, false if user is "deleted"
	
	@Embedded
	private Credentials credentials;
	
	@ManyToMany
	private List<Tweet> likedTweets = new ArrayList<>();
	    
	@ManyToMany
	private List<User> followers = new ArrayList<>();
	
	@ManyToMany(mappedBy = "followers")
	private List<User> following = new ArrayList<>();
	
	@OneToMany(mappedBy = "author")
	private List<Tweet> allTweets = new ArrayList<>();
	
	@ManyToMany(mappedBy = "mentions")
	private List<Tweet> mentions = new ArrayList<>();
	
	
	public User() {
		
	}
	
	public User(String username, Profile profile, Timestamp timestamp, Credentials credentials) {
		this.username = username;
		this.profile = profile;
		this.joined = timestamp;
		this.credentials = credentials;
		this.status = true;
	}
	
	
	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public List<Tweet> getLikedTweets() {
		return likedTweets;
	}

	public void setLikedTweets(List<Tweet> likedTweets) {
		this.likedTweets = likedTweets;
	}

	public List<User> getFollowers() {
		return followers;
	}

	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}

	public List<User> getFollowing() {
		return following;
	}

	public void setFollowing(List<User> following) {
		this.following = following;
	}

	public List<Tweet> getAllTweets() {
		return allTweets;
	}

	public void setAllTweets(List<Tweet> allTweets) {
		this.allTweets = allTweets;
	}

	public List<Tweet> getMentions() {
		return mentions;
	}

	public void setMentions(List<Tweet> mentions) {
		this.mentions = mentions;
	}

	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public Profile getProfile() {
		return profile;
	}
	
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	public Timestamp getJoined() {
		return joined;
	}
	
	public void setJoined(Timestamp joined) {
		this.joined = joined;
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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", profile=" + profile + ", joined=" + joined + ", status="
				+ status + ", credentials=" + credentials + ", likedTweets=" + likedTweets + ", followers=" + followers
				+ ", following=" + following + ", allTweets=" + allTweets + ", mentions=" + mentions + "]";
	}
	
	

}
