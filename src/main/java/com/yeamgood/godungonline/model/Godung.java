package com.yeamgood.godungonline.model;

import java.util.List;

import javax.persistence.CascadeType;
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
	private Long id;
	
	@Column(name = "godung_name")
	@NotEmpty(message = "{validation.required.godung.name}")
	private String name;

	@Column(name = "active")
	
	@OneToMany(mappedBy = "godung", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<GodungUserRole> godungUserRoleList;
	
	private int active;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
