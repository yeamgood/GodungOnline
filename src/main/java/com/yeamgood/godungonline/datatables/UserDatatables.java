package com.yeamgood.godungonline.datatables;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.yeamgood.godungonline.model.RoleLogin;
import com.yeamgood.godungonline.model.User;

public class UserDatatables {

	private String userIdEncrypt;
	
	private String email;
	
	private String password;
	
	private String confirmPassword;

	private String name;
	
	private String language;
	
	private int active;
	
	private String roleLogin;
	
	public void setDatatableByModel(User user) {
		this.userIdEncrypt = user.getUserIdEncrypt();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.name = user.getName();
		this.language = user.getLanguage();
		this.active = user.getActive();
		this.roleLogin = getRoleLogin(user.getRoles());
	}
	
	public String getRoleLogin(Set<RoleLogin> roles) {
		String roleList = "";
		for (RoleLogin roleLogin : roles) {
			if(StringUtils.isNotBlank(roleList)) {
				roleList += ",";
			}
			roleList += roleLogin.getRole();
		}
		return roleList;
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

	public String getRoleLogin() {
		return roleLogin;
	}

	public void setRoleLogin(String roleLogin) {
		this.roleLogin = roleLogin;
	}
	
}
