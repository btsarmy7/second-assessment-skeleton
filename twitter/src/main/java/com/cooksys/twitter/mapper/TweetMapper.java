package com.cooksys.twitter.mapper;

import org.mapstruct.Mapper;

import com.cooksys.twitter.Dto.TweetDto;
import com.cooksys.twitter.entity.Tweet;


@Mapper(componentModel = "spring")
public interface TweetMapper {

	Tweet toTweet(TweetDto dto);
	
	TweetDto toTweetDto(Tweet tweet);
}
