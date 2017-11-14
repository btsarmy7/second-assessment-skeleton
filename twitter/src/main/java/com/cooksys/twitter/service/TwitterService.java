package com.cooksys.twitter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.cooksys.twitter.Dto.UserDto;
import com.cooksys.twitter.entity.Credentials;
import com.cooksys.twitter.entity.Hashtag;
import com.cooksys.twitter.entity.Profile;
import com.cooksys.twitter.entity.Tweet;
import com.cooksys.twitter.entity.User;
import com.cooksys.twitter.exceptions.InvalidIdException;
import com.cooksys.twitter.mapper.UserMapper;
import com.cooksys.twitter.repository.HashtagRepository;
import com.cooksys.twitter.repository.TweetRepository;
import com.cooksys.twitter.repository.TwitterRepository;
import com.cooksys.twitter.repository.UserRepository;

public class TwitterService {
	
	private TwitterRepository twitterRepository;
	private HashtagRepository hashtagRepository;
	private UserRepository userRepository;
	private TweetRepository tweetRepository;
	
	public TwitterService(TwitterRepository twitterRepository, HashtagRepository hashtagRepository,
			UserRepository userRepository, TweetRepository tweetRepository) {
		
		this.twitterRepository = twitterRepository;
		this.hashtagRepository = hashtagRepository;
		this.userRepository = userRepository;
		this.tweetRepository = tweetRepository;
	}
	
	
	public boolean isHashtag(String label) {
		
		if(hashtagRepository.findByHashtag(label) != null)
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

	public List<User> getAllActiveUsers() {
		return null;
		/*UserDto[] allUsers = new UserDto[userRepository.findAll().size()];
		List<UserDto> allUserDtos = userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
		for(int i = 0; i < userRepository.findAll().size(); i++) {
			allUsers[i] = toUserDto(allUserDtos.get(i));
		}
		
		return  userRepository.findByStatusTrue().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
		//return allUsers;*/
	}

	public void addUser(Credentials credentials, Profile profile) throws InvalidIdException {
		// TODO Auto-generated method stub
		
	}

	public User getUsername(String username) throws InvalidIdException {
		
		if(!availableUsername(username))
			return userRepository.findby_username(username);
		throw new InvalidIdException("User does not exist");
		
	}

	@Transactional
	public User updateUserProfile(Credentials credentials, Profile profile) throws InvalidIdException {
		
		if(isUsername(credentials.getUsername())) {
			User user = userRepository.findby_username(credentials.getUsername());
			user.setProfile(profile);
			return userRepository.saveAndFlush(user);
		} throw new InvalidIdException("User does not exist");
		
	}

	@Transactional
	public User deleteUser(Credentials credentials) throws InvalidIdException {
		
		if (isUsername(credentials.getUsername())) {
			User user = userRepository.findby_username(credentials.getUsername());
			user.setStatus(false);
			List<Tweet> deleteTweets = user.getAllTweets();
			for(Tweet t : deleteTweets) {
				t.setContent("");
				t.setDeleted(true);
				tweetRepository.saveAndFlush(t);
			}
			return user;
		} throw new InvalidIdException("User does not exist");
		
	}


	public void follow(String username, Credentials credentials) throws InvalidIdException {
		
		if(isUsername(username)) {
			User follower = userRepository.findby_username(credentials.getUsername());
			User followed = userRepository.findby_username(username);
			follower.getFollowing().add(followed);
			followed.getFollowers().add(follower);
			userRepository.saveAndFlush(follower);
			userRepository.saveAndFlush(followed);
		}throw new InvalidIdException("User does not exist");
		
	}


	public void unfollow(String username, Credentials credentials) throws InvalidIdException {
		
		if(isUsername(username)) {
			User follower = userRepository.findby_username(credentials.getUsername());
			User followed = userRepository.findby_username(username);
			follower.getFollowing().remove(followed);
			followed.getFollowers().remove(follower);
			userRepository.saveAndFlush(follower);
			userRepository.saveAndFlush(followed);
		}throw new InvalidIdException("User does not exist");
	}


	public List<Tweet> getFeed(String username) throws InvalidIdException {
		
		if(isUsername(username)) {
			User user = userRepository.findby_username(username);
			List<Tweet> userFeed = new ArrayList<>();
			for(User f : user.getFollowing()) {
				userFeed.addAll(f.getAllTweets());
			}
			//userFeed.sort((o1, o2)-> o1.compareTo(o2));
			return userFeed;
		} throw new InvalidIdException("User does not exist");
		
	}


	public List<Tweet> getTweets(String username) throws InvalidIdException{
		return userRepository.findby_username(username).getAllTweets();
		
	}


	public List<Tweet> getMentions(String username) throws InvalidIdException {
		
		if(isUsername(username)) {
			User user = userRepository.findby_username(username);
			return user.getMentions();
		}throw new InvalidIdException ("Not a user");
		
	}
	
	
	
	
}
