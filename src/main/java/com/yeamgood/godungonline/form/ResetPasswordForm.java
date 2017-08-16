package com.yeamgood.godungonline.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class ResetPasswordForm {
	
	@Length(min = 5, message = "{validation.required.password.valid}")
	@NotEmpty(message = "{validation.required.password}")
	private String password;
	
	@NotEmpty(message = "{validation.required.password.confirm}")
	private String confirmPassword;

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

}
