package com.cooksys.twitter.dto;

import java.sql.Timestamp;
import java.util.ArrayList;

import com.cooksys.twitter.entity.User;
import com.cooksys.twitter.entity.Tweet;

public class TweetDto {

	private Integer id;

	private Timestamp posted;

	private String content;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getPosted() {
		return posted;
	}

	public void setPosted(Timestamp posted) {
		this.posted = posted;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
		if (!(obj instanceof TweetDto)) {
			return false;
		}
		TweetDto other = (TweetDto) obj;
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
		return "TweetDto [id=" + id + ", posted=" + posted + ", content=" + content + "]";
	}



}
