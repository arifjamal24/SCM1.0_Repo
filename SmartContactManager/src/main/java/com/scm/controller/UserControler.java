package com.scm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserControler {

	@RequestMapping("/index")
	public String dashboard() {
		
		return "general/userDashboard";
	}
}
