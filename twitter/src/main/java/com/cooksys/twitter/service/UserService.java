package com.cooksys.twitter.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooksys.twitter.dto.UserDto;
import com.cooksys.twitter.dto.TweetDto;
import com.cooksys.twitter.embedded.UserData;
import com.cooksys.twitter.embedded.Credentials;
import com.cooksys.twitter.embedded.Profile;
import com.cooksys.twitter.entity.User;
import com.cooksys.twitter.entity.Tweet;
import com.cooksys.twitter.mapper.UserMapper;
import com.cooksys.twitter.mapper.TweetMapper;
import com.cooksys.twitter.repository.UserRepository;
import com.cooksys.twitter.repository.TweetRepository;

@Service
public class UserService {

	private UserRepository userRepository;
	private UserMapper userMapper;
	private TweetRepository tweetRepository;
	private TweetMapper tweetMapper;

	public UserService(UserRepository userRepository, UserMapper userMapper, TweetRepository tweetRepository, TweetMapper tweetMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.tweetRepository = tweetRepository;
		this.tweetMapper = tweetMapper;
	}

	public boolean userNameExists(String userName) {
		User user = userRepository.findByUserName(userName);
		return user != null;
	}
	
	public Set<UserDto> findUsers() {
		return userMapper.toUserDtos(userRepository.findByDeleted(false));
	}
	
	/*public UserDto[] getAll() {
	UserDto[] allUsers = new UserDto[userRepository.getAllUsers().size()];
	List<UserDto> allUserDtos = userRepository.getAllUsers().stream().map(userMapper::toUserDto).collect(Collectors.toList());
	for(int i = 0; i < userRepository.getAllUsers().size(); i++) {
		if(allUserDtos.get(i).isActive()) {
		allUsers[i] = allUserDtos.get(i);
		}
	}
		return allUsers;
	}*/

	@Transactional
	public UserDto create(Credentials credentials, Profile profile) {

		User user = new User(credentials, profile);
		return userMapper.toUserDto(userRepository.save(user));
	}
	
	@Transactional
	public UserDto create(boolean status, Credentials credentials, Profile profile) {
		User user = new User(status, credentials, profile);
		return userMapper.toUserDto(userRepository.save(user));
	}
	
	@Transactional
	public UserDto create(UserData userData){
		User user = new User(userData.getCredentials(), userData.getProfile());
		return userMapper.toUserDto(userRepository.save(user));
	}

	public UserDto findByUserName(String userName){
		return userMapper.toUserDto(userRepository.findByUserName(userName));
	}

	@Transactional
	public UserDto activateUser(UserDto userDto) {
		User user = userRepository.findByUserName(userDto.getUserName());
		user.setDeleted(false);
		// activate all of the user's previous tweets
		List<Tweet> userTweets = tweetRepository.findByAuthorAndDeleted(user, true);
		for (Tweet t : userTweets){
			t.setDeleted(false);
			tweetRepository.save(t);
		}
		userRepository.saveAndFlush(user);
		return userMapper.toUserDto(user);
	}
	
	public boolean userIsDeleted(String userName){
		return findByUserName(userName).isDeleted();
	}
	
	@Transactional
	public UserDto updateUser(UserData userData) {
		User user = userRepository.findByUserName(userData.getUserName());

		user.getProfile().setEmail(userData.getProfile().getEmail());
		user.getProfile().setFirstName(userData.getProfile().getFirstName());
		user.getProfile().setLastName(userData.getProfile().getLastName());
		user.getProfile().setPhone(userData.getProfile().getPhone());
		userRepository.saveAndFlush(user);
		return userMapper.toUserDto(user);
	}

	/*public UserDto update(Integer id, Profile profile) {
		User updated = userRepository.get(id);
		updated.setProfile(profile);
		userRepository.update(updated);
		return userMapper.toUserDto(updated);
	}*/
	
	public boolean validatePassword(UserData userData) {
		User u = userRepository.findByUserName(userData.getUserName());
		return userData.getPassword().equals(u.getCredentials().getPassword());
	}

	public boolean validatePassword(Credentials credentials) {
		User u = userRepository.findByUserName(credentials.getUserLogin());
		return credentials.getPassword().equals(u.getCredentials().getPassword());
	}

	@Transactional
	public UserDto deleteUser(String userName) {
		User user = userRepository.findByUserName(userName);
		
		List<Tweet> userTweets = tweetRepository.findByAuthorAndDeleted(user, false);
		for (Tweet t : userTweets){
			t.setDeleted(true);
			tweetRepository.save(t);
		}
		user.setDeleted(true);
		userRepository.saveAndFlush(user);
		return userMapper.toUserDto(user);
	}

	/*public UserDto deleteUser(Credentials credentials) {
		User user = userRepository.findby_username(credentials.getUsername());
		user.setStatus(false);
		List<Tweet> deleteTweets = user.getAllTweets();
		for(Tweet t : deleteTweets) {
			t.setContent("");
			t.setDeleted(true);
		}
		return userMapper.toUserDto(user);
	}*/
	
	@Transactional
	public void follow(String followed, String follower) {
		User followedUser = userRepository.findByUserName(followed);
		User followerUser = userRepository.findByUserName(follower);
		followedUser.getFollowers().add(followerUser);
	}

	public Set<UserDto> getFollowers(String userName) {
		User user = userRepository.findByUserName(userName);
		return userMapper.toUserDtos(userRepository.findByFollowersAndDeleted(user, false));
	}
	
	public Set<UserDto> getFollowing(String userName) {
		User user = userRepository.findByUserName(userName);
		return userMapper.toUserDtos(userRepository.findByFollowingAndDeleted(user, false));
	}

	@Transactional
	public void unFollow(String followed, String follower) {
		User followedUser = userRepository.findByUserName(followed);
		User followerUser = userRepository.findByUserName(follower);
		followedUser.getFollowers().remove(followerUser);
	}

	public List<TweetDto> getFeed(String userName) {
		User user = userRepository.findByUserName(userName);	
		List<Tweet> feed = tweetRepository.findByAuthorAndDeleted(user, false);
		Set<User> isFollowing = userRepository.findByFollowingAndDeleted(user, false);
		for (User u : isFollowing){
			feed.addAll(tweetRepository.findByAuthorAndDeleted(u, false));
		}
		
		// Compare tweets' timestamps for sorting
		Comparator<Tweet> compareTweets = new Comparator<Tweet>(){
			public int compare(Tweet t1, Tweet t2){
				return -t1.getPosted().compareTo(t2.getPosted());
			}
		};
		Collections.sort(feed, compareTweets);
		return tweetMapper.toTweetDtos(feed);
	}
	
	/*public List<TweetDto> getFeed(String username){
		
		User user = userRepository.findby_username(username);
		List<Tweet> userFeed = new ArrayList<>();
		for(User f : user.getFollowing()) {
			userFeed.addAll(f.getAllTweets());
		}
		return userFeed.stream().map(tweetMapper::toTweetDto).collect(Collectors.toList());	
	
	}*/

	public List<TweetDto> getTweets(String userName) {
		User user = userRepository.findByUserName(userName);
		List<Tweet> tweets = tweetRepository.findByAuthorAndDeleted(user, false);
		return tweetMapper.toTweetDtos(tweets);
	}

	public List<TweetDto> getMentions(String userName) {
		User user = userRepository.findByUserName(userName);
		List<Tweet> tweets = tweetRepository.findByMentionedByAndDeleted(user, false);
		return tweetMapper.toTweetDtos(tweets);
	}
	
}
