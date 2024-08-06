package com.scm.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scm.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	// this type of query is called JPQL (Java Persistence Query Language) query
	@Query("from Contact as c where c.user.id = :customVar")
	public Optional<List<Contact>> findContactsByUser(@Param("customVar") int userid);
}
