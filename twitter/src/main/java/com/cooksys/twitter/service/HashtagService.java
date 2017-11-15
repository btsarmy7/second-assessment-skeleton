package com.cooksys.twitter.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooksys.twitter.dto.HashtagDto;
import com.cooksys.twitter.entity.Hashtag;
import com.cooksys.twitter.mapper.HashtagMapper;
import com.cooksys.twitter.repository.HashtagRepository;

@Service
public class HashtagService {

	private HashtagRepository hashtagRepository;
	private HashtagMapper hashtagMapper;

	public HashtagService(HashtagRepository hashtagRepository, HashtagMapper hashtagMapper) {
		this.hashtagRepository = hashtagRepository;
		this.hashtagMapper = hashtagMapper;
	}

	@Transactional
	public Hashtag create(String hashtagName){
		Hashtag tag = new Hashtag(hashtagName);
		return hashtagRepository.save(tag);
	}

	public boolean tagExists(String label) {
		Hashtag hashtag = hashtagRepository.findByHashtagName(label);
		return hashtag != null;
	}

	public List<HashtagDto> getHashtags() {
		return hashtagMapper.toHashtagDtos(hashtagRepository.findAll());
	}

}
