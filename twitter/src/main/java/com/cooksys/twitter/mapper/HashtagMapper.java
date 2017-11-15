package com.cooksys.twitter.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import com.cooksys.twitter.dto.HashtagDto;
import com.cooksys.twitter.entity.Hashtag;

@Mapper(componentModel="spring")
public interface HashtagMapper {

	Set<HashtagDto> toHashtagDtos(Set<Hashtag> hashtags);

	List<HashtagDto> toHashtagDtos(List<Hashtag> hashtags);
}
