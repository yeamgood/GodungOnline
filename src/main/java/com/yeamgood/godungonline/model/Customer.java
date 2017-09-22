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
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "customer")
public class Customer extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "customer_Id")
	private Long customerId;
	
	@Transient
	private String customerIdEncrypt;
	
	@Column(name = "customer_code")
	private String customerCode;
	
	@Column(name = "customer_type")
	private String customerType;
	
	@Column(name = "national_number")
	@Length(max = 13, message = "{validation.max.lenght}")
	private String nationalNumber;
	
	@Column(name = "tax_number")
	@Length(max = 13, message = "{validation.max.lenght}")
	private String taxNumber;
	
	@Column(name = "title")
	@Length(max = 50, message = "{validation.max.lenght}")
	private String title;
	
	@Column(name = "first_name")
	@NotEmpty(message = "{form.customer.valid.firstname}")
	@Length(max = 50, message = "{validation.max.lenght}")
	private String firstName;
	
	@Column(name = "last_name")
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
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_send_id")
	@Valid
	private Address addressSend;
	
	@ManyToOne()
	@JoinColumn(name = "godung_id")
	@JsonIgnore
	private Godung godung;
	
	public void setObject(Customer customer) {
		this.title = customer.getTitle();
		this.firstName = customer.getFirstName();
		this.lastName = customer.getLastName();
		this.nationalNumber = customer.getNationalNumber();
		this.taxNumber = customer.getTaxNumber();
		this.telephone = customer.getTelephone();
		this.email = customer.getEmail();
	}
	
	public void encryptData(Customer customer) {
		this.customerIdEncrypt = AESencrpUtils.encryptLong(customer.getCustomerId());
		this.customerId = null;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
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

	public Address getAddressSend() {
		return addressSend;
	}

	public void setAddressSend(Address addressSend) {
		this.addressSend = addressSend;
	}

	public Godung getGodung() {
		return godung;
	}

	public void setGodung(Godung godung) {
		this.godung = godung;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCustomerIdEncrypt() {
		return customerIdEncrypt;
	}

	public void setCustomerIdEncrypt(String customerIdEncrypt) {
		this.customerIdEncrypt = customerIdEncrypt;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", customerIdEncrypt=" + customerIdEncrypt + ", customerCode="
				+ customerCode + ", customerType=" + customerType + ", nationalNumber=" + nationalNumber
				+ ", taxNumber=" + taxNumber + ", title=" + title + ", firstName=" + firstName + ", lastName="
				+ lastName + ", telephone=" + telephone + ", email=" + email + ", description=" + description
				+ ", address=" + address + ", addressSend=" + addressSend + ", godung=" + godung + "]";
	}


	
}
