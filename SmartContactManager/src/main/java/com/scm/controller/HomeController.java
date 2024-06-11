package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.dao.UserRepository;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model m) {
		m.addAttribute("title","Home - Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model m) {
		m.addAttribute("title","About - Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("title","Register - Smart Contact Manager");
		return "signup";
	}
	
}
