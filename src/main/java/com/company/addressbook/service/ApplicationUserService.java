package com.company.addressbook.service;

import static java.util.Collections.emptyList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.addressbook.model.ApplicationUser;
import com.company.addressbook.repository.ApplicationUserRepository;

@Service
public class ApplicationUserService implements UserDetailsService {

	private ApplicationUserRepository applicationUserRepository;

	public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
		this.applicationUserRepository = applicationUserRepository;
	}

	@Transactional
	public ApplicationUser save(ApplicationUser applicationUser) {
		return this.applicationUserRepository.saveAndFlush(applicationUser);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ApplicationUser applicationUser = applicationUserRepository.findFirstByUsername(username);
		if (applicationUser == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
	}

}