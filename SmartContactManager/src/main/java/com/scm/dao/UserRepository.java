package com.scm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scm.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
