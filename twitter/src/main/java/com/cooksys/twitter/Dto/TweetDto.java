package com.cooksys.twitter.Dto;

import java.sql.Timestamp;


import com.cooksys.twitter.entity.Tweet;
import com.cooksys.twitter.entity.User;

public class TweetDto {

	
	private Integer id;
	private User author;	
	private Timestamp posted;	
	private String content;
	private Tweet inReplyto;
	private Tweet repostOf;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
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
	public Tweet getInReplyto() {
		return inReplyto;
	}
	public void setInReplyto(Tweet inReplyto) {
		this.inReplyto = inReplyto;
	}
	public Tweet getRepostOf() {
		return repostOf;
	}
	public void setRepostOf(Tweet repostOf) {
		this.repostOf = repostOf;
	}
	
	
}
