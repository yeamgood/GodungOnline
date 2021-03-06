package com.yeamgood.godungonline.model.template;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.yeamgood.godungonline.model.User;

@MappedSuperclass 
public class ModelTemplate {

	@Column(name = "create_user")
	public String createUser;
	
	@Column(name = "create_date")
	public Date createDate;
	
	@Column(name = "update_user")
	public String updateUser;
	
	@Column(name = "update_date")
	public Date updateDate;
	
	@Version
	@Column(name = "version")
	public int version;
	
	public void setCreate(User user) {
		this.createUser = user.getEmail();
		this.createDate = new Date();
	}
	
	public void setUpdate(User user) {
		this.updateUser = user.getEmail();
		this.updateDate = new Date();
	}
	
	public void setCreateAndUpdate(User user) {
		this.createUser = user.getEmail();
		this.createDate = new Date();
		this.updateUser = user.getEmail();
		this.updateDate = new Date();
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	

}
