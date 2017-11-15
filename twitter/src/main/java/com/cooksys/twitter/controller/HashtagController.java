package com.cooksys.twitter.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter.Dto.HashtagDto;
import com.cooksys.twitter.Dto.TweetDto;
import com.cooksys.twitter.repository.HashtagRepository;
import com.cooksys.twitter.service.HashtagService;


@RestController
@RequestMapping("tags")
public class HashtagController {
	
	private HashtagService hashtagService;
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
	}
	

}
