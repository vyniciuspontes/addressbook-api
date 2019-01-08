package com.company.addressbook.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the contact database table.
 * 
 */
@Entity
@NamedQuery(name = "Contact.findAll", query = "SELECT c FROM Contact c")
@NamedQuery(name = "Contact.findByUsername", query = "SELECT c FROM Contact c JOIN c.applicationUser au where au.username=:username")
public class Contact implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Temporal(TemporalType.DATE)
	@Column(name = "born_date")
	private Date bornDate;

	@NotNull
	private String cpf;

	@Column(name = "first_name")
	@NotNull
	private String firstName;

	@Column(name = "last_name")
	@NotNull
	private String lastName;

	// bi-directional many-to-one association to Address
	@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval=true)
	private List<Address> addresses = new ArrayList<>();

	// bi-directional many-to-one association to User
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "application_user_id")
	private ApplicationUser applicationUser;

	public Contact() {
	}

	public Contact(Date bornDate, @NotNull String cpf, @NotNull String firstName, @NotNull String lastName,
			List<Address> addresses, @NotNull ApplicationUser applicationUser) {
		this.bornDate = bornDate;
		this.cpf = cpf;
		this.firstName = firstName;
		this.lastName = lastName;
		this.addresses = addresses;
		this.applicationUser = applicationUser;
	}

	public UUID getId() {
		return this.id;
	}

	public Date getBornDate() {
		return this.bornDate;
	}

	public void setBornDate(Date bornDate) {
		this.bornDate = bornDate;
	}

	public String getCpf() {
		return this.cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Address> getAddresses() {
		return this.addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public Address addAddress(Address address) {
		getAddresses().add(address);
		address.setContact(this);

		return address;
	}

	public Address removeAddress(Address address) {
		getAddresses().remove(address);
		address.setContact(null);

		return address;
	}

	public ApplicationUser getApplicationUser() {
		return this.applicationUser;
	}

	public void setApplicationUser(ApplicationUser user) {
		this.applicationUser = user;
	}

}