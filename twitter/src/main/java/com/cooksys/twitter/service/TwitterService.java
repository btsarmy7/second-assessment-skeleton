package com.cooksys.twitter.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cooksys.twitter.Dto.HashtagDto;
import com.cooksys.twitter.Dto.TweetDto;
import com.cooksys.twitter.Dto.UserDto;
import com.cooksys.twitter.entity.Context;
import com.cooksys.twitter.entity.Credentials;
import com.cooksys.twitter.entity.Hashtag;
import com.cooksys.twitter.entity.Profile;
import com.cooksys.twitter.entity.Tweet;
import com.cooksys.twitter.entity.User;
import com.cooksys.twitter.exceptions.InvalidIdException;
import com.cooksys.twitter.mapper.HashtagMapper;
import com.cooksys.twitter.mapper.TweetMapper;
import com.cooksys.twitter.mapper.UserMapper;
import com.cooksys.twitter.repository.HashtagRepository;
import com.cooksys.twitter.repository.TweetRepository;
import com.cooksys.twitter.repository.UserRepository;

@Service
public class TwitterService {
	
	private HashtagRepository hashtagRepository;
	private UserRepository userRepository;
	private TweetRepository tweetRepository;
	
	private UserMapper userMapper;
	private TweetMapper tweetMapper;
	private HashtagMapper hashtagMapper;
	
	public TwitterService(HashtagRepository hashtagRepository, UserRepository userRepository, 
			TweetRepository tweetRepository, UserMapper userMapper, TweetMapper tweetMapper, HashtagMapper hashtagMapper) {
		
		this.hashtagRepository = hashtagRepository;
		this.userRepository = userRepository;
		this.tweetRepository = tweetRepository;
		this.userMapper = userMapper;
		this.tweetMapper = tweetMapper;
		this.hashtagMapper = hashtagMapper;
		
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

	public UserDto[] getAllActiveUsers() {
		UserDto[] allUsers = new UserDto[userRepository.findByStatusTrue().size()];
		List<UserDto> allUserDtos = userRepository.findByStatusTrue().stream().map(userMapper::toUserDto).collect(Collectors.toList());
		for(int i = 0; i < userRepository.findAll().size(); i++) {
			allUsers[i] = allUserDtos.get(i);
		}
		return allUsers;
	}

	@Transactional
	public UserDto addUser(Credentials credentials, Profile profile) throws InvalidIdException {
		if(availableUsername(credentials.getUsername())) {
			User user = new User(credentials.getUsername(), profile, new Timestamp(System.currentTimeMillis()), credentials);
			userRepository.saveAndFlush(user);
			return userMapper.toUserDto(user);
		}throw new InvalidIdException("Username not available");
		
		
	}

	public UserDto getUsername(String username) throws InvalidIdException {
		
		if(!availableUsername(username)) {
			User user = userRepository.findby_username(username);
			return userMapper.toUserDto(user);
		}throw new InvalidIdException("User does not exist");
		
	}

	@Transactional
	public UserDto updateUserProfile(Credentials credentials, Profile profile) throws InvalidIdException {
		
		if(isUsername(credentials.getUsername())) {
			User user = userRepository.findby_username(credentials.getUsername());
			user.setProfile(profile);
			User updatedUser = userRepository.saveAndFlush(user);
			return userMapper.toUserDto(updatedUser);
		} throw new InvalidIdException("User does not exist");
		
	}

	@Transactional
	public UserDto deleteUser(Credentials credentials) throws InvalidIdException {
		
		if (isUsername(credentials.getUsername())) {
			User user = userRepository.findby_username(credentials.getUsername());
			user.setStatus(false);
			List<Tweet> deleteTweets = user.getAllTweets();
			for(Tweet t : deleteTweets) {
				t.setContent("");
				t.setDeleted(true);
				tweetRepository.saveAndFlush(t);
			}
			return userMapper.toUserDto(user);
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


	public List<TweetDto> getFeed(String username) throws InvalidIdException {
		
		if(isUsername(username)) {
			User user = userRepository.findby_username(username);
			List<Tweet> userFeed = new ArrayList<>();
			for(User f : user.getFollowing()) {
				userFeed.addAll(f.getAllTweets());
			}
			return userFeed.stream().map(tweetMapper::toTweetDto).collect(Collectors.toList());
			
		} throw new InvalidIdException("User does not exist");
		
	}


	public List<TweetDto> getTweets(String username) throws InvalidIdException{
		List<Tweet> allTweets = userRepository.findby_username(username).getAllTweets();
		return allTweets.stream().map(tweetMapper :: toTweetDto).collect(Collectors.toList());
		
	}


	public List<Tweet> getMentions(String username) throws InvalidIdException {
		
		if(isUsername(username)) {
			User user = userRepository.findby_username(username);
			return user.getMentions();
		}throw new InvalidIdException("Not a user");
		
	}


	public List<UserDto> getFollowers(String username) throws InvalidIdException {
		
		if(isUsername(username)) {
			User user = userRepository.findby_username(username);
			return user.getFollowers().stream().map(userMapper :: toUserDto).collect(Collectors.toList());
		}throw new InvalidIdException("Not a user");
		
	}


	public List<UserDto> getFollowing(String username) throws InvalidIdException {
		if(isUsername(username)) {
			User user = userRepository.findby_username(username);
			return user.getFollowing().stream().map(userMapper :: toUserDto).collect(Collectors.toList());
		} throw new InvalidIdException("Not a user");
	}


	public List<HashtagDto> getHashtags() {
		return hashtagRepository.findAll().stream().map(hashtagMapper :: toHashtagDto).collect(Collectors.toList());
	}


	public List<TweetDto> getTagged(String label) throws InvalidIdException {
		if(hashtagRepository.findByHashtag(label) != null) {
		Hashtag h = hashtagRepository.findByHashtag(label);
		return h.getTweets().stream().map(tweetMapper :: toTweetDto).collect(Collectors.toList());
		}throw new InvalidIdException("Not a hashtag");
	}


	public List<TweetDto> getAllTweets() {
		return tweetRepository.findAll().stream().map(tweetMapper :: toTweetDto).collect(Collectors.toList());
	}


	public TweetDto postNewTweet(String content, Credentials credentials) throws InvalidIdException {
		
		if(isUsername(credentials.getUsername())) {
			User user = userRepository.findby_username(credentials.getUsername());
			Tweet newTweet = new Tweet(user, new Timestamp(System.currentTimeMillis()), content);
			newTweet.setDeleted(false);
			tweetRepository.saveAndFlush(newTweet);
			newTweet.setHashtags(findContentHashtags(newTweet));
			newTweet.setMentions(findMentions(newTweet));
			tweetRepository.saveAndFlush(newTweet);
			user.getAllTweets().add(newTweet);
			userRepository.saveAndFlush(user);
			return tweetMapper.toTweetDto(newTweet);
		}throw new InvalidIdException("Not a valid user, cannot post new tweet");
		
		
	}
	
	private List<Hashtag> findContentHashtags(Tweet tweet){
		
		if(tweet.equals(null))
			return null;
		List<Hashtag> hashtags = new ArrayList<>();
		String string = tweet.getContent();
		Pattern pattern = Pattern.compile("#(\\S+)");
		Matcher matcher = pattern.matcher(string);
		while(matcher.find()) {
			Hashtag h = new Hashtag();
			h.setLabel(matcher.group(1));
			if(hashtagRepository.findByHashtag(h.getLabel()) != null) {
				h.setFirstUsed(new Timestamp(System.currentTimeMillis()));
			}
			h.getTweets().add(tweet);
			h.setLastUsed(new Timestamp(System.currentTimeMillis()));
			hashtags.add(h);
		} return hashtags;
	}
	
	private List<User> findMentions(Tweet tweet){
		
		List<User> mentions = new ArrayList<>();
		String string = tweet.getContent();
		Pattern pattern = Pattern.compile("@(\\S+)");
		Matcher matcher = pattern.matcher(string);
		while(matcher.find()) {
			if(userRepository.findby_username(matcher.group(1)) == null);
			else {
				User user = userRepository.findby_username(matcher.group(1));
				mentions.add(user);
				user.getMentions().add(tweet);
				userRepository.saveAndFlush(user);				
			}
		} return mentions;
	}


	public TweetDto getTweet(Integer id) throws InvalidIdException {
		
		if(tweetRepository.findByIdAndDeletedFalse(id) != null) {
			Tweet tweet = tweetRepository.findById(id);
			if(tweet != null) { 
				return tweetMapper.toTweetDto(tweet);
			}
		}throw new InvalidIdException("No such tweet");
	
	}

	public TweetDto deleteUserTweet(Integer id, Credentials credentials) throws InvalidIdException {
		
		Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
		if(tweet == null) {
			throw new InvalidIdException("No such tweet");
		}
		if(!credentials.getUsername().equals(tweet.getAuthor().getUsername())){
			throw new InvalidIdException("Not author of tweet");
		}
		tweet.setDeleted(true);
		return tweetMapper.toTweetDto(tweet);
	}


	public void likedTweets(Integer id, Credentials credentials) throws InvalidIdException {
		
		Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
		User user = userRepository.findby_username(credentials.getUsername());
		if(user == null) {
			throw new InvalidIdException("Not a user");
		}
		tweet.getLikes().add(user);
		tweetRepository.saveAndFlush(tweet);
		user.getLikedTweets().add(tweet);
		userRepository.saveAndFlush(user);
	}


	public TweetDto reply(Integer id, String content, Credentials credentials) throws InvalidIdException {
		
		if(!isUsername(credentials.getUsername())) {
			throw new InvalidIdException("Not a valid user");
		}
		User user = userRepository.findby_username(credentials.getUsername());
		Tweet newTweet = new Tweet(user, new Timestamp(System.currentTimeMillis()), content);
		newTweet.setDeleted(false);
		newTweet.setHashtags(findContentHashtags(newTweet));
		tweetRepository.saveAndFlush(newTweet);
		Tweet tweet = tweetMapper.toTweet(getTweet(id));
		tweet.setInReplyto(newTweet);
		tweetRepository.saveAndFlush(tweet);
		return tweetMapper.toTweetDto(newTweet);
				
	}


	public TweetDto repostTweet(Integer id, Credentials credentials) throws InvalidIdException {
		
		if(!isUsername(credentials.getUsername())) {
			throw new InvalidIdException("Not a valid user");
		}
		User user = userRepository.findby_username(credentials.getUsername());
		Tweet originalTweet = tweetMapper.toTweet(getTweet(id));
		Tweet repost = new Tweet(user, new Timestamp(System.currentTimeMillis()), null);
		repost.setDeleted(false);
		repost.setRepostOf(originalTweet);
		tweetRepository.saveAndFlush(repost);
		return tweetMapper.toTweetDto(repost);
		
	}

	public List<HashtagDto> userTaggedTweets(Integer id) throws InvalidIdException {
		if(tweetRepository.findByIdAndDeletedFalse(id) == null)
			throw new InvalidIdException("Not an existing tweet");
		else {
			Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
			return tweet.getHashtags().stream().map(hashtagMapper :: toHashtagDto).collect(Collectors.toList());
			}
	}


	public List<UserDto> usersLiked(Integer id) throws InvalidIdException {
		 if(tweetRepository.findByIdAndDeletedFalse(id) == null )
			 throw new InvalidIdException("Not an existing tweet");
		 else {
			 Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
			 return tweet.getLikes().stream().map(userMapper :: toUserDto).collect(Collectors.toList());
		 }
	}


	public Context tweetContext(Integer id) throws InvalidIdException {
		if(tweetRepository.findByIdAndDeletedFalse(id) == null )
			 throw new InvalidIdException("Not an existing tweet");
		return null;
	}


	public List<Tweet> userReplies(Integer id) throws InvalidIdException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
