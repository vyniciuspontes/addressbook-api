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
	public Contact updateApplicationUserContact(Contact newContact, UUID id, String username) {

		Optional<Contact> currentContactOptional = this.contactRepository.findById(id);
		
		if(!currentContactOptional.isPresent())
			throw new IllegalArgumentException("Resource does not exist");
		
		Contact currentContact = currentContactOptional.get();
		
		ApplicationUser user = applicationUserRepository.findFirstByUsername(username);
	
		if (user.getId() != currentContact.getApplicationUser().getId()) {
			throw new AccessDeniedException("Cannot change other's users data");
		}		
		
		currentContact.setFirstName(newContact.getFirstName());
		currentContact.setLastName(newContact.getLastName());
		currentContact.setBornDate(newContact.getBornDate());
		currentContact.setEmail(newContact.getEmail());
		currentContact.setPhone(newContact.getPhone());
		currentContact.setCpf(newContact.getCpf());
		
		newContact.setApplicationUser(user);
		
		for (Address address : newContact.getAddresses()) {
			address.setContact(currentContact);
		}

		currentContact.setAddresses(newContact.getAddresses());
		
		currentContact = this.contactRepository.save(currentContact);

		Contact recentlyUpdatedContact = this.contactRepository.findById(currentContact.getId()).get();

		return recentlyUpdatedContact;

	}

	@Transactional
	public void delete(UUID id, String username) {

		ApplicationUser user = applicationUserRepository.findFirstByUsername(username);

		Contact toBeDeleted = this.contactRepository.getOne(id);

		if (user.getId() != toBeDeleted.getApplicationUser().getId()) {
			throw new AccessDeniedException("Cannot change other's users data");
		}

		this.contactRepository.delete(toBeDeleted);
	}

}
