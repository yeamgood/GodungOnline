package com.yeamgood.godungonline.form;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class ProfileForm {
	
	private String email;
	
	@NotEmpty(message = "{validation.required.name}")
	@Size(max=50, message="{validation.max.lenght}")
	private String name;
	
	@NotEmpty(message = "{validation.required.language}")
	private String language;
	
	@NotEmpty(message = "{validation.required.godung.name}")
	@Size(max=25, message="{validation.max.lenght}")
	private String godungName;
	
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

	public String getGodungName() {
		return godungName;
	}

	public void setGodungName(String godungName) {
		this.godungName = godungName;
	}

}
