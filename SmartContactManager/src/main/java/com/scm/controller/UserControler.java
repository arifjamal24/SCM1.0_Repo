package com.scm.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	
	@PostMapping("/addContact")
	public String addContactAction(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file, Principal principal) {
		try {
		var userId = principal.getName();
		var obj = this.userRepository.getUsersByUserName(userId);
		
		// proccessing and uploading file
		if(!file.isEmpty()) {
			contact.setImage(file.getOriginalFilename());
			
			File saveFile = new ClassPathResource("/static/img/contact").getFile();
			System.out.println(saveFile);
			System.out.println(saveFile.getAbsolutePath());
		var path = Paths.get(saveFile.getAbsolutePath()+File.pathSeparator+file.getOriginalFilename());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		System.out.println("Images is uploaded");
		}
		else {
			System.out.println("file is empty");
		}
		
		Users user = obj.get();
		contact.setUser(user);
		user.getContacts().add(contact);
		this.userRepository.save(user);
		System.out.println(contact);
		System.out.println("added to databnase");
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return "general/addContact";
	}

}
