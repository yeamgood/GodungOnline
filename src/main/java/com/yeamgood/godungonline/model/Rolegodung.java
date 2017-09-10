package com.yeamgood.godungonline.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yeamgood.godungonline.model.template.ModelTemplate;

@Entity
@Table(name = "rolegodung")
public class Rolegodung extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "rolegodung_Id")
	private Long rolegodungId;
	
	@Transient
	private String rolegodungIdEncrypt;
	
	@Column(name = "rolegodung_code")
	private String rolegodungCode;
	
	@Column(name = "national_number")
	@Length(max = 13, message = "{validation.max.lenght}")
	private String nationalNumber;
	
	@Column(name = "tax_number")
	@Length(max = 13, message = "{validation.max.lenght}")
	private String taxNumber;
	
	@Column(name = "title")
	@NotEmpty(message = "{form.rolegodung.valid.title}")
	@Length(max = 50, message = "{validation.max.lenght}")
	private String title;
	
	@Column(name = "first_name")
	@NotEmpty(message = "{form.rolegodung.valid.firstname}")
	@Length(max = 50, message = "{validation.max.lenght}")
	private String firstName;
	
	@Column(name = "last_name")
	@NotEmpty(message = "{form.rolegodung.valid.lastname}")
	@Length(max = 50, message = "{validation.max.lenght}")
	private String lastName;
	
	@Column(name = "telephone")
	@Length(max = 50, message = "{validation.max.lenght}")
	private String telephone;
	
	@Column(name = "email")
	@Length(max = 50, message = "{validation.max.lenght}")
	private String email;
	
	@Column(name = "description")
	@Length(max = 50, message = "{validation.max.lenght}")
	private String description;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	@Valid
	private Address address;
	
	@ManyToOne()
	@JoinColumn(name = "godung_id")
	@JsonIgnore
	private Godung godung;
	
	public void setObject(Rolegodung rolegodung) {
		this.title = rolegodung.getTitle();
		this.firstName = rolegodung.getFirstName();
		this.lastName = rolegodung.getLastName();
		this.nationalNumber = rolegodung.getNationalNumber();
		this.taxNumber = rolegodung.getTaxNumber();
		this.telephone = rolegodung.getTelephone();
		this.email = rolegodung.getEmail();
	}

	public Long getRolegodungId() {
		return rolegodungId;
	}

	public void setRolegodungId(Long rolegodungId) {
		this.rolegodungId = rolegodungId;
	}

	public String getRolegodungCode() {
		return rolegodungCode;
	}

	public void setRolegodungCode(String rolegodungCode) {
		this.rolegodungCode = rolegodungCode;
	}

	public String getNationalNumber() {
		return nationalNumber;
	}

	public void setNationalNumber(String nationalNumber) {
		this.nationalNumber = nationalNumber;
	}

	public String getTaxNumber() {
		return taxNumber;
	}

	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Godung getGodung() {
		return godung;
	}

	public void setGodung(Godung godung) {
		this.godung = godung;
	}

	public String getRolegodungIdEncrypt() {
		return rolegodungIdEncrypt;
	}

	public void setRolegodungIdEncrypt(String rolegodungIdEncrypt) {
		this.rolegodungIdEncrypt = rolegodungIdEncrypt;
	}

	@Override
	public String toString() {
		return "Rolegodung [rolegodungId=" + rolegodungId + ", rolegodungIdEncrypt=" + rolegodungIdEncrypt + ", rolegodungCode="
				+ rolegodungCode + ", nationalNumber=" + nationalNumber + ", taxNumber=" + taxNumber + ", title=" + title
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", telephone=" + telephone + ", email="
				+ email + ", description=" + description + ", address=" + address + ", godung=" + godung + "]";
	}

	
}
