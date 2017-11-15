package com.cooksys.twitter.service;

import org.springframework.stereotype.Service;

@Service
public class ValidateService {
	
	private HashtagService hashtagService;

	public ValidateService(HashtagService hashtagService) {
		this.hashtagService = hashtagService;
	}

	public boolean tagExists(String label) {
		return hashtagService.tagExists(label);
	}

}
