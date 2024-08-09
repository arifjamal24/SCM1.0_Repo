package com.scm.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.dao.ContactRepository;
import com.scm.dao.UserRepository;
import com.scm.entities.Contact;
import com.scm.entities.Users;

// to return json data we use @RestController
@RestController
@RequestMapping("/search")
public class SearchController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@GetMapping("/name/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String q, Principal p){
		Users user = this.userRepository.getUsersByUserName(p.getName()).get();
		List<Contact> contact = this.contactRepository.findByNameContainingAndUser(q, user);
		return ResponseEntity.ok(contact);
	}
}
