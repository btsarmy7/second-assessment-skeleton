package com.cooksys.twitter.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter.dto.HashtagDto;
import com.cooksys.twitter.dto.TweetDto;
import com.cooksys.twitter.entity.Hashtag;
import com.cooksys.twitter.repository.HashtagRepository;
import com.cooksys.twitter.service.HashtagService;
import com.cooksys.twitter.service.TweetService;

@RestController
@RequestMapping("tags")
public class HashtagController {

	private HashtagService hashtagService;
	private HashtagRepository hashtagRepository;
	private TweetService tweetService;

	public HashtagController(HashtagService hashtagService, HashtagRepository hashtagRepository, TweetService tweetService) {
		this.hashtagService = hashtagService;
		this.hashtagRepository = hashtagRepository;
		this.tweetService = tweetService;
	}

	@GetMapping
	public List<HashtagDto> getHashtags(){
		return hashtagService.getHashtags();
	}

	@GetMapping("/{label}")
	public List<TweetDto> findTweetsByHashtags(@RequestParam String label, HttpServletResponse response){
		if (!validHashtag(label)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		return tweetService.findByHashtags(label);
	}

	private boolean validHashtag(String hashtagName){
		Hashtag tag = hashtagRepository.findByHashtagName(hashtagName);
		if (tag != null)
			return true;
		return false;
	}
	

	/*private HashtagService hashtagService;
	private HashtagRepository hashtagRepository;

	
	public HashtagController(HashtagService hashtagService, HashtagRepository hashtagRepository) {
		this.hashtagService = hashtagService;
		this.hashtagRepository = hashtagRepository;
	}
	
	@GetMapping
	public List<HashtagDto> getAllHashtags(){
		return hashtagService.getHashtags();
	}
	
	@GetMapping("/{label}")
	public List<TweetDto> getTagged(@PathVariable String label, HttpServletResponse response){
		if(hashtagRepository.findByLabel(label) != null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return hashtagService.getTagged(label);
	}*/
}
