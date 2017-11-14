package com.cooksys.twitter.controller;



import java.util.List;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter.Dto.HashtagDto;
import com.cooksys.twitter.Dto.TweetDto;
import com.cooksys.twitter.Dto.UserDto;
import com.cooksys.twitter.entity.Credentials;
import com.cooksys.twitter.entity.Profile;
import com.cooksys.twitter.entity.Tweet;
import com.cooksys.twitter.entity.User;
import com.cooksys.twitter.exceptions.InvalidIdException;
import com.cooksys.twitter.service.TwitterService;



@RestController
@RequestMapping("twitter")
public class TwitterController {
	
	private TwitterService twitterService;

	public TwitterController(TwitterService twitterService) {
		this.twitterService = twitterService;
	}
	
	@GetMapping("/validate/tag/exists/{label}")
	public boolean isHashtag(@PathVariable String label) {
		return twitterService.isHashtag(label);
	}
	
	@GetMapping("/validate/username/exists/{username}")
	public boolean isUsername(@PathVariable String username) {
		return twitterService.isUsername(username);
	}
	
	@GetMapping("/validate/username/available/{username}")
	public boolean availableUsername(@PathVariable String username) {
		return twitterService.availableUsername(username);
	}
	
	@GetMapping("/users")
	public List<User> getAllUsers() {
		return twitterService.getAllActiveUsers();
	}
	
	@PostMapping("/users/{credentials}/{profile}")
	public void addUser(@RequestParam Credentials credentials, @RequestParam Profile profile, HttpServletResponse response) {
		try {
			twitterService.addUser(credentials, profile);
		} catch (InvalidIdException e) {
			response.setStatus(e.STATUS_CODE);
		}
	}
	
	@GetMapping("/users/{username}")
	public User getUsername(@PathVariable String username) {
		
		try {
				return twitterService.getUsername(username);
		} catch (InvalidIdException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@PatchMapping("/users/{username}")
	public User updateUser(@PathVariable String username, @RequestBody User user) throws InvalidIdException {
		return twitterService.updateUserProfile(user.getCredentials(), user.getProfile());
	}
	
	//delete user
	@DeleteMapping("/users/{username}")
	public User deleteUser(@PathVariable String username, @RequestBody Credentials credentials) throws InvalidIdException {
		return twitterService.deleteUser(credentials);
	}
	
	@PostMapping("/users/{username}/follow")
	public void followUser(@PathVariable String username, @RequestBody Credentials credentials) throws InvalidIdException {
		twitterService.follow(username, credentials);
	}
	
	@PostMapping("/users/{username}/unfollow")
	public void unfollowUser(@PathVariable String username, @RequestBody Credentials credentials) throws InvalidIdException {
		twitterService.unfollow(username, credentials);
	}
	
	@GetMapping("/users/{username}/feed")
	public List<Tweet> getFeed(@PathVariable String username) throws InvalidIdException {
		return twitterService.getFeed(username);
	}
	
	@GetMapping("/users/{username}/tweets")
	public List<Tweet> getTweets(@RequestParam String username) throws InvalidIdException {
		return twitterService.getTweets(username);
	}
	
	@GetMapping("/users/@{Username}/mentions")
	public List<TweetDto> getMentions(@RequestParam String username){
		return null;
	}
	
	@GetMapping("/users/@{Username}/followers")
	public List<UserDto> getFollowers(@RequestParam String username){
		return null;
	}
	
	@GetMapping("/users/@{Username}/following")
	public List<UserDto> getFollowing(@RequestParam String username){
		return null;
	}
	
	@GetMapping("/tags")
	public List<HashtagDto> getAllHashtags(@PathVariable String tags){
		return null;
	}
	
	@GetMapping("/tags/{label}")
	public List<TweetDto> getTagged(@PathVariable String tags, @RequestParam String label){
		return null;
	}
	
	@GetMapping("/tweets")
	public List<TweetDto> getAllTweets(@PathVariable String tweets){
		return null;
	}
	
	@PostMapping("/tweets/{content}/{credentials}")
	public TweetDto postTweet(@PathVariable String tweets, @RequestParam String content, @RequestParam Credentials credentials) {
		return null;
	}
	
	@GetMapping("/tweets/{id}")
	public TweetDto getUserTweet(@PathVariable String tweets, @RequestParam Integer id) {
		return null;
	}
	
	//delete tweet
	
	@PostMapping("/tweets/{id}/like")
	public void likeTweet(@PathVariable String tweets, @RequestParam Integer id) {
		
	}
	
	@PostMapping("/tweets/{id}/reply")
	public TweetDto replyTweet(@PathVariable String tweets, @RequestParam Integer id) {
		return null;
	}
	
	@PostMapping("/tweets/{id}/repost")
	public TweetDto repostTweet(@PathVariable String tweets, @RequestParam Integer id) {
		return null;
	}
	
	@GetMapping("/tweets/{id}/tags")
	public List<HashtagDto> userTaggedTweet(@PathVariable String tweets, @RequestParam Integer id) {
		return null;
	}
	
	@GetMapping("/tweets/{id}/likes")
	public List<UserDto> usersLiked(@PathVariable String tweets, @RequestParam Integer id) {
		return null;
	}
}
