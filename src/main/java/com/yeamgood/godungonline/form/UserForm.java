package com.yeamgood.godungonline.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.yeamgood.godungonline.model.User;

public class UserForm {
	
	private String userIdEncrypt;
	
	@Email(message = "{validation.required.email.valid}")
	@NotEmpty(message = "{validation.required.email}")
	private String email;
	
	@NotEmpty(message = "{validation.required.name}")
	private String name;
	
	@NotEmpty(message = "{validation.required.language}")
	private String language;
	
	private int active;
	
	public void mapObjectToForm(User user) {
		this.userIdEncrypt = user.getUserIdEncrypt();
		this.email = user.getEmail();
		this.name = user.getName();
		this.language = user.getLanguage();
		this.active = user.getActive();
	}
	
	public String getUserIdEncrypt() {
		return userIdEncrypt;
	}
	public void setUserIdEncrypt(String userIdEncrypt) {
		this.userIdEncrypt = userIdEncrypt;
	}
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
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "UserForm [userIdEncrypt=" + userIdEncrypt + ", email=" + email + ", name=" + name + ", language="
				+ language + ", active=" + active + "]";
	}
	
}
