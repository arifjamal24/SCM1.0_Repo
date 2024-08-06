package com.scm.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.dao.UserRepository;
import com.scm.entities.Contact;
import com.scm.entities.Users;

@Controller
@RequestMapping("/user")
public class UserControler {

	@Autowired
	private UserRepository userRepository;

	@ModelAttribute
	public void addCommonData(Model m, Principal p) {
		// using Principal we get username in out case we get emailid as a username
		String userName = p.getName();
		Optional<Users> user = userRepository.getUsersByUserName(userName);
		var u = user.get();
		m.addAttribute("userDetail", u); 
	}

	@RequestMapping("/index")
	public String dashboard(Model m) {
		m.addAttribute("title","User Dashboard");
		return "general/userDashboard";
	}

	@GetMapping("/add-contact")
	public String openAddContactForm(Model m) {
		m.addAttribute("title","Add Contact");
		m.addAttribute("contact", new Contact());
		return "general/addContact";
	}

}
