package com.yeamgood.godungonline.form;

import javax.persistence.Column;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.yeamgood.godungonline.model.User;

public class UserPasswordForm {
	
	private String userIdEncrypt;
	
	@Column(name = "password")
	@Length(min = 5, message = "{validation.required.password.valid}")
	@NotEmpty(message = "{validation.required.password}")
	private String password;
	
	private String confirmPassword;
	
	public void mapObjectToForm(User user) {
		this.userIdEncrypt = user.getUserIdEncrypt();
		this.password = user.getPassword();
		this.confirmPassword = user.getConfirmPassword();
	}
	
	public String getUserIdEncrypt() {
		return userIdEncrypt;
	}
	public void setUserIdEncrypt(String userIdEncrypt) {
		this.userIdEncrypt = userIdEncrypt;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public String toString() {
		return "UserPasswordForm [userIdEncrypt=" + userIdEncrypt + ", password=" + password + ", confirmPassword="
				+ confirmPassword + "]";
	}
	
}
