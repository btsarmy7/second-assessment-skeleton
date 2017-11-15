package com.cooksys.twitter.controller;


import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter.dto.UserDto;
import com.cooksys.twitter.dto.HashtagDto;
import com.cooksys.twitter.dto.TweetDto;
import com.cooksys.twitter.embedded.Context;
import com.cooksys.twitter.embedded.Credentials;
import com.cooksys.twitter.embedded.TweetData;
import com.cooksys.twitter.exceptions.InvalidIdException;
import com.cooksys.twitter.service.TweetService;

@RestController
@RequestMapping("tweets")
public class TweetController {

	private TweetService tweetService;
	private UserController userController;

	

	public TweetController(TweetService tweetService, UserController userController) {
		this.tweetService = tweetService;
		this.userController = userController;
	}

	@GetMapping
	public List<TweetDto> getTweets(){
		return tweetService.getTweets();
	}
	/*@GetMapping
	public List<TweetDto> getAllTweets(){
		return tweetService.getAll();
	}*/

	@PostMapping
	public TweetDto postTweet(@RequestBody TweetData tweetData, HttpServletResponse response) throws IOException{
		try {
				return tweetService.createSimpleTweet(tweetData);
			} catch( InvalidIdException e ) {
				response.sendError(e.BAD_REQ, "Invalid Tweet Data");
				return null;
			}
		}
		/*if (tweetData.getContent() == null || tweetData.getContent().equals("")){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

		return tweetService.createSimpleTweet(tweetData);*/


	@GetMapping("/{id}")
	public TweetDto getTweetById(@RequestParam Integer id, HttpServletResponse response) throws IOException{
		TweetDto tweetDto = tweetService.getTweetById(id);
		if (tweetDto == null){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not find tweet id: " + id);
			return null;
		}
		return tweetDto;
		/*TweetDto tweetDto = tweetService.getTweetById(id);
		if (tweetDto == null){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		return tweetDto;*/
	}

	@DeleteMapping("/{id}")
	public TweetDto deleteTweetById(@RequestParam Integer id, HttpServletResponse response) throws IOException{
		TweetDto tweetDto = tweetService.deleteTweetById(id);
		if (tweetDto == null){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not find tweet id: " + id + " , to delete");
			return null;
		}
		return tweetDto;
		/*TweetDto tweetDto = tweetService.deleteTweetById(id);
		if (tweetDto == null){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		return tweetDto;*/
	}
	//delete tweet
		/*@DeleteMapping("/{id}")
		public TweetDto deleteTweet(@PathVariable Integer id, @RequestBody Credentials credentials, HttpServletResponse response){
			if(tweetRepository.findByIdAndDeletedFalse(id) == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}
			return tweetService.deleteUserTweet(id, credentials);		
		}*/


	@PostMapping("/{id}/like")
	public void like(@RequestParam Integer id, @RequestBody Credentials credentials, HttpServletResponse response) throws IOException{
		if (!userController.validUser(credentials) || !tweetService.tweetExists(id)){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Either user is invalid or tweet does not exist");
			//response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		tweetService.like(id, credentials.getUserLogin());
	}

	@PostMapping("/{id}/reply")
	public TweetDto replyTo(@RequestParam Integer id, @RequestBody TweetData tweetData, HttpServletResponse response) throws IOException{
		if (!userController.validUser(tweetData.getCredentials()) || !tweetService.tweetExists(id)){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Either user is invalid or tweet does not exist");
			//response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		if (tweetData.getContent() == null || tweetData.getContent().equals("")){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tweet cannot be null");
			//response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		return tweetService.replyTo(id, tweetData);
	}
	

	/*@PostMapping("/{id}/reply")
	public TweetDto replyTweet(@PathVariable Integer id, @RequestBody TweetInfo tweetInfo, HttpServletResponse response){
		if(tweetRepository.findByIdAndDeletedFalse(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return tweetService.reply(id, tweetInfo.getContent(), tweetInfo.getCredentials());
	}*/

	@PostMapping("/{id}/repost")
	public TweetDto repost(@RequestParam Integer id, @RequestBody Credentials credentials, HttpServletResponse response) throws IOException{
		if (!userController.validUser(credentials) || !tweetService.tweetExists(id)){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Either user is invalid or tweet does not exist");
			//response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		return tweetService.repost(id, credentials.getUserLogin());
	}

	@GetMapping("{id}/tags")
	public Set<HashtagDto> getTags(@RequestParam Integer id, HttpServletResponse response) throws IOException{
		if (!tweetService.tweetExists(id)){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not find tweet id " + id);
			//response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		return tweetService.getTagsByTweet(id);
	}

	@GetMapping("/{id}/likes")
	public Set<UserDto> getLikes(@RequestParam Integer id, HttpServletResponse response) throws IOException{
		if (!tweetService.tweetExists(id)){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not find tweet id " + id);
			//response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		return tweetService.getLikesByTweet(id);
	}

	@GetMapping("/{id}/context")
	public Context getContext(@RequestParam Integer id, HttpServletResponse response) throws IOException{
		if (!tweetService.tweetExists(id)){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not find tweet id " + id);
			//response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		return tweetService.getContext(id);
	}

	@GetMapping("/{id}/replies")
	public List<TweetDto> getReplies(@RequestParam Integer id, HttpServletResponse response) throws IOException{
		if (!tweetService.tweetExists(id)){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not find tweet id " + id);
			//response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

		return tweetService.getRepliesByTweet(id);
	}

	@GetMapping("/{id}/reposts")
	public List<TweetDto> getReposts(@RequestParam Integer id, HttpServletResponse response) throws IOException{
		if (!tweetService.tweetExists(id)){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not find tweet id " + id);
			//response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

		return tweetService.getRepostsByTweet(id);
	}

	@GetMapping("/{id}/mentions")
	public List<UserDto> getMentions(@RequestParam Integer id, HttpServletResponse response) throws IOException{
		if (!tweetService.tweetExists(id)){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not find tweet id " + id);
			//response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		return tweetService.getMentionsByTweet(id);
	}
	
	/*@GetMapping("/{id}/likes")
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
	}*/
}
