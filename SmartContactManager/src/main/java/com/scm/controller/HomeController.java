package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.dao.UserRepository;
import com.scm.entities.User;
import com.scm.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

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
		m.addAttribute("user",new User());
		return "signup";
	}
	
	@PostMapping("/doRegister")
	public String registerUser(@Valid @ModelAttribute User user,BindingResult result,
			@RequestParam(value="agreement",defaultValue = "false") boolean agreement,Model model,
			HttpSession session) {
		try {
			if(!agreement) {
				System.out.println("You have not agreed terms and conditions");
				throw new Exception("\nYou have not agreed terms and conditions");
			}
			
			if(result.hasErrors()){
				System.out.println("result :"+result.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			
			System.out.println("agreement: "+agreement);
			System.out.println("model: "+user);
			
			 this.userRepository.save(user);
			
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered !!","alert-success"));
			return "signup";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong."+e.getMessage(),"alert-danger"));
			return "signup";
		}
		
	}
	
}
