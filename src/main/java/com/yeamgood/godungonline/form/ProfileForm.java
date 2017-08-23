package com.yeamgood.godungonline.form;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.yeamgood.godungonline.model.Common;

public class ProfileForm {
	
	private String email;
	
	@NotEmpty(message = "{validation.required.name}")
	private String name;
	
	@NotEmpty(message = "{validation.required.language}")
	private String language;
	
	@NotEmpty(message = "{validation.required.godung.name}")
	private String godungName;
	
	private List<Common> languageList;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<Common> getLanguageList() {
		return languageList;
	}

	public void setLanguageList(List<Common> languageList) {
		this.languageList = languageList;
	}

	public String getGodungName() {
		return godungName;
	}

	public void setGodungName(String godungName) {
		this.godungName = godungName;
	}
	
}
