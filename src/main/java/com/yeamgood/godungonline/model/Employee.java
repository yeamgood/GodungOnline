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
@Table(name = "employee")
public class Employee extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "employee_Id")
	private Long employeeId;
	
	@Transient
	private String employeeIdEncrypt;
	
	@Column(name = "employee_code")
	private String employeeCode;
	
	@Column(name = "national_number")
	@Length(max = 13, message = "{validation.max.lenght}")
	private String nationalNumber;
	
	@Column(name = "tax_number")
	@Length(max = 13, message = "{validation.max.lenght}")
	private String taxNumber;
	
	@Column(name = "title")
	@NotEmpty(message = "{form.employee.valid.title}")
	@Length(max = 50, message = "{validation.max.lenght}")
	private String title;
	
	@Column(name = "first_name")
	@NotEmpty(message = "{form.employee.valid.firstname}")
	@Length(max = 50, message = "{validation.max.lenght}")
	private String firstName;
	
	@Column(name = "last_name")
	@NotEmpty(message = "{form.employee.valid.lastname}")
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
	
	@ManyToOne()
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne()
	@JoinColumn(name = "rolegodung_id",nullable = true)
	private Rolegodung rolegodung;
	
	public void setObject(Employee employee) {
		this.title = employee.getTitle();
		this.firstName = employee.getFirstName();
		this.lastName = employee.getLastName();
		this.nationalNumber = employee.getNationalNumber();
		this.taxNumber = employee.getTaxNumber();
		this.telephone = employee.getTelephone();
		this.email = employee.getEmail();
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
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

	public String getEmployeeIdEncrypt() {
		return employeeIdEncrypt;
	}

	public void setEmployeeIdEncrypt(String employeeIdEncrypt) {
		this.employeeIdEncrypt = employeeIdEncrypt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Rolegodung getRolegodung() {
		return rolegodung;
	}

	public void setRolegodung(Rolegodung rolegodung) {
		this.rolegodung = rolegodung;
	}

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", employeeIdEncrypt=" + employeeIdEncrypt + ", employeeCode="
				+ employeeCode + ", nationalNumber=" + nationalNumber + ", taxNumber=" + taxNumber + ", title=" + title
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", telephone=" + telephone + ", email="
				+ email + ", description=" + description + ", address=" + address + ", godung=" + godung + ", user="
				+ user + ", rolegodung=" + rolegodung + "]";
	}

}
