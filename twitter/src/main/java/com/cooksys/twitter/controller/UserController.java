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

import com.cooksys.twitter.Dto.TweetDto;
import com.cooksys.twitter.Dto.UserDto;
import com.cooksys.twitter.embedded.Credentials;
import com.cooksys.twitter.embedded.UserInfo;
import com.cooksys.twitter.entity.Tweet;
import com.cooksys.twitter.service.UserService;

@RestController
@RequestMapping("users")
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		super();
		this.userService = userService;
		
	}
	

	@GetMapping("validate/tag/exists/{label}")
	public boolean isHashtag(@PathVariable String label) {
		return userService.isHashtag(label);
	}
	
	@GetMapping("validate/username/exists/@{username}")
	public boolean isUsername(@PathVariable String username) {
		return userService.isUsername(username);
	}
	
	@GetMapping("validate/username/available/@{username}")
	public boolean availableUsername(@PathVariable String username) {
		return userService.availableUsername(username);
	}
	
	
	@GetMapping
	public UserDto[] getAllUsers() {
		return userService.getAllActiveUsers();
	}
	
	@PostMapping
	public UserDto addUser(@RequestBody UserInfo userInfo, HttpServletResponse response) {
		if(!availableUsername(userInfo.getUsername())) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		} return userService.addUser(userInfo);
		
	}
	
	@GetMapping("/@{username}")
	public UserDto getUsername(@PathVariable String username, HttpServletResponse response) {
		if(!isUsername(username)) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}else 
			return userService.getUsername(username);
			
	}
	
	@PatchMapping("/@{username}")
	public UserDto updateUser(@PathVariable String username, @RequestBody UserInfo userInfo, HttpServletResponse response ){
		if(!isUsername(userInfo.getUsername())) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return userService.updateUserProfile(userInfo.getCredentials(), userInfo.getProfile());
	}
	
	//delete user
		@DeleteMapping("/@{username}")
		public UserDto deleteUser(@PathVariable String username, @RequestBody Credentials credentials, HttpServletResponse response ){
			if(!isUsername(credentials.getUsername())) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}return userService.deleteUser(credentials);
		}

	@PostMapping("/@{username}/follow")
		public void followUser(@PathVariable String username, @RequestBody Credentials credentials, HttpServletResponse response) {
		if(!isUsername(credentials.getUsername())) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
			userService.follow(username, credentials);
		}
		
		@PostMapping("/@{username}/unfollow")
		public void unfollowUser(@PathVariable String username, @RequestBody Credentials credentials, HttpServletResponse response){
			if(!isUsername(credentials.getUsername())) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
				userService.unfollow(username, credentials);
		}
		
		@GetMapping("/@{username}/feed")
		public List<TweetDto> getFeed(@PathVariable String username, HttpServletResponse response){
			if(!isUsername(username)) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}
			return userService.getFeed(username);
		}
		
		@GetMapping("/@{username}/tweets")
		public List<TweetDto> getTweets(@RequestParam String username, HttpServletResponse response) {
			return userService.getTweets(username);
		}
		
		@GetMapping("/@{username}/mentions")
		public List<Tweet> getMentions(@PathVariable String username, HttpServletResponse response) {
			if(!isUsername(username)) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}
			return userService.getMentions(username);
		}
		
		@GetMapping("/@{username}/followers")
		public List<UserDto> getFollowers(@RequestParam String username, HttpServletResponse response){
			if(!isUsername(username)) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}
			return userService.getFollowers(username);
		}
		
		@GetMapping("/@{username}/following")
		public List<UserDto> getFollowing(@PathVariable String username, HttpServletResponse response){
			if(!isUsername(username)) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}
			return userService.getFollowing(username);
		}	
		
		
}
