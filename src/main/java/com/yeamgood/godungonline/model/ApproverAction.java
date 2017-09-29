package com.yeamgood.godungonline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "approveraction")
public class ApproverAction{

	@Id
	@Column(name = "approveraction_code")
	private String approverActionCode;

	@Column(name = "name")
	private String name;

	@Column(name = "message")
	private String message;

	public String getApproverActionCode() {
		return approverActionCode;
	}

	public void setApproverActionCode(String approverActionCode) {
		this.approverActionCode = approverActionCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
