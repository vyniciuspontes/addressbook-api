package com.company.addressbook.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.addressbook.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {
	
	public List<Contact> findByUsername(String username);
}
