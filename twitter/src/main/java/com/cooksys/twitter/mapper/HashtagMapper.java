package com.cooksys.twitter.mapper;


import org.mapstruct.Mapper;

import com.cooksys.twitter.Dto.HashtagDto;
import com.cooksys.twitter.entity.Hashtag;


@Mapper(componentModel = "spring")
public interface HashtagMapper {


	Hashtag toHashtag(HashtagDto dto);
	
	HashtagDto toHashtagDto(Hashtag hashtag);

}
