package com.yeamgood.godungonline.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.yeamgood.godungonline.model.template.ModelTemplate;

@Entity
@Table(name = "godung")
public class Godung extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "godung_id")
	private Long godungId;
	
	@Column(name = "godung_name")
	@NotEmpty(message = "{validation.required.godung.name}")
	private String godungName;

	@Column(name = "active")
	private int active;
	
	@OneToMany(mappedBy = "godung")
    private List<GodungUserRole> godungUserRoleList;

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


}
