package com.cooksys.twitter.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksys.twitter.Dto.TweetDto;
import com.cooksys.twitter.Dto.UserDto;
import com.cooksys.twitter.embedded.Credentials;
import com.cooksys.twitter.embedded.Profile;
import com.cooksys.twitter.embedded.UserInfo;
import com.cooksys.twitter.entity.Tweet;
import com.cooksys.twitter.entity.User;
import com.cooksys.twitter.mapper.TweetMapper;
import com.cooksys.twitter.mapper.UserMapper;
import com.cooksys.twitter.repository.HashtagRepository;
import com.cooksys.twitter.repository.TweetRepository;
import com.cooksys.twitter.repository.UserRepository;

@Service
public class UserService {
	
	private UserRepository userRepository;
	private TweetRepository tweetRepository;
	private HashtagRepository hashtagRepository;
	
	private UserMapper userMapper;
	private TweetMapper tweetMapper;

	public UserService(UserRepository userRepository, TweetRepository tweetRepository, HashtagRepository hashtagRepository,
			UserMapper userMapper, TweetMapper tweetMapper) {
		 
		this.userRepository = userRepository;
		this.tweetRepository = tweetRepository;
		this.userMapper = userMapper;
		this.tweetMapper = tweetMapper;
	}
	
	
	public boolean isHashtag(String label) {
		
		if(hashtagRepository.findByLabel(label) != null)
			return true;
		else
			return false;
	}
	
	public boolean isUsername(String username) {
		// check among active users
		if(userRepository.findByUsernameAndStatusTrue(username) != null)
			return true;
		else
			return false;
	}
	
	public boolean availableUsername(String username) {
		// check in all users including "deleted" ones
		if(userRepository.findByUsernameAndStatusFalse(username) != null)
			return false;
		else
			return true;
	}
	
	
	public UserDto[] getAllActiveUsers() {
		UserDto[] allUsers = new UserDto[userRepository.findByStatusTrue().size()];
		List<UserDto> allUserDtos = userRepository.findByStatusTrue().stream().map(userMapper::toUserDto).collect(Collectors.toList());
		for(int i = 0; i < userRepository.findAll().size(); i++) {
			allUsers[i] = allUserDtos.get(i);
		}
		return allUsers;
	}


	public UserDto addUser(UserInfo userInfo) {
		User user = new User(userInfo.getUsername(), userInfo.getProfile(), new Timestamp(System.currentTimeMillis()), userInfo.getCredentials());
		userRepository.saveAndFlush(user);
		return userMapper.toUserDto(user);
	}
	
	public UserDto getUsername(String username) {
		User user = userRepository.findby_username(username);
		return userMapper.toUserDto(user);
				
	}


	public UserDto updateUserProfile(Credentials credentials, Profile profile) {
		User user = userRepository.findby_username(credentials.getUsername());
		user.setProfile(profile);
		User updatedUser = userRepository.saveAndFlush(user);
		return userMapper.toUserDto(updatedUser);
	}


	public UserDto deleteUser(Credentials credentials) {
		User user = userRepository.findby_username(credentials.getUsername());
		user.setStatus(false);
		List<Tweet> deleteTweets = user.getAllTweets();
		for(Tweet t : deleteTweets) {
			t.setContent("");
			t.setDeleted(true);
			tweetRepository.saveAndFlush(t);
		}
		return userMapper.toUserDto(user);
	}
	
public void follow(String username, Credentials credentials){
		
			User follower = userRepository.findby_username(credentials.getUsername());
			User followed = userRepository.findby_username(username);
			follower.getFollowing().add(followed);
			followed.getFollowers().add(follower);
			userRepository.saveAndFlush(follower);
			userRepository.saveAndFlush(followed);
		
	}


	public void unfollow(String username, Credentials credentials){
		
			User follower = userRepository.findby_username(credentials.getUsername());
			User followed = userRepository.findby_username(username);
			follower.getFollowing().remove(followed);
			followed.getFollowers().remove(follower);
			userRepository.saveAndFlush(follower);
			userRepository.saveAndFlush(followed);
		
	}


	public List<TweetDto> getFeed(String username){
		
			User user = userRepository.findby_username(username);
			List<Tweet> userFeed = new ArrayList<>();
			for(User f : user.getFollowing()) {
				userFeed.addAll(f.getAllTweets());
			}
			return userFeed.stream().map(tweetMapper::toTweetDto).collect(Collectors.toList());	
		
	}


	public List<TweetDto> getTweets(String username){
		List<Tweet> allTweets = userRepository.findby_username(username).getAllTweets();
		return allTweets.stream().map(tweetMapper :: toTweetDto).collect(Collectors.toList());
		
	}


	public List<Tweet> getMentions(String username){
		
			User user = userRepository.findby_username(username);
			return user.getMentions();
		
	}


	public List<UserDto> getFollowers(String username){
			User user = userRepository.findby_username(username);
			return user.getFollowers().stream().map(userMapper :: toUserDto).collect(Collectors.toList());
		
	}


	public List<UserDto> getFollowing(String username){
			User user = userRepository.findby_username(username);
			return user.getFollowing().stream().map(userMapper :: toUserDto).collect(Collectors.toList());
	}

	
}
