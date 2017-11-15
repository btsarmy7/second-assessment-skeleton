package com.cooksys.twitter.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cooksys.twitter.embedded.Credentials;
import com.cooksys.twitter.embedded.Profile;

@Entity
@Table(name = "Usertable")
public class User {

	@Id
	@GeneratedValue
	private Integer id;

	private String userName;

	private boolean deleted;

	private Timestamp joined;

	@Embedded
	private Profile profile;

	@Embedded
	private Credentials credentials;
	
	@ManyToMany(mappedBy="mentionedBy")
	private List<Tweet> mentions = new ArrayList<Tweet>();

	@ManyToMany
	private Set<Tweet> likes = new HashSet<Tweet>();

	@ManyToMany
	private Set<User> followers = new HashSet<User>();

	@ManyToMany(mappedBy="followers")
	private Set<User> following = new HashSet<User>();

	@OneToMany(mappedBy="author")
	private Set<Tweet> tweets = new HashSet<Tweet>();

	public User() {
		
	}

	public User(Credentials credentials, Profile profile) {
		this.userName = credentials.getUserLogin();
		this.deleted = false;
		this.credentials = credentials;
		this.profile = profile;
		this.joined = new Timestamp(System.currentTimeMillis());
	}

	public User(boolean deleted, Credentials credentials, Profile profile) {
		this.userName = credentials.getUserLogin();
		this.deleted = deleted;
		this.credentials = credentials;
		this.profile = profile;
		this.joined = new Timestamp(System.currentTimeMillis());
	}

	public List<Tweet> getMentions() {
		return mentions;
	}

	public void setMentions(List<Tweet> mentions) {
		this.mentions = mentions;
	}

	public Set<Tweet> getLikes() {
		return likes;
	}

	public void setLikes(Set<Tweet> likes) {
		this.likes = likes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Timestamp getJoined() {
		return joined;
	}

	public void setJoined(Timestamp joined) {
		this.joined = joined;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Set<User> getFollowers() {
		return followers;
	}

	public void setFollowers(Set<User> followers) {
		this.followers = followers;
	}

	public Set<User> getFollowing() {
		return following;
	}

	public void setFollowing(Set<User> following) {
		this.following = following;
	}

	public Set<Tweet> getTweets() {
		return tweets;
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
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
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
		return "User [id=" + id + ", userName=" + userName + ", deleted=" + deleted + ", joined=" + joined
				+ ", profile=" + profile + ", credentials=" + credentials + "]";
	}
}
