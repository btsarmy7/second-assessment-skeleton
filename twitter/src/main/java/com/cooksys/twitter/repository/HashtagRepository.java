package com.cooksys.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.twitter.entity.Hashtag;


public interface HashtagRepository extends JpaRepository<Hashtag, Integer>{

	Hashtag findByHashtagName(String hashtagName);

}
