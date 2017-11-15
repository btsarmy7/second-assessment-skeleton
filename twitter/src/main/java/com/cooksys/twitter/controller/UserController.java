package com.cooksys.twitter.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter.dto.UserDto;
import com.cooksys.twitter.dto.TweetDto;
import com.cooksys.twitter.embedded.UserData;
import com.cooksys.twitter.mapper.UserMapper;
import com.cooksys.twitter.embedded.Credentials;
import com.cooksys.twitter.repository.UserRepository;
import com.cooksys.twitter.service.UserService;

@RestController
@RequestMapping("users")
public class UserController {

	private UserService userService;


	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@GetMapping
	public UserDto[] getUsers(){
		// convert to array of active users
		UserDto[] allUsers = userService.findUsers().toArray(new UserDto[userService.findUsers().size()]);
		return allUsers;
		//return userService.findUsers();
	}

	@PostMapping
	public UserDto createUser(@RequestBody UserData userData, HttpServletResponse response){
		if (!validUserData(userData)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		UserDto userDto = userService.findByUserName(userData.getUserName());
		if (userDto != null && userService.userNameExists(userDto.getUserName())){
			if (userDto.isDeleted()){
				return userService.activateUser(userDto);
			}
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return null;
		}
		return userService.create(userData);
	}

	@GetMapping("/@{userName}")
	public UserDto findByUserName(@RequestParam String userName, HttpServletResponse response){

		if (!userService.userNameExists(userName) || userService.userIsDeleted(userName)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return userService.findByUserName(userName);
	}

	@PatchMapping("/@{userName}")
	public UserDto updateUserProfile(@RequestBody UserData userData, HttpServletResponse response){
		if (!validUserData(userData)){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

		if (!validUser(userData)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return userService.updateUser(userData);
	}

	@DeleteMapping("/@{userName}")
	public UserDto deleteUser(@RequestBody Credentials credentials, HttpServletResponse response){
		if (!validUser(credentials)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return userService.deleteUser(credentials.getUserLogin());
	}
	//delete user
		/*@DeleteMapping("/@{username}")
		  public UserDto deleteUser(@PathVariable String username, @RequestBody Credentials credentials, HttpServletResponse response ){
			if(!isUsername(credentials.getUsername())) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return null;
				}return userService.deleteUser(credentials);
			}*/

	@PostMapping("/@{userName}/follow")
	public void followUser(@RequestParam String userName, @RequestBody Credentials followerCred, HttpServletResponse response){
		if (!validUser(followerCred) || !validUser(userName)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		userService.follow(followerCred.getUserLogin(), userName);
	}
	/*@PostMapping("/@{username}/follow")
	public void followUser(@PathVariable String username, @RequestBody Credentials credentials, HttpServletResponse response) {
	if(!isUsername(credentials.getUsername())) {
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	}
		userService.follow(username, credentials);
	}*/

	@PostMapping("@{username}/unfollow")
	public void unFollowUser(@RequestParam String username, @RequestBody Credentials followerCred, HttpServletResponse response){
		if (!validUser(followerCred) || !validUser(username)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		userService.unFollow(followerCred.getUserLogin(), username);
	}

	@GetMapping("/@{username}/feed")
	public List<TweetDto> getFeed(@RequestParam String username, HttpServletResponse response){
		if (!userService.userNameExists(username)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return userService.getFeed(username);
	}

	@GetMapping("/@{username}/tweets")
	public List<TweetDto> getTweets(@RequestParam String username, HttpServletResponse response){
		if (!userService.userNameExists(username)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return userService.getTweets(username);
	}

	@GetMapping("/@{username}/mentions")
	public List<TweetDto> getMentions(@RequestParam String username, HttpServletResponse response){
		if (!userService.userNameExists(username)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return userService.getMentions(username);
	}

	@GetMapping("/@{userName}/followers")
	public Set<UserDto> getFollowers(@RequestParam String userName, HttpServletResponse response){
		if (!userService.userNameExists(userName)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return userService.getFollowers(userName);
	}

	@GetMapping("/@{userName}/following")
	public Set<UserDto> getFollowing(@RequestParam String userName, HttpServletResponse response){
		if (!userService.userNameExists(userName)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return userService.getFollowing(userName);
	}


	private boolean validUser(UserData userData){
		String userName = userData.getUserName();
		if (!userService.userNameExists(userName) || userService.userIsDeleted(userName))
			return false;
		return userService.validatePassword(userData);
	}

	public boolean validUser(Credentials credentials){
		String userName = credentials.getUserLogin();
		if (!userService.userNameExists(userName) || userService.userIsDeleted(userName))
			return false;
		return userService.validatePassword(credentials);
	}

	public boolean validUser(String userName){
		if (!userService.userNameExists(userName) || userService.userIsDeleted(userName))
			return false;
		return true;
	}

	private boolean validUserData(UserData userData){
		if (userData.getProfile().getEmail() == null || userData.getProfile().getEmail().equals(""))
			return false;
		if (!validCredentials(userData.getCredentials()))
			return false;
		return true;
	}

	private boolean validCredentials(Credentials credentials){
		if (credentials.getUserLogin() == null || credentials.getUserLogin().equals(""))
			return false;
		if (credentials.getPassword() == null || credentials.getPassword().equals(""))
			return false;
		return true;
	}


}
