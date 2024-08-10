package com.scm.controller;

import java.net.Authenticator.RequestorType;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgetController {

 static	Random random = new Random(100000); // minimum value ie. seed value
	
	
	@RequestMapping("/forgot")
	public String emailForm() {
		
		return "forgetEmailForm";
	}
	
	@RequestMapping(value="/sendOTP",method=RequestMethod.POST)
	public String sendOTP(@RequestParam("email") String email) {
		System.out.println("---"+email+"---");
		

		int otp = random.nextInt(999999); // max. value  excluding this
		System.out.println("otp is "+otp);
		
		return "verifyOTP";
	}
}
