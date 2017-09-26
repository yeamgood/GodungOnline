package com.yeamgood.godungonline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yeamgood.godungonline.model.template.ModelTemplate;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "brand")
public class Brand extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "brand_Id")
	private Long brandId;
	
	@Transient
	private String brandIdEncrypt;
	
	@Column(name = "brand_code")
	private String brandCode;
	
	@Column(name = "brand_name")
	@NotEmpty(message = "{form.brand.valid.name}")
	@Length(max = 100, message = "{validation.max.lenght}")
	private String brandName;
	
	@Column(name = "description")
	@Length(max = 200, message = "{validation.max.lenght}")
	private String description;
	
	@ManyToOne()
	@JoinColumn(name = "godung_id")
	@JsonIgnore
	private Godung godung;
	
	public void encryptData() {
		this.brandIdEncrypt = AESencrpUtils.encryptLong(this.brandId);
		this.brandId = null;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Godung getGodung() {
		return godung;
	}

	public void setGodung(Godung godung) {
		this.godung = godung;
	}
	
	public String getBrandIdEncrypt() {
		return brandIdEncrypt;
	}

	public void setBrandIdEncrypt(String brandIdEncrypt) {
		this.brandIdEncrypt = brandIdEncrypt;
	}

	@Override
	public String toString() {
		return "Brand [brandId=" + brandId + ", brandIdEncrypt=" + brandIdEncrypt + ", brandCode=" + brandCode
				+ ", brandName=" + brandName + ", description=" + description + ", godung=" + godung + "]";
	}
	
	
}
