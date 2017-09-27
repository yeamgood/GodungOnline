package com.yeamgood.godungonline.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.yeamgood.godungonline.model.template.ModelTemplate;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "approver")
public class Approver extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "approver_id")
	private Long approverId;
	
	@Transient
	private String approverIdEncrypt;
	
	@ManyToOne()
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@Column(name = "request_date")
	private Date requestDate;
	
	@Column(name = "approve_date")
	private Date approverDate;
	
	@JoinColumn(name = "approver_role_code")
	private String approverRoleCode;
	
	@JoinColumn(name = "approve_action")
	private String approverActionCode;
	
	@Column(name = "description")
	private String description;
	
	public void encryptData() {
		this.approverIdEncrypt = AESencrpUtils.encryptLong(this.approverId);
		this.approverId = null;
	}

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}

	public String getApproverIdEncrypt() {
		return approverIdEncrypt;
	}

	public void setApproverIdEncrypt(String approverIdEncrypt) {
		this.approverIdEncrypt = approverIdEncrypt;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getApproverRoleCode() {
		return approverRoleCode;
	}

	public void setApproverRoleCode(String approverRoleCode) {
		this.approverRoleCode = approverRoleCode;
	}

	public Date getApproverDate() {
		return approverDate;
	}

	public void setApproverDate(Date approverDate) {
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

	

}
