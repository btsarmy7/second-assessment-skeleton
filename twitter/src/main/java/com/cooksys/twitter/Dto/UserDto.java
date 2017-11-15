package com.cooksys.twitter.Dto;

import java.sql.Timestamp;

import javax.persistence.Embedded;

import com.cooksys.twitter.embedded.Credentials;
import com.cooksys.twitter.embedded.Profile;


public class UserDto {
	
	private Integer id;
	
	private String username;

	@Embedded
	private Profile profile;
	
	private Timestamp joined;
	
	private boolean status;
	
	@Embedded 
	private Credentials credentials;

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

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	
	

}
