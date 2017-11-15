package com.cooksys.twitter.embedded;

import java.util.ArrayList;
import java.util.List;

import com.cooksys.twitter.dto.TweetDto;

public class Context {

	private TweetDto target;
	private List<TweetDto> before = new ArrayList<TweetDto>();
	private List<TweetDto> after = new ArrayList<TweetDto>();

	public Context(TweetDto target) {
		this.target = target;
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
