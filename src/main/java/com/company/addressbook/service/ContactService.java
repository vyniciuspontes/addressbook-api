package com.company.addressbook.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.addressbook.model.Address;
import com.company.addressbook.model.ApplicationUser;
import com.company.addressbook.model.Contact;
import com.company.addressbook.repository.ApplicationUserRepository;
import com.company.addressbook.repository.ContactRepository;

@Service
public class ContactService {

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private ApplicationUserRepository applicationUserRepository;

	public Optional<Contact> find(UUID id) {
		return this.contactRepository.findById(id);
	}

	public List<Contact> findByUsername(String username) {

		List<Contact> contacts = this.contactRepository.findByUsername(username);

		return contacts;
	}

	@Transactional
	public Contact save(Contact contact) {
		return this.contactRepository.save(contact);
	}

	@Transactional
	public Contact saveApplicationUserContact(Contact newContact, String username) {

		ApplicationUser user = applicationUserRepository.findFirstByUsername(username);

		newContact.setApplicationUser(user);

		for (Address address : newContact.getAddresses()) {
			address.setContact(newContact);
		}

		newContact = this.contactRepository.save(newContact);

		Optional<Contact> recentlyCreatedContact = this.contactRepository.findById(newContact.getId());

		return recentlyCreatedContact.get();
	}

	@Transactional
	public Contact updateApplicationUserContact(Contact contact, String username) {

		if (contact.getId() == null)
			throw new IllegalArgumentException("Can't update entity without id");

		ApplicationUser user = applicationUserRepository.findFirstByUsername(username);

		Optional<Contact> toBeUpdated = this.contactRepository.findById(contact.getId());

		if (user.getId() != toBeUpdated.get().getApplicationUser().getId()) {
			throw new AccessDeniedException("Cannot change other's users data");
		}

		contact.setApplicationUser(user);

		for (Address address : contact.getAddresses()) {
			address.setContact(contact);
		}

		contact = this.contactRepository.save(contact);

		Optional<Contact> recentlyUpdatedContact = this.contactRepository.findById(contact.getId());

		return recentlyUpdatedContact.get();

	}

	@Transactional
	public void delete(Contact contact, String username) {

		ApplicationUser user = applicationUserRepository.findFirstByUsername(username);

		Contact toBeDeleted = this.contactRepository.getOne(contact.getId());

		if (user.getId() != toBeDeleted.getApplicationUser().getId()) {
			throw new AccessDeniedException("Cannot change other's users data");
		}

		this.contactRepository.delete(toBeDeleted);
	}

}
