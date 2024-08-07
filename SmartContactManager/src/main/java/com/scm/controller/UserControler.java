package com.scm.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.scm.dao.ContactRepository;
import com.scm.dao.UserRepository;
import com.scm.entities.Contact;
import com.scm.entities.Users;
import com.scm.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserControler {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

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
		m.addAttribute("title", "User Dashboard");
		return "general/userDashboard";
	}

	@GetMapping("/add-contact")
	public String openAddContactForm(Model m) {
		m.addAttribute("title", "Add Contact");
		m.addAttribute("contact", new Contact());
		return "general/addContact";
	}

	@PostMapping("/addContact")
	public String addContactAction(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {
		try {
			var userId = principal.getName();
			var obj = this.userRepository.getUsersByUserName(userId);

			/*
			 * to generate exception for testing if(true) throw new Exception();
			 */

			// proccessing and uploading file
			if (!file.isEmpty()) {
				contact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("/static/img/contact").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			} else {
				contact.setImage("default-contact.png");
			}

			Users user = obj.get();
			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepository.save(user);

			// send success msg
			session.setAttribute("message", new Message("Contact added successfully !! Add more...", "success"));

		} catch (Exception e) {
			e.printStackTrace();

			// send error msg
			session.setAttribute("message", new Message("Something went wrong !!", "danger"));
		}
		return "general/addContact";
	}

	@GetMapping("/show-contacts/{page}")
	public String showContact(@PathVariable("page") int page, Model m, Principal p) {
		m.addAttribute("title", "Show Contacts");
		var username = p.getName();
		Users user = this.userRepository.getUsersByUserName(username).get();

		Pageable pgNo_pgSize = PageRequest.of(page, 3);
		Optional<Page<Contact>> contactsByUser = this.contactRepository.findContactsByUser(user.getId(), pgNo_pgSize);
		Page<Contact> list = contactsByUser.get();
		m.addAttribute("listOfContacts", list);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", list.getTotalPages());

		return "general/showContact";
	}

	@RequestMapping("/{cid}/contactDetail")
	public String showContactDetail(@PathVariable("cid") int cid, Model m, Principal p) {
		var contact = this.contactRepository.findById(cid).get();
		var username = p.getName();
		Users user = this.userRepository.getUsersByUserName(username).get();

		if (user.getId() == contact.getUser().getId()) {
			m.addAttribute("contactDetail", contact);
			m.addAttribute("title", contact.getName());
		}

		return "general/showContactDetail";
	}

	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") int cid, Principal p, HttpSession session) {
		var username = p.getName();
		Users user = this.userRepository.getUsersByUserName(username).get();
		Contact contact = this.contactRepository.findById(cid).get();
		// if not deleted in any case then use
		// contact.setUser(null);

		if (user.getId() == contact.getUser().getId()) {
			// remove contact image from directory ie. /img/contact/fileName

			try {
				File filePath = new ClassPathResource("/static/img/contact").getFile();
				Path path = Paths.get(filePath.getAbsolutePath() + File.separator + contact.getImage());
				if (!contact.getImage().equals("default-contact.png"))
					Files.deleteIfExists(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.contactRepository.delete(contact);
			session.setAttribute("message", new Message("Contact deleted successfully...", "success"));
		}

		return "redirect:/user/show-contacts/0";
	}

	@PostMapping("/update/{cid}")
	public String updateContactForm(@PathVariable("cid") int cid, Principal p, HttpSession session, Model m) {
		Contact contact = this.contactRepository.findById(cid).get();
		m.addAttribute("updateContactDetail", contact);
		m.addAttribute("title", "Update Contact");

		return "general/updateContact";
	}

	@PostMapping("/updateContact")
	public String updateContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal p, HttpSession session) {
		try {
			Contact oldContactDetail = this.contactRepository.findById(contact.getCid()).get();
			if (!file.isEmpty()) {
				
				// delete old image
				File filePath = new ClassPathResource("/static/img/contact").getFile();
				Path delpath = Paths.get(filePath.getAbsolutePath() + File.separator + oldContactDetail.getImage());
				if (!oldContactDetail.getImage().equals("default-contact.png"))
					Files.deleteIfExists(delpath);
				
				// update new image
				
				File saveFile = new ClassPathResource("/static/img/contact").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
			} else {
				contact.setImage(oldContactDetail.getImage());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		Users users = this.userRepository.getUsersByUserName(p.getName()).get();
		contact.setUser(users);
		this.contactRepository.save(contact);
		session.setAttribute("message", new Message("Contact updated successfully...", "success"));
		return "redirect:/user/" + contact.getCid() + "/contactDetail";
	}

	@GetMapping("/user-profile")
	public String userProfile(Model m) {
		m.addAttribute("title", "User Profile");
		return "general/userProfile";
	}
	
	@GetMapping("/user-profile-update")
	public String userProfileUpdate(Model m) {
		m.addAttribute("title", "User Profile Update");
		return "general/userProfileUpdate";
	}
	
	@PostMapping("/userProfileUpdate")
	public String userProfileUpdate(@ModelAttribute Users users, @RequestParam("profileImage") MultipartFile file,
			Principal p, HttpSession session) {
		try {
			Users oldUserDetail = this.userRepository.getUsersByUserName(p.getName()).get();
			System.err.println(oldUserDetail);
			users.setRole(oldUserDetail.getRole());
			users.setEnabled(true);
			System.out.println("users.getPassword()---"+users.getPassword()+"---"+users.getPassword().length()+"{{{{"+users.getPassword().isBlank());
			if(!users.getPassword().isBlank() && users.getPassword().length() > 2) {
				System.out.println("mark1");
			users.setPassword(passwordEncoder.encode(users.getPassword()));
			}else {
				System.out.println("mark2 " +oldUserDetail.getPassword());
				users.setPassword(oldUserDetail.getPassword());
			}
			if (!file.isEmpty()) {				
				// delete old image
				File filePath = new ClassPathResource("/static/img").getFile();
				Path delpath = Paths.get(filePath.getAbsolutePath() + File.separator + oldUserDetail.getImageUrl());
				if (!oldUserDetail.getImageUrl().equals("default.png"))
					Files.deleteIfExists(delpath);
				
				// update new image
				
				File saveFile = new ClassPathResource("/static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				users.setImageUrl(file.getOriginalFilename());
			} else {
				users.setImageUrl(oldUserDetail.getImageUrl());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		this.userRepository.save(users);
		
		session.setAttribute("message", new Message("profile updated successfully...", "success"));
		return "redirect:/user/user-profile";
	}
}
