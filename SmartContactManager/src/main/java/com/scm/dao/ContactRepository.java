package com.scm.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scm.entities.Contact;
import com.scm.entities.Users;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	// this type of query is called JPQL (Java Persistence Query Language) query
	@Query("from Contact as c where c.user.id = :customVar")
	public Optional<Page<Contact>> findContactsByUser(@Param("customVar") int userid, Pageable pageable);
	
	// to create pagination we use 'Page' interface. for more information goto its interface
	// Pageable store the information of pagination ie. data per page and current page
	
	// search by keyword which found in name having under users
	// in findByNameContainingAnd'User' 
	// user reference name should be same as instance name present in Contact entity
	public List<Contact> findByNameContainingAndUser(String keywords,Users user);
	
}
