package com.yeamgood.godungonline.form;

import org.hibernate.validator.constraints.NotBlank;

public class ApproverForm {
	
	private String approverIdEncrypt;
	
	private String purchaseRequestIdEncrypt;
	
	@NotBlank(message = "{form.purchaseRequestProduct.valid.product}")
	private String employeeIdEncrypt;
	
	private String employeeName;

	private String requestDate;
	
	private String approverDate;
	
	@NotBlank(message = "{form.purchaseRequestProduct.valid.product}")
	private String approverRoleCode;
	
	private String approverRoleName;
	
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

	public String getApproverDate() {
		return approverDate;
	}

	public void setApproverDate(String approverDate) {
		this.approverDate = approverDate;
	}

	public String getApproverRoleCode() {
		return approverRoleCode;
	}

	public void setApproverRoleCode(String approverRoleCode) {
		this.approverRoleCode = approverRoleCode;
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
	
	public String getPurchaseRequestIdEncrypt() {
		return purchaseRequestIdEncrypt;
	}

	public void setPurchaseRequestIdEncrypt(String purchaseRequestIdEncrypt) {
		this.purchaseRequestIdEncrypt = purchaseRequestIdEncrypt;
	}
	
	public String getApproverRoleName() {
		return approverRoleName;
	}

	public void setApproverRoleName(String approverRoleName) {
		this.approverRoleName = approverRoleName;
	}

	@Override
	public String toString() {
		return "ApproverForm [approverIdEncrypt=" + approverIdEncrypt + ", purchaseRequestIdEncrypt="
				+ purchaseRequestIdEncrypt + ", employeeIdEncrypt=" + employeeIdEncrypt + ", employeeName="
				+ employeeName + ", requestDate=" + requestDate + ", approverDate=" + approverDate
				+ ", approverRoleCode=" + approverRoleCode + ", approverRoleName=" + approverRoleName
				+ ", approverActionCode=" + approverActionCode + ", description=" + description + "]";
	}
	
}
