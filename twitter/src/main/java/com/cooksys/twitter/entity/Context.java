package com.cooksys.twitter.entity;

import java.util.List;

import com.cooksys.twitter.Dto.TweetDto;

public class Context {

	private TweetDto target;
	private List<TweetDto> before;
	private List<TweetDto> after;
	
	public Context() {
		
	}

	public TweetDto getTarget() {
		return target;
	}

	public void setTarget(TweetDto target) {
		this.target = target;
	}

	public List<TweetDto> getBefore() {
		return before;
	}

	public void setBefore(List<TweetDto> before) {
		this.before = before;
	}

	public List<TweetDto> getAfter() {
		return after;
	}

	public void setAfter(List<TweetDto> after) {
		this.after = after;
	}
	
	
	
	
}
