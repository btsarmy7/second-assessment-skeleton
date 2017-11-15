package com.cooksys.twitter.dto;

import java.sql.Timestamp;

import javax.persistence.Embedded;

import com.cooksys.twitter.embedded.Credentials;
import com.cooksys.twitter.embedded.Profile;

public class UserDto {

	private Integer id;
	private String userName;
	private boolean status;

	@Embedded
	private Credentials credentials;

	@Embedded
	private Profile profile;
	private Timestamp joined;

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
		if (!(obj instanceof UserDto)) {
			return false;
		}
		UserDto other = (UserDto) obj;
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
		return "UserDto [id=" + id + ", userName=" + userName + ", profile=" + profile + ", joined=" + joined + "]";
	}



}
