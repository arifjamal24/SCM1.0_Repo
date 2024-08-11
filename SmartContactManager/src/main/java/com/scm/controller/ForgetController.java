package com.scm.controller;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.dao.UserRepository;
import com.scm.entities.Users;
import com.scm.helper.Message;
import com.scm.util.EmailServices;
import com.scm.util.RandomOTP;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgetController {

	@Autowired
	private EmailServices emailServices;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCrypt;
	@Autowired
	private RandomOTP random;
	

	@RequestMapping("/forget")
	public String emailForm() {
		return "forgetEmailForm";
	}

	@PostMapping("/sendOTP")
	public String sendOTP(@RequestParam("email") String email, HttpSession session) {
		Optional<Users> user = this.userRepository.getUsersByUserName(email);
		if (!user.isPresent()) {
			session.setAttribute("message", new Message("User does not exist. Please Register", "danger"));
			return "forgetEmailForm";
		}
		int otp = this.random.otp();	
		String message = "<h4>SCM OTP: " + otp + "</h4>";
		boolean status = true;
		// this.emailServices.sendEmailWithHtml(email, "OTP Verification", message);
		System.out.println("otp is " + otp);
		if (status) {
			session.setAttribute("otp", otp);
			session.setAttribute("email", email);
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
		if (otp.equals(sessionOTP)) {
			return "changePassword";
		} else {
			session.setAttribute("message", new Message("you have enter wrong otp", "danger"));
			return "verifyOTP";
		}
	}

	@PostMapping("/changePasswordNew")
	public String changePasswordNew(@RequestParam("password") String password, HttpSession session) {
		String email = session.getAttribute("email").toString();
		Optional<Users> usersByUserName = this.userRepository.getUsersByUserName(email);
		if (usersByUserName.isPresent()) {
			Users users = usersByUserName.get();
			users.setPassword(this.bCrypt.encode(password));

			this.userRepository.save(users);
		}
		return "redirect:signin?change=you have been change password successfully. Please login";
	}
}
