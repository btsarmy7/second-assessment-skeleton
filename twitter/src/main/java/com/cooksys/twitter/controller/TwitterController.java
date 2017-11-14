package com.cooksys.twitter.controller;



import java.util.List;


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
import com.cooksys.twitter.entity.Context;
import com.cooksys.twitter.entity.Credentials;
import com.cooksys.twitter.entity.Hashtag;
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
	public UserDto[] getAllUsers() {
		return twitterService.getAllActiveUsers();
	}
	
	@PostMapping("/users")
	public UserDto addUser(@RequestBody Credentials credentials, @RequestBody Profile profile, HttpServletResponse response) {
		try {
			return twitterService.addUser(credentials, profile);
		} catch (InvalidIdException e) {
			response.setStatus(e.STATUS_CODE);
		} return null;
	}
	
	@GetMapping("/users/{username}")
	public UserDto getUsername(@PathVariable String username) {
		
		try {
				return twitterService.getUsername(username);
		} catch (InvalidIdException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@PatchMapping("/users/{username}")
	public UserDto updateUser(@PathVariable String username, @RequestBody User user) throws InvalidIdException {
		return twitterService.updateUserProfile(user.getCredentials(), user.getProfile());
	}
	
	//delete user
	@DeleteMapping("/users/{username}")
	public UserDto deleteUser(@PathVariable String username, @RequestBody Credentials credentials) throws InvalidIdException {
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
	public List<TweetDto> getFeed(@PathVariable String username) throws InvalidIdException {
		return twitterService.getFeed(username);
	}
	
	@GetMapping("/users/{username}/tweets")
	public List<TweetDto> getTweets(@RequestParam String username) throws InvalidIdException {
		return twitterService.getTweets(username);
	}
	
	@GetMapping("/users/{username}/mentions")
	public List<Tweet> getMentions(@PathVariable String username) throws InvalidIdException {
		return twitterService.getMentions(username);
	}
	
	@GetMapping("/users/{username}/followers")
	public List<UserDto> getFollowers(@RequestParam String username) throws InvalidIdException {
		return twitterService.getFollowers(username);
	}
	
	@GetMapping("/users/{username}/following")
	public List<UserDto> getFollowing(@PathVariable String username) throws InvalidIdException {
		return twitterService.getFollowing(username);
	}
	
	@GetMapping("/tags")
	public List<HashtagDto> getAllHashtags(){
		return twitterService.getHashtags();
	}
	
	@GetMapping("/tags/{label}")
	public List<TweetDto> getTagged(@PathVariable String label) throws InvalidIdException {
		return twitterService.getTagged(label);
	}
	
	@GetMapping("/tweets")
	public List<TweetDto> getAllTweets(){
		return twitterService.getAllTweets();
	}
	
	@PostMapping("/tweets")
	public TweetDto postTweet(@RequestBody String content, @RequestBody Credentials credentials) throws InvalidIdException {
		return twitterService.postNewTweet(content, credentials);
	}
	
	@GetMapping("/tweets/{id}")
	public TweetDto getTweet(@PathVariable String id) throws InvalidIdException {
		return twitterService.getTweet(Integer.parseInt(id));
	}
	
	//delete tweet
	@DeleteMapping("/{id}")
	public TweetDto deleteTweet(@PathVariable String id, @RequestBody Credentials credentials) throws InvalidIdException {
		return twitterService.deleteUserTweet(Integer.parseInt(id), credentials);		
	}
	
	@PostMapping("/tweets/{id}/like")
	public void likeTweet(@PathVariable String id, @RequestBody Credentials credentials) throws InvalidIdException {
		twitterService.likedTweets(Integer.parseInt(id), credentials);
	}
	
	@PostMapping("/tweets/{id}/reply")
	public TweetDto replyTweet(@PathVariable String id, @RequestBody String content, @RequestBody Credentials credentials) throws InvalidIdException {
		return twitterService.reply(Integer.parseInt(id), content, credentials);
	}
	
	@PostMapping("/tweets/{id}/repost")
	public TweetDto repostTweet(@PathVariable String id, @RequestBody Credentials credentials) throws InvalidIdException {
		return twitterService.repostTweet(Integer.parseInt(id), credentials);
	}
	
	@GetMapping("/tweets/{id}/tags")
	public List<HashtagDto> userTaggedTweet(@PathVariable String id) throws InvalidIdException {
		return twitterService.userTaggedTweets(Integer.parseInt(id));
	}
	
	@GetMapping("/tweets/{id}/likes")
	public List<UserDto> usersLiked(@PathVariable String id) throws InvalidIdException {
		return twitterService.usersLiked(Integer.parseInt(id));
	}
	
	@GetMapping("/tweets/{id}/context")
	public Context tweetContext(@PathVariable String id) throws InvalidIdException {
		return twitterService.tweetContext(Integer.parseInt(id));
	}
	
	@GetMapping("/tweets/{id}/replies")
	public List<Tweet> userReplies(@PathVariable String id) throws InvalidIdException {
		return twitterService.userReplies(Integer.parseInt(id));
	}
	
	@GetMapping("/tweets/{id}/reposts")
	public Context userReposts(@PathVariable String id) {
		return null;
	}
	
	@GetMapping("/tweets/{id}/mentions")
	public Context userMentions(@PathVariable String id) {
		return null;
	}
}
