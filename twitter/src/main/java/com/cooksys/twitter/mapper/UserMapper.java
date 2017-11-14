package com.cooksys.twitter.mapper;

import org.mapstruct.Mapper;

import com.cooksys.twitter.Dto.UserDto;
import com.cooksys.twitter.entity.User;


@Mapper(componentModel = "spring")
public interface UserMapper {

	User toUser(UserDto dto);
	
	UserDto toUserDto(User user);
	
}
