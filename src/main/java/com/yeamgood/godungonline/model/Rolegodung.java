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
	
	@Column(name = "rolegodung_name")
	@NotEmpty(message = "{form.rolegodung.valid.name}")
	@Length(max = 100, message = "{validation.max.lenght}")
	private String rolegodungName;
	
	@Column(name = "description")
	@Length(max = 200, message = "{validation.max.lenght}")
	private String description;
	
	@ManyToOne()
	@JoinColumn(name = "godung_id")
	@JsonIgnore
	private Godung godung;
	
	public void setObject(Rolegodung rolegodung) {
		this.rolegodungName = rolegodung.getRolegodungName();
		this.description  = rolegodung.getDescription();
	}
	
	public void encryptData() {
		this.rolegodungIdEncrypt = AESencrpUtils.encryptLong(this.rolegodungId);
		this.rolegodungId = null;
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

	public String getRolegodungName() {
		return rolegodungName;
	}

	public void setRolegodungName(String rolegodungName) {
		this.rolegodungName = rolegodungName;
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

	public String getRolegodungIdEncrypt() {
		return rolegodungIdEncrypt;
	}

	public void setRolegodungIdEncrypt(String rolegodungIdEncrypt) {
		this.rolegodungIdEncrypt = rolegodungIdEncrypt;
	}

	@Override
	public String toString() {
		return "Rolegodung [rolegodungId=" + rolegodungId + ", rolegodungIdEncrypt=" + rolegodungIdEncrypt
				+ ", rolegodungCode=" + rolegodungCode + ", rolegodungName=" + rolegodungName + ", description="
				+ description + ", godung=" + godung + "]";
	}
	
}
