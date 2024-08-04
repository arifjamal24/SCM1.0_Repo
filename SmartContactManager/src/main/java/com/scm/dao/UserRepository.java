package com.scm.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scm.entities.Users;

public interface UserRepository extends JpaRepository<Users, Integer>{

	@Query("select u from Users u where u.email= :emailvar")
	public Optional<Users> getUsersByUserName(@Param("emailvar") String email);
}
