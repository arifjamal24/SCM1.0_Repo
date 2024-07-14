package com.scm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scm.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	@Query("select u from User u where u.email= : emailvar")
	public User getUserByUserName(@Param("emailvar") String email);
}
