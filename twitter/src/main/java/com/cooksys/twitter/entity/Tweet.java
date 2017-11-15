package com.cooksys.twitter.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;



@Entity
public class Tweet {

	@Id
	@GeneratedValue()
	private Integer id;
	
	@ManyToOne
	private User author;
	
	private Timestamp posted;
	
	private String content;
	
	@ManyToOne
	private Tweet inReplyto;
	
	@ManyToOne
	private Tweet repostOf;
	
	private boolean deleted; // keeps track of whether tweet is deleted 
	
	@ManyToMany(mappedBy = "likedTweets")
	private List<User> likes = new ArrayList<>();
	
	@ManyToMany
	private List<Hashtag> hashtags = new ArrayList<>();
	
	@ManyToMany(mappedBy = "mentions")
	private List<User> mentions = new ArrayList<>();
	
	@OneToMany
	private List<Tweet> replies = new ArrayList<>();
	
	@OneToMany(mappedBy = "repostOf")
	private List<Tweet> reposts = new ArrayList<>();
	
	public Tweet() {
		
	}
	
	public List<Tweet> getReplies() {
		return replies;
	}

	public void setReplies(List<Tweet> replies) {
		this.replies = replies;
	}

	public List<Tweet> getReposts() {
		return reposts;
	}

	public void setReposts(List<Tweet> reposts) {
		this.reposts = reposts;
	}
	
	public Tweet(User author, Timestamp posted, String content) {
		this.author = author;
		this.posted = posted;
		this.content = content;
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

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
		Tweet other = (Tweet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void setRepostOf(Tweet repostOf) {
		this.repostOf = repostOf;
	}
	
	
	public List<User> getLikes() {
		return likes;
	}

	public void setLikes(List<User> likes) {
		this.likes = likes;
	}

	public List<Hashtag> getHashtags() {
		return hashtags;
	}

	public void setHashtags(List<Hashtag> hashtags) {
		this.hashtags = hashtags;
	}

	public List<User> getMentions() {
		return mentions;
	}

	public void setMentions(List<User> mentions) {
		this.mentions = mentions;
	}

	@Override
	public String toString() {
		return "Tweet [id=" + id + ", author=" + author + ", posted=" + posted + ", content=" + content + ", inReplyto="
				+ inReplyto + ", repostOf=" + repostOf + ", deleted=" + deleted + ", likes=" + likes + ", hashtags="
				+ hashtags + ", mentions=" + mentions + ", replies=" + replies + ", reposts=" + reposts + "]";
	}
	
	
	
	
}
