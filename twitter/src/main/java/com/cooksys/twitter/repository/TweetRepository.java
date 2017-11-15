package com.cooksys.twitter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.twitter.entity.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Integer>{

	Tweet findById(Integer id);
	
	List<Tweet> findByAuthor_Username(String username);
	
    Tweet findByHashtags_Hashtag(String hashtag);

	Tweet findByIdAndDeletedFalse(Integer id);

}
