package com.cooksys.twitter.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import com.cooksys.twitter.dto.TweetDto;
import com.cooksys.twitter.entity.Tweet;

@Mapper(componentModel="spring")
public interface TweetMapper {

	TweetDto toTweetDto(Tweet tweet);

	Tweet fromTweetDto(TweetDto tweetDto);

	Set<TweetDto> toTweetDtos(Set<Tweet> set);

	List<TweetDto> toTweetDtos(List<Tweet> tweet);

}
