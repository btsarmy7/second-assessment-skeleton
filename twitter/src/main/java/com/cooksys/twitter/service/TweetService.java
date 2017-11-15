package com.cooksys.twitter.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooksys.twitter.controller.UserController;
import com.cooksys.twitter.dto.UserDto;
import com.cooksys.twitter.dto.HashtagDto;
import com.cooksys.twitter.dto.TweetDto;
import com.cooksys.twitter.embedded.Context;
import com.cooksys.twitter.embedded.TweetData;
import com.cooksys.twitter.entity.User;
import com.cooksys.twitter.exceptions.InvalidIdException;
import com.cooksys.twitter.entity.Hashtag;
import com.cooksys.twitter.entity.Tweet;
import com.cooksys.twitter.mapper.UserMapper;
import com.cooksys.twitter.mapper.HashtagMapper;
import com.cooksys.twitter.mapper.TweetMapper;
import com.cooksys.twitter.repository.UserRepository;
import com.cooksys.twitter.repository.HashtagRepository;
import com.cooksys.twitter.repository.TweetRepository;

@Service
public class TweetService {

	private HashtagService hashtagService;
	private UserController userController;
	private UserRepository userRepository;
	private TweetRepository tweetRepository;
	private HashtagRepository hashtagRepository;
	private TweetMapper tweetMapper;
	private HashtagMapper hashtagMapper;
	private UserMapper userMapper;

	public TweetService(HashtagService hashtagService, UserController userController,
			UserRepository userRepository, TweetRepository tweetRepository, HashtagRepository hashtagRepository,
			TweetMapper tweetMapper, HashtagMapper hashtagMapper, UserMapper userMapper) {
		this.hashtagService = hashtagService;
		this.userController = userController;
		this.userRepository = userRepository;
		this.tweetRepository = tweetRepository;
		this.hashtagRepository = hashtagRepository;
		this.tweetMapper = tweetMapper;
		this.hashtagMapper = hashtagMapper;
		this.userMapper = userMapper;
	}

	@Transactional
	public TweetDto createSimpleTweet(TweetData simpleTweetData) throws InvalidIdException{
		User author= userRepository.findByUserName(simpleTweetData.getCredentials().getUserLogin());
		String content = simpleTweetData.getContent();
		Tweet tweet = new Tweet(false);
		tweetRepository.saveAndFlush(tweet);
		tweet.setPosted(new Timestamp(System.currentTimeMillis()));
		tweet.setContent(content);
		hashtagsInTweet(tweet, content);
		mentionsInTweet(tweet, content);
		tweet.setAuthor(author);
		return tweetMapper.toTweetDto(tweetRepository.save(tweet));
	}

	public List<TweetDto> findByHashtags(String hashtagName) {

		Hashtag tag = hashtagRepository.findByHashtagName(hashtagName);
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		tweets.addAll(tweetRepository.findByHashtagsAndDeleted(tag, false));
		Comparator<Tweet> compareTweets = new Comparator<Tweet>(){
			public int compare(Tweet t1, Tweet t2){
				return -t1.getPosted().compareTo(t2.getPosted());
			}
		};
		Collections.sort(tweets, compareTweets);
		return tweetMapper.toTweetDtos(tweets);
	}

	public List<TweetDto> getTweets() {
		List<Tweet> allTweets = tweetRepository.findAll(new Sort(Sort.Direction.DESC, "posted"));
		List<Tweet> returnedTweets = new ArrayList<Tweet>();

		for (Tweet t : allTweets){
			if (!t.isDeleted())
				returnedTweets.add(t);
		}
		return tweetMapper.toTweetDtos(returnedTweets);

	}

	public TweetDto getTweetById(Integer id) {
		return tweetMapper.toTweetDto(tweetRepository.findByIdAndDeleted(id, false));
	}

	@Transactional
	public TweetDto deleteTweetById(Integer id) {
		Tweet tweet = tweetRepository.findById(id);
		if (tweet == null || tweet.isDeleted())
			return null;
		tweet.setDeleted(true);
		return tweetMapper.toTweetDto(tweet);
	}

	public boolean tweetExists(Integer id){
		return getTweetById(id) != null;
	}

	@Transactional
	public void like(Integer id, String userName) {
		Tweet tweet = tweetRepository.findById(id);
		User u = userRepository.findByUserName(userName);
		u.getLikes().add(tweet);
	}

	@Transactional
	public TweetDto replyTo(Integer id, TweetData tweetData) {
		User author= userRepository.findByUserName(tweetData.getCredentials().getUserLogin());
		String content = tweetData.getContent();
		Tweet tweet = new Tweet(false);
		tweetRepository.saveAndFlush(tweet);
		Tweet inReplyTo = tweetRepository.findById(id);

		tweet.setPosted(new Timestamp(System.currentTimeMillis()));
		tweet.setContent(content);
		tweet = hashtagsInTweet(tweet, content);
		tweet = mentionsInTweet(tweet, content);
		tweet.setInReplyTo(inReplyTo);
		tweet.setAuthor(author);
		return tweetMapper.toTweetDto(tweetRepository.save(tweet));
	}

	private Tweet hashtagsInTweet(Tweet tweet, String content){
		String hashtag;
		Hashtag tag;
		Pattern p = Pattern.compile("#\\w*");
		Matcher m = p.matcher(content);
		while(m.find()){
			hashtag = m.group().substring(1);
			System.out.println(hashtag);
			if (!hashtagService.tagExists(hashtag)){
				tag = hashtagService.create(hashtag);
			} else {
				tag = hashtagRepository.findByHashtagName(hashtag);
			}
			tweet.getHashtags().add(tag);
		}
		return tweet;
	}
	
	/*private List<Hashtag> findContentHashtags(Tweet tweet){
	
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
	}*/
	
	

	private Tweet mentionsInTweet(Tweet tweet, String content){
		String userName;
		User usr;
		Pattern p = Pattern.compile("@\\w*");
		Matcher m = p.matcher(content);		
		while(m.find()){
			userName = m.group().substring(1);
			if (userController.validUser(userName)){
				usr = userRepository.findByUserName(userName);
				tweet.getMentionedBy().add(usr);
			}
		}
		return tweet;
	}
	
	/*private List<User> findMentions(Tweet tweet){
	
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
			}
		} return mentions;
	}*/

	@Transactional
	public TweetDto repost(Integer id, String userName) {
		User author = userRepository.findByUserName(userName);
		Tweet tweet = new Tweet(false);
		tweetRepository.saveAndFlush(tweet);
		Tweet repostOf = tweetRepository.findById(id);

		tweet.setPosted(new Timestamp(System.currentTimeMillis()));
		tweet.setRepostOf(repostOf);
		tweet.setContent(repostOf.getContent());
		tweet.setAuthor(author);
		return tweetMapper.toTweetDto(tweetRepository.save(tweet));
	}

	public Set<HashtagDto> getTagsByTweet(Integer id) {
		Tweet tweet = tweetRepository.findById(id);
		return hashtagMapper.toHashtagDtos(tweet.getHashtags());
	}

	public Set<UserDto> getLikesByTweet(Integer id) {
		Tweet tweet = tweetRepository.findById(id);
		return userMapper.toUserDtos(userRepository.findByLikesAndDeleted(tweet, false));
	}

	public List<TweetDto> getRepliesByTweet(Integer id) {
		Tweet tweet = tweetRepository.findById(id);
		return tweetMapper.toTweetDtos(tweetRepository.findByinReplyToAndDeleted(tweet, false));
}

	public List<UserDto> getMentionsByTweet(Integer id) {
		Tweet tweet = tweetRepository.findById(id);
		return userMapper.toUserDtos(userRepository.findByMentionsAndDeleted(tweet, false));
	}

	public List<TweetDto> getRepostsByTweet(Integer id) {
		Tweet tweet = tweetRepository.findById(id);
		List<Tweet> tweets = tweetRepository.findByRepostOfAndDeleted(tweet, false);
		return tweetMapper.toTweetDtos(tweets);
	}

	public Context getContext(Integer id) {
		
		Tweet target = tweetRepository.findById(id);
		Tweet tweet = target;
		Context context = new Context(tweetMapper.toTweetDto(target));
		ArrayList<Tweet> before = new ArrayList<Tweet>();
		ArrayList<Tweet> after = new ArrayList<Tweet>();
		// Find before tweets
		Tweet t = null;
		while (tweet.getInReplyTo() != null){
				t = tweet.getInReplyTo();
			if (!t.isDeleted())
				before.add(t);
			tweet = t;
		}
		// Sort by timestamp
		Comparator<Tweet> compareTweets = new Comparator<Tweet>(){
			public int compare(Tweet t1, Tweet t2){
				return -t1.getPosted().compareTo(t2.getPosted());
			}
		};
		Collections.sort(before, compareTweets);
		context.setBefore(tweetMapper.toTweetDtos(before));

		// look through after tweets
		tweetsAfter(target, after);
		Collections.sort(after, compareTweets);
		context.setAfter(tweetMapper.toTweetDtos(after));
		return context;
	}

	private void tweetsAfter(Tweet current, ArrayList<Tweet> after) {
		for (Tweet tweet : tweetRepository.findByinReplyTo(current)){
			if (!tweet.isDeleted())
				after.add(tweet);
			tweetsAfter(tweet, after);
		}
	}
}
