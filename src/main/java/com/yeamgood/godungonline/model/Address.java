package com.yeamgood.godungonline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.yeamgood.godungonline.model.template.ModelTemplate;

@Entity
@Table(name = "address")
public class Address extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "address_id")
	private Long addressId;
	
	@Column(name = "street1")
	@Length(max = 50, message = "{validation.max.lenght}")
	private String street1;
	
	@Column(name = "street2")
	@Length(max = 50, message = "{validation.max.lenght}")
	private String street2;
	
	@Column(name = "city")
	@Length(max = 50, message = "{validation.max.lenght}")
	private String city;
	
	@ManyToOne
    @JoinColumn(name = "province_code")
    private Province province;
	
	@Column(name = "postcode")
	@Length(max = 5, message = "{validation.max.lenght}")
	private String postcode;
	
	@ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
	
	public void setObject(Address address) {
		this.street1 = address.getStreet1();
		this.street2 = address.getStreet2();
		this.city = address.getCity();
		this.postcode = address.getPostcode();
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "Address [addressId=" + addressId + ", street1=" + street1 + ", street2=" + street2 + ", city=" + city
				+ ", province=" + province + ", postcode=" + postcode + ", country=" + country + "]";
	}

}
