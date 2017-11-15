package com.cooksys.twitter.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter.Dto.HashtagDto;
import com.cooksys.twitter.Dto.TweetDto;
import com.cooksys.twitter.Dto.UserDto;
import com.cooksys.twitter.embedded.Credentials;
import com.cooksys.twitter.embedded.TweetInfo;
import com.cooksys.twitter.entity.Context;
import com.cooksys.twitter.entity.Tweet;
import com.cooksys.twitter.repository.TweetRepository;
import com.cooksys.twitter.repository.UserRepository;
import com.cooksys.twitter.service.TweetService;

@RestController
@RequestMapping("tweets")
public class TweetController {
	
	private TweetService tweetService;
	private TweetRepository tweetRepository;
	
	public TweetController(TweetService tweetService, UserRepository userRespository, TweetRepository tweetRepository) {
		this.tweetService = tweetService;
		this.tweetRepository = tweetRepository;
	}
	
	@GetMapping
	public List<TweetDto> getAllTweets(){
		return tweetService.getAllTweets();
	}
	
	@PostMapping
	public TweetDto postTweet(@RequestBody TweetInfo tweetInfo, HttpServletResponse response){
		if(tweetInfo.getContent() == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return tweetService.postNewTweet(tweetInfo.getContent(), tweetInfo.getCredentials());
	}
	
	@GetMapping("/{id}")
	public TweetDto getTweet(@PathVariable String id, HttpServletResponse response) {
		Tweet tweet = tweetRepository.findByIdAndDeletedFalse(Integer.parseInt(id));
		if(tweet == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return tweetService.getTweet(Integer.parseInt(id));
	}
	
	//delete tweet
	@DeleteMapping("/{id}")
	public TweetDto deleteTweet(@PathVariable Integer id, @RequestBody Credentials credentials, HttpServletResponse response){
		if(tweetRepository.findByIdAndDeletedFalse(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return tweetService.deleteUserTweet(id, credentials);		
	}
	
	@PostMapping("/{id}/like")
	public void likeTweet(@PathVariable Integer id, @RequestBody Credentials credentials, HttpServletResponse response){
		if(tweetRepository.findByIdAndDeletedFalse(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		tweetService.likedTweets(id, credentials);
	}
	
	@PostMapping("/{id}/reply")
	public TweetDto replyTweet(@PathVariable Integer id, @RequestBody TweetInfo tweetInfo, HttpServletResponse response){
		if(tweetRepository.findByIdAndDeletedFalse(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return tweetService.reply(id, tweetInfo.getContent(), tweetInfo.getCredentials());
	}
	
	@PostMapping("/{id}/repost")
	public TweetDto repostTweet(@PathVariable Integer id, @RequestBody Credentials credentials, HttpServletResponse response) {
		if(tweetRepository.findByIdAndDeletedFalse(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return tweetService.repostTweet(id, credentials);
	}
	
	@GetMapping("/{id}/tags")
	public List<HashtagDto> userTaggedTweet(@PathVariable Integer id, HttpServletResponse response){
		if(tweetRepository.findByIdAndDeletedFalse(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return tweetService.userTaggedTweets(id);
	}
	
	@GetMapping("/{id}/likes")
	public List<UserDto> usersLiked(@PathVariable Integer id, HttpServletResponse response) {
		if(tweetRepository.findByIdAndDeletedFalse(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return tweetService.usersLiked(id);
	}
	
	@GetMapping("/{id}/context")
	public Context tweetContext(@PathVariable Integer id, HttpServletResponse response){
		if(tweetRepository.findByIdAndDeletedFalse(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return tweetService.tweetContext(id);
	}
	
	@GetMapping("/{id}/replies")
	public List<TweetDto> userReplies(@PathVariable Integer id, HttpServletResponse response){
		if(tweetRepository.findByIdAndDeletedFalse(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return tweetService.userReplies(id);
	}
	
	@GetMapping("/{id}/reposts")
	public List<TweetDto> userReposts(@PathVariable Integer id, HttpServletResponse response){
		if(tweetRepository.findByIdAndDeletedFalse(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return tweetService.userReposts(id);
	}
	
	@GetMapping("/{id}/mentions")
	public List<UserDto> userMentions(@PathVariable Integer id, HttpServletResponse response){
		if(tweetRepository.findByIdAndDeletedFalse(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return tweetService.userMentions(id);
	}

}
