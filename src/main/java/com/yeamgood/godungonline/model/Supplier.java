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
@Table(name = "supplier")
public class Supplier extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "supplier_Id")
	private Long supplierId;
	
	@Transient
	private String supplierIdEncrypt;
	
	@Column(name = "supplier_code")
	private String supplierCode;
	
	@Column(name = "supplier_type")
	private String supplierType;
	
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
	@NotEmpty(message = "{form.supplier.valid.firstname}")
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
	
	public void setObject(Supplier supplier) {
		this.title = supplier.getTitle();
		this.firstName = supplier.getFirstName();
		this.lastName = supplier.getLastName();
		this.nationalNumber = supplier.getNationalNumber();
		this.taxNumber = supplier.getTaxNumber();
		this.telephone = supplier.getTelephone();
		this.email = supplier.getEmail();
	}

	public void encryptData(Supplier supplier) throws Exception {
		this.supplierIdEncrypt = AESencrpUtils.encryptLong(supplier.getSupplierId());
		this.supplierId = null;
	}
	
	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
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

	public String getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(String supplierType) {
		this.supplierType = supplierType;
	}

	public String getSupplierIdEncrypt() {
		return supplierIdEncrypt;
	}

	public void setSupplierIdEncrypt(String supplierIdEncrypt) {
		this.supplierIdEncrypt = supplierIdEncrypt;
	}

	@Override
	public String toString() {
		return "Supplier [supplierId=" + supplierId + ", supplierIdEncrypt=" + supplierIdEncrypt + ", supplierCode="
				+ supplierCode + ", supplierType=" + supplierType + ", nationalNumber=" + nationalNumber
				+ ", taxNumber=" + taxNumber + ", title=" + title + ", firstName=" + firstName + ", lastName="
				+ lastName + ", telephone=" + telephone + ", email=" + email + ", description=" + description
				+ ", address=" + address + ", addressSend=" + addressSend + ", godung=" + godung + "]";
	}


	
}
