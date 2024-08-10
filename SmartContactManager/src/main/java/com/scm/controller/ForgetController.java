package com.scm.controller;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.dao.UserRepository;
import com.scm.entities.Users;
import com.scm.helper.Message;
import com.scm.util.EmailServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgetController {
	static Random random = new Random(); // minimum value ie. seed value

	@Autowired
	private EmailServices emailServices;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/forget")
	public String emailForm() {
		return "forgetEmailForm";
	}

	@PostMapping("/sendOTP")
	public String sendOTP(@RequestParam("email") String email, HttpSession session) {
		System.out.println("---" + email + "---");

		Optional<Users> user = this.userRepository.getUsersByUserName(email);
		if (!user.isPresent()) {
			session.setAttribute("message", new Message("User does not exist. Please Register", "danger"));
			return "forgetEmailForm";
		}

		long otp = random.nextLong(111111,999999); // max. value excluding this
		String message = "<h4>SCM OTP: " + otp + "</h4>";
		boolean status = true;
		// this.emailServices.sendEmailWithHtml(email, "OTP Verification", message);
		System.out.println("otp is " + otp);
		if (status) {
			session.setAttribute("otp", otp);
			session.setAttribute("email",email);
			return "verifyOTP";
		} else {

			session.setAttribute("message",
					new Message("Something went wrong !! \n check your email address", "danger"));
			return "forgetEmailForm";
		}
	}

	@PostMapping("/verifyOTPEmail")
	public String verifyOTP(@RequestParam("otp") String otp, HttpSession session) {
		String sessionOTP = session.getAttribute("otp").toString();
		String email	  = session.getAttribute("email").toString();
		if (otp.equals(sessionOTP)) {
			
			return "changePassword";
		} else {
			session.setAttribute("message", new Message("you have enter wrong otp", "danger"));
			return "verifyOTP";
		}
	}
}
