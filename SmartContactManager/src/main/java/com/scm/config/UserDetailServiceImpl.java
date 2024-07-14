package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.scm.dao.UserRepository;
import com.scm.entities.User;

public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// fetching user from database
		User user = userRepository.getUserByUserName(username);
		if(user == null) {
			throw new UsernameNotFoundException("Could not found user!!"); 
		}
		
		CustomUserDetails cud = new CustomUserDetails(user);
		return cud;
	}

}
