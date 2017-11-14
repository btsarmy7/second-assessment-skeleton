package com.cooksys.twitter.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cooksys.twitter.entity.Credentials;
import com.cooksys.twitter.entity.Hashtag;
import com.cooksys.twitter.entity.Profile;
import com.cooksys.twitter.entity.Tweet;
import com.cooksys.twitter.entity.User;
import com.cooksys.twitter.exceptions.InvalidIdException;
import com.cooksys.twitter.repository.HashtagRepository;
import com.cooksys.twitter.repository.TweetRepository;
import com.cooksys.twitter.repository.UserRepository;

@Service
public class TwitterService {
	
	private HashtagRepository hashtagRepository;
	private UserRepository userRepository;
	private TweetRepository tweetRepository;
	
	public TwitterService(HashtagRepository hashtagRepository,
			UserRepository userRepository, TweetRepository tweetRepository) {
		
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

	@Transactional
	public User addUser(Credentials credentials, Profile profile) throws InvalidIdException {
		if(availableUsername(credentials.getUsername())) {
			User user = new User(credentials.getUsername(), profile, new Timestamp(System.currentTimeMillis()), credentials);
			return userRepository.saveAndFlush(user);
		}throw new InvalidIdException("Username not available");
		
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
		}throw new InvalidIdException("Not a user");
		
	}


	public List<User> getFollowers(String username) throws InvalidIdException {
		
		if(isUsername(username)) {
			User user = userRepository.findby_username(username);
			return user.getFollowers();
		}throw new InvalidIdException("Not a user");
		
	}


	public List<User> getFollowing(String username) throws InvalidIdException {
		if(isUsername(username)) {
			User user = userRepository.findby_username(username);
			return user.getFollowing();
		} throw new InvalidIdException("Not a user");
	}


	public List<Hashtag> getHashtags() {
		return hashtagRepository.findAll();
	}


	public List<Tweet> getTagged(String label) throws InvalidIdException {
		if(hashtagRepository.findByHashtag(label) != null) {
		Hashtag h = hashtagRepository.findByHashtag(label);
		return h.getTweets();
		}throw new InvalidIdException("Not a hashtag");
	}


	public List<Tweet> getAllTweets() {
		return tweetRepository.findAll();
	}


	public Tweet postNewTweet(String content, Credentials credentials) throws InvalidIdException {
		
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
			return newTweet;
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


	public Tweet getTweet(Integer id) throws InvalidIdException {
		
		if(tweetRepository.findByIdAndDeletedFalse(id) != null) {
			Tweet tweet = tweetRepository.findById(id);
			if(tweet != null) 
				return tweet;
		}throw new InvalidIdException("No such tweet");
	
	}

	public Tweet deleteUserTweet(Integer id, Credentials credentials) throws InvalidIdException {
		
		Tweet tweet = getTweet(id);
		if(tweet == null) {
			throw new InvalidIdException("No such tweet");
		}
		if(!credentials.getUsername().equals(tweet.getAuthor().getUsername())){
			throw new InvalidIdException("Not author of tweet");
		}
		tweet.setDeleted(true);
		return tweet;
	}


	public void likedTweets(Integer id, Credentials credentials) throws InvalidIdException {
		
		Tweet tweet = getTweet(id);
		User user = userRepository.findby_username(credentials.getUsername());
		if(user == null) {
			throw new InvalidIdException("Not a user");
		}
		tweet.getLikes().add(user);
		tweetRepository.saveAndFlush(tweet);
		user.getLikedTweets().add(tweet);
		userRepository.saveAndFlush(user);
	}


	public Tweet reply(Integer id, String content, Credentials credentials) throws InvalidIdException {
		
		if(!isUsername(credentials.getUsername())) {
			throw new InvalidIdException("Not a valid user");
		}
		User user = userRepository.findby_username(credentials.getUsername());
		Tweet newTweet = new Tweet(user, new Timestamp(System.currentTimeMillis()), content);
		newTweet.setDeleted(false);
		newTweet.setHashtags(findContentHashtags(newTweet));
		tweetRepository.saveAndFlush(newTweet);
		Tweet tweet = getTweet(id);
		tweet.setInReplyto(newTweet);
		tweetRepository.saveAndFlush(tweet);
		return newTweet;
				
	}


	public Tweet repostTweet(Integer id, Credentials credentials) throws InvalidIdException {
		
		if(!isUsername(credentials.getUsername())) {
			throw new InvalidIdException("Not a valid user");
		}
		User user = userRepository.findby_username(credentials.getUsername());
		Tweet originalTweet = getTweet(id);
		Tweet repost = new Tweet(user, new Timestamp(System.currentTimeMillis()), null);
		repost.setDeleted(false);
		repost.setRepostOf(originalTweet);
		tweetRepository.saveAndFlush(repost);
		return repost;
		
	}
	
	
	
	
}
