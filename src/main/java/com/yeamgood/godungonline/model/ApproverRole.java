package com.yeamgood.godungonline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "approverrole")
public class ApproverRole{

	@Id
	@Column(name = "approverrole_code")
	private String approverRoleCode;

	@Column(name = "name")
	private String name;

	@Column(name = "message")
	private String message;
	
	@Column(name = "sequence")
	private String sequence;

	public String getApproverRoleCode() {
		return approverRoleCode;
	}

	public void setApproverRoleCode(String approverRoleCode) {
		this.approverRoleCode = approverRoleCode;
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

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
}
