package com.company.addressbook.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.company.addressbook.model.ApplicationUser;
import com.company.addressbook.model.Contact;
import com.company.addressbook.service.ApplicationUserService;
import com.company.addressbook.service.ContactService;

@RestController
@RequestMapping("/api/users")
public class ApplicationUserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private ApplicationUserService applicationUserService;

	@Autowired
	private ContactService contactService;

	@PostMapping("/sign-up")
	public void signUp(@RequestBody ApplicationUser user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		applicationUserService.save(user);
	}

	@PostMapping("/current/contacts")
	@ResponseBody
	public ResponseEntity<Contact> createUserContact(@RequestBody @Valid Contact contact,
			Authentication authentication) {
		Contact createdContact = contactService.saveApplicationUserContact(contact, authentication.getName());
		return new ResponseEntity<Contact>(createdContact, HttpStatus.CREATED);
	}

	@PutMapping("/current/contacts")
	@ResponseBody
	public ResponseEntity<Contact> updateUserContact(@RequestBody @Valid Contact contact,
			Authentication authentication) {
		Contact createdContact = contactService.updateApplicationUserContact(contact, authentication.getName());
		return new ResponseEntity<Contact>(createdContact, HttpStatus.OK);
	}

	@DeleteMapping("/current/contacts/")
	@ResponseBody
	public ResponseEntity<Object> deleteUserContact(@RequestBody @Valid Contact contact,
			Authentication authentication) {
		this.contactService.delete(contact, authentication.getName());
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/current/contacts")
	@ResponseBody
	public List<Contact> getUserContacts(Authentication authentication) {

		return contactService.findByUsername(authentication.getName());
	}

	@GetMapping("/current")
	public UserDetails getCurrent(Authentication authentication) {
		return applicationUserService.loadUserByUsername(authentication.getName());
	}
}
