package com.yeamgood.godungonline.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yeamgood.godungonline.model.template.ModelTemplate;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "godung")
public class Godung extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "godung_id")
	private Long godungId;
	
	@Transient
	private String godungIdEncrypt;
	
	@Column(name = "godung_name")
	@NotEmpty(message = "{validation.required.godung.name}")
	private String godungName;

	@Column(name = "active")
	private int active;
	
	@OneToMany(mappedBy = "godung")
	@JsonIgnore
    private List<GodungUserRole> godungUserRoleList;

	public void encryptData() {
		this.godungIdEncrypt = AESencrpUtils.encryptLong(this.godungId);
		this.godungId = null;
	}
	
	public Long getGodungId() {
		return godungId;
	}

	public void setGodungId(Long godungId) {
		this.godungId = godungId;
	}

	public String getGodungName() {
		return godungName;
	}

	public void setGodungName(String godungName) {
		this.godungName = godungName;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public List<GodungUserRole> getGodungUserRoleList() {
		return godungUserRoleList;
	}

	public void setGodungUserRoleList(List<GodungUserRole> godungUserRoleList) {
		this.godungUserRoleList = godungUserRoleList;
	}

	public String getGodungIdEncrypt() {
		return godungIdEncrypt;
	}

	public void setGodungIdEncrypt(String godungIdEncrypt) {
		this.godungIdEncrypt = godungIdEncrypt;
	}

	@Override
	public String toString() {
		return "Godung [godungId=" + godungId + ", godungIdEncrypt=" + godungIdEncrypt + ", godungName=" + godungName
				+ ", active=" + active + "]";
	}
	
}
