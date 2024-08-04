package com.scm.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.scm.dao.UserRepository;
import com.scm.entities.Users;

public class UserDetailServiceImpl implements UserDetailsService {
// second step for Role based autherization
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// fetching user from database
		Optional<Users> user = userRepository.getUsersByUserName(username);
		if(user.isPresent()) {
			var userObj = user.get();
			return User.builder()
					 .username(userObj.getEmail())
					 .password(userObj.getPassword())
					 .roles(getRoles(userObj))
					 .build();
		}
		else {
			throw new UsernameNotFoundException("Could not found user!!"); 
		}
	//	CustomUserDetails cud = new CustomUserDetails(user);
//		return cud;
	}

	private String[] getRoles(Users user) {
		if(user.getRole() == null) {
			return new String[] {"USER"};
		}
		
		return user.getRole().split(","); // if ADMIN,USER
	}
}
