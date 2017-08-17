package com.yeamgood.godungonline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "godung")
public class Godung extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "godung_id")
	private long id;
	
	@Column(name = "godung_name")
	private String name;

	@Column(name = "active")
	
	private int active;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

}
