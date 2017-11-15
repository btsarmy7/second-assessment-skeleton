package com.cooksys.twitter.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksys.twitter.Dto.HashtagDto;
import com.cooksys.twitter.Dto.TweetDto;
import com.cooksys.twitter.Dto.UserDto;
import com.cooksys.twitter.embedded.Credentials;
import com.cooksys.twitter.entity.Context;
import com.cooksys.twitter.entity.Hashtag;
import com.cooksys.twitter.entity.Tweet;
import com.cooksys.twitter.entity.User;
import com.cooksys.twitter.mapper.HashtagMapper;
import com.cooksys.twitter.mapper.TweetMapper;
import com.cooksys.twitter.mapper.UserMapper;
import com.cooksys.twitter.repository.HashtagRepository;
import com.cooksys.twitter.repository.TweetRepository;
import com.cooksys.twitter.repository.UserRepository;

@Service
public class TweetService {
	
	private HashtagRepository hashtagRepository;
	private UserRepository userRepository;
	private TweetRepository tweetRepository;
	
	private UserMapper userMapper;
	private TweetMapper tweetMapper;
	private HashtagMapper hashtagMapper;
	
	public TweetService(HashtagRepository hashtagRepository, UserRepository userRepository, 
			TweetRepository tweetRepository, UserMapper userMapper, TweetMapper tweetMapper, HashtagMapper hashtagMapper) {
		
		this.hashtagRepository = hashtagRepository;
		this.userRepository = userRepository;
		this.tweetRepository = tweetRepository;
		this.userMapper = userMapper;
		this.tweetMapper = tweetMapper;
		this.hashtagMapper = hashtagMapper;
		
	}

	public List<TweetDto> getAllTweets() {
		return tweetRepository.findAll().stream().map(tweetMapper :: toTweetDto).collect(Collectors.toList());
	}


	public TweetDto postNewTweet(String content, Credentials credentials){
	
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
			if(hashtagRepository.findByLabel(h.getLabel()) != null) {
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


	public TweetDto getTweet(Integer id){	
		
			Tweet tweet = tweetRepository.findById(id);
			return tweetMapper.toTweetDto(tweet);
			
	
	}

	public TweetDto deleteUserTweet(Integer id, Credentials credentials){
		
		Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
		tweet.setDeleted(true);
		return tweetMapper.toTweetDto(tweet);
	}


	public void likedTweets(Integer id, Credentials credentials){
		
		Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
		User user = userRepository.findby_username(credentials.getUsername());
		tweet.getLikes().add(user);
		tweetRepository.saveAndFlush(tweet);
		user.getLikedTweets().add(tweet);
		userRepository.saveAndFlush(user);
	}


	public TweetDto reply(Integer id, String content, Credentials credentials){
		
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


	public TweetDto repostTweet(Integer id, Credentials credentials){
		
		User user = userRepository.findby_username(credentials.getUsername());
		Tweet originalTweet = tweetMapper.toTweet(getTweet(id));
		Tweet repost = new Tweet(user, new Timestamp(System.currentTimeMillis()), null);
		repost.setDeleted(false);
		repost.setRepostOf(originalTweet);
		tweetRepository.saveAndFlush(repost);
		return tweetMapper.toTweetDto(repost);
		
	}

	public List<HashtagDto> userTaggedTweets(Integer id){
			Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
			return tweet.getHashtags().stream().map(hashtagMapper :: toHashtagDto).collect(Collectors.toList());
			
	}


	public List<UserDto> usersLiked(Integer id){
		
			 Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
			 return tweet.getLikes().stream().map(userMapper :: toUserDto).collect(Collectors.toList());
		 
	}


	public Context tweetContext(Integer id){
		return null;
	}


	public List<TweetDto> userReplies(Integer id){
			Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
			return tweet.getReplies().stream().map(tweetMapper :: toTweetDto).collect(Collectors.toList());
		
	}


	public List<TweetDto> userReposts(Integer id){
			Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
			return tweet.getReposts().stream().map(tweetMapper :: toTweetDto).collect(Collectors.toList());
		
	}


	public List<UserDto> userMentions(Integer id){
			
			 Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);
			 List<User> usersMentioned = findMentions(tweet);
			 return usersMentioned.stream().map(userMapper :: toUserDto).collect(Collectors.toList());
	}
	
}
