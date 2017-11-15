package com.cooksys.twitter.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.twitter.entity.User;
import com.cooksys.twitter.entity.Hashtag;
import com.cooksys.twitter.entity.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Integer>{

	Set<Tweet> findByHashtagsOrderByPosted(String tag);

	Tweet findByIdAndDeleted(Integer id, boolean status);

	Tweet findById(Integer id);

	List<Tweet> findByinReplyToAndDeleted(Tweet tweet, boolean status);

	List<Tweet> findByAuthorAndDeleted(User user, boolean status);

	List<Tweet> findByMentionedByAndDeleted(User user, boolean status);

	List<Tweet> findByRepostOfAndDeleted(Tweet tweet, boolean status);

	List<Tweet> findByinReplyTo(Tweet current);

	List<Tweet> findByHashtagsAndDeleted(Hashtag tag, boolean status);
}
