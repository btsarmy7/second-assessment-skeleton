package com.cooksys.twitter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cooksys.twitter.entity.Hashtag;

public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {

	Hashtag findByLabel(String Hashtag);
	
	List<Hashtag> findAll();
}
