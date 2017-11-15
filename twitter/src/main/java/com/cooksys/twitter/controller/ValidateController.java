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

	@PostMapping("/setup")
	public String setupData(HttpServletResponse response){
		String completeMessage = "";
		hashtagService.create("whereIsBangtan");
		hashtagService.create("RM");
		hashtagService.create("BTSAMAs");
		completeMessage += "Tag setup completed\n";
		
		Credentials jjk = new Credentials("kookie", "overwatch");
		Credentials kth = new Credentials("vante", "photography");
		Credentials pjm = new Credentials("chimchim", "calicoCat");
		Credentials jhs = new Credentials("jayhope", "hobi");
		
		
		Profile p1 = new Profile();
		Profile p2 = new Profile();
		Profile p3 = new Profile();
		Profile p4 = new Profile();
	

		p1.setEmail("kookie@gmail.com");
		p1.setFirstName("Jungkook");
		p1.setLastName("Jeon");
		p1.setPhone("585858585");
		p2.setEmail("chimchim@gmail.com");
		p2.setFirstName("Jimin");
		p2.setLastName("Park");
		p2.setPhone("1310131013");
		p3.setEmail("vante@gmail.com");
		p3.setFirstName("Taehyung");
		p3.setLastName("Kim");
		p3.setPhone("123437264");
		p4.setEmail("jayHope@gmail.com");
		p4.setFirstName("Hoseok");
		p4.setLastName("Jung");
		p4.setPhone("1658745314");
	


		userService.create(true, jjk, p1);
		userService.create(pjm, p2);
		userService.create(kth, p3);
		userService.create(true, jhs, p4);
		
		completeMessage += "User setup completed\n";

		return completeMessage;
	}

}
