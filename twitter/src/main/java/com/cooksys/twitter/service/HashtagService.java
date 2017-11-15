package com.cooksys.twitter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksys.twitter.Dto.HashtagDto;
import com.cooksys.twitter.Dto.TweetDto;
import com.cooksys.twitter.entity.Hashtag;
import com.cooksys.twitter.mapper.HashtagMapper;
import com.cooksys.twitter.mapper.TweetMapper;
import com.cooksys.twitter.repository.HashtagRepository;

@Service
public class HashtagService {
	
	private HashtagRepository hashtagRepository;
	private HashtagMapper hashtagMapper;
	private TweetMapper tweetMapper;

	public HashtagService(HashtagRepository hashtagRepository, HashtagMapper hashtagMapper, TweetMapper tweetMapper) {
		super();
		this.hashtagMapper = hashtagMapper;
		this.hashtagRepository = hashtagRepository;
		this.tweetMapper = tweetMapper;
	}
	 
	public List<HashtagDto> getHashtags() {
		return hashtagRepository.findAll().stream().map(hashtagMapper :: toHashtagDto).collect(Collectors.toList());
	}
	
	public List<TweetDto> getTagged(String label){
		Hashtag h = hashtagRepository.findByLabel(label);
		return h.getTweets().stream().map(tweetMapper :: toTweetDto).collect(Collectors.toList());
		
	}

}
