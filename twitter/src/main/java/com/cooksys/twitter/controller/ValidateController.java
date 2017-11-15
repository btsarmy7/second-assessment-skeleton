package com.cooksys.twitter.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.twitter.embedded.Credentials;
import com.cooksys.twitter.embedded.Profile;
import com.cooksys.twitter.service.UserService;
import com.cooksys.twitter.service.HashtagService;


@RestController
@RequestMapping("validate")
public class ValidateController {

	private HashtagService hashtagService;
	private UserService userService;

	public ValidateController(HashtagService hashtagService, UserService userService) {
		this.hashtagService = hashtagService;
		this.userService = userService;

	}
	
	@GetMapping("/tag/exists/{label}")
	public boolean tagExists(@RequestParam String label, HttpServletResponse response){
		if (hashtagService.tagExists(label)){
			response.setStatus(HttpServletResponse.SC_FOUND);
			return true;
		}
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return false;
	}

	@GetMapping("/username/exists/@{userName}")
	public boolean userNameExists(@RequestParam String userName, HttpServletResponse response){
		
		if (userService.userNameExists(userName)){
			response.setStatus(HttpServletResponse.SC_FOUND);
			return true;
		}
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return false;
	}

	@GetMapping("/username/available/@{userName}")
	public boolean userNameAvailable(@RequestParam String userName, HttpServletResponse response){
		if (!userService.userNameExists(userName)){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return true;
		}
		response.setStatus(HttpServletResponse.SC_FOUND);
		return false;
	}

	@PostMapping("/test")
	public void testData(HttpServletResponse response){
		
		hashtagService.create("whereIsBangtan");
		hashtagService.create("RM");
		hashtagService.create("BTSAMAs");
		hashtagService.create("DNA");
		
		Credentials jjk = new Credentials("kookie", "overwatch");
		Credentials kth = new Credentials("vante", "photography");
		Credentials pjm = new Credentials("chimchim", "calicoCat");
		Credentials jhs = new Credentials("jayhope", "hobi");
		Credentials myg = new Credentials("suga", "piano");
		Credentials ksj = new Credentials("worldwideHandsome", "my face");
		Credentials knj = new Credentials("RM", "notRapMonsterAnymore");
		
		Profile maknae = new Profile();
		Profile fourD = new Profile();
		Profile mochi = new Profile();
		Profile hope = new Profile();
		Profile genius = new Profile();
		Profile visual = new Profile();
		Profile leader = new Profile();

		maknae.setEmail("goldenKookie@gmail.com");
		maknae.setFirstName("Jungkook");
		maknae.setLastName("Jeon");
		maknae.setPhone("585858585");
		mochi.setEmail("chimchim@gmail.com");
		mochi.setFirstName("Jimin");
		mochi.setLastName("Park");
		mochi.setPhone("1310131013");
		fourD.setEmail("vante@gmail.com");
		fourD.setFirstName("Taehyung");
		fourD.setLastName("Kim");
		fourD.setPhone("123437264");
		hope.setEmail("jayHope@gmail.com");
		hope.setFirstName("Hoseok");
		hope.setLastName("Jung");
		hope.setPhone("1658745314");
		genius.setEmail("agustD@gmail.com");
		genius.setFirstName("Yoongi");
		genius.setLastName("Min");
		genius.setPhone("321474623");
		visual.setEmail("mrWWH@gmail.com");
		visual.setFirstName("Seokjin");
		visual.setLastName("Kim");
		visual.setPhone("3716429842");
		leader.setEmail("iLoveRyan@gmail.com");
		leader.setFirstName("Namjoon");
		leader.setLastName("Kim");
		leader.setPhone("127364724");
		
		userService.create(true, jjk, maknae);
		userService.create(pjm, mochi);
		userService.create(kth, fourD);
		userService.create(true, jhs, hope);
		userService.create(myg, genius);
		userService.create(ksj, visual);
		userService.create(true, knj, leader);

	}

}
