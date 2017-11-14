package com.cooksys.twitter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.twitter.entity.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Integer>{

	public Tweet findById(Integer id);
	
	public List<Tweet> findByAuthor_Username(String username);
	
    public Tweet findByHashtags_Hashtag(String hashtag);

	public Tweet findByIdAndDeletedFalse(Integer id);

}
