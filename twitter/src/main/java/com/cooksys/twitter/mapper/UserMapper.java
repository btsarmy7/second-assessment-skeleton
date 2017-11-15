package com.cooksys.twitter.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import com.cooksys.twitter.dto.UserDto;
import com.cooksys.twitter.entity.User;

@Mapper(componentModel="spring")
public interface UserMapper {

	UserDto toUserDto(User user);

	User fromUserDto(UserDto userDto);

	Set<UserDto> toUserDtos(Set<User> set);

	List<UserDto> toUserDtos(List<User> set);
}
