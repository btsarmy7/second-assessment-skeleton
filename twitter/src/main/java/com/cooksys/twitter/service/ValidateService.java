package com.cooksys.twitter.service;

import org.springframework.stereotype.Service;

import com.cooksys.twitter.repository.HashtagRepository;

@Service
public class ValidateService {
	
	//private HashtagRepository hashtagRepository;
	private HashtagService hashtagService;

	public ValidateService(/*HashtagRepository hashtagRepository,*/ HashtagService hashtagService) {
		//this.hashtagRepository = hashtagRepository;
		this.hashtagService = hashtagService;
	}

	public boolean tagExists(String label) {
		return hashtagService.tagExists(label);
	}

}
