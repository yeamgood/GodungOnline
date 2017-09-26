package com.yeamgood.godungonline.form;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

public class ApproverForm {
	
	@Transient
	private String approverIdEncrypt;

	@NotNull(message = "{form.approver.valid.employee}")
	private String employeeIdEncrypt;
	
	private String employeeName;

	private String requestDate;
	
	@NotNull(message = "{form.approver.valid.role}")
	private String approverRoleCode;
	
	private String approverRoleName;
	
	private String approverDate;
	
	private String approverActionCode;
	
	private String description;

	public String getApproverIdEncrypt() {
		return approverIdEncrypt;
	}

	public void setApproverIdEncrypt(String approverIdEncrypt) {
		this.approverIdEncrypt = approverIdEncrypt;
	}

	public String getEmployeeIdEncrypt() {
		return employeeIdEncrypt;
	}

	public void setEmployeeIdEncrypt(String employeeIdEncrypt) {
		this.employeeIdEncrypt = employeeIdEncrypt;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public String getApproverRoleCode() {
		return approverRoleCode;
	}

	public void setApproverRoleCode(String approverRoleCode) {
		this.approverRoleCode = approverRoleCode;
	}

	public String getApproverRoleName() {
		return approverRoleName;
	}

	public void setApproverRoleName(String approverRoleName) {
		this.approverRoleName = approverRoleName;
	}

	public String getApproverDate() {
		return approverDate;
	}

	public void setApproverDate(String approverDate) {
		this.approverDate = approverDate;
	}

	public String getApproverActionCode() {
		return approverActionCode;
	}

	public void setApproverActionCode(String approverActionCode) {
		this.approverActionCode = approverActionCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ApproverForm [approverIdEncrypt=" + approverIdEncrypt + ", employeeIdEncrypt=" + employeeIdEncrypt
				+ ", employeeName=" + employeeName + ", requestDate=" + requestDate + ", approverRoleCode="
				+ approverRoleCode + ", approverRoleName=" + approverRoleName + ", approverDate=" + approverDate
				+ ", approverActionCode=" + approverActionCode + ", description=" + description + "]";
	}
	
}
