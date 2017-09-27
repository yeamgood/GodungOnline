package com.yeamgood.godungonline.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yeamgood.godungonline.model.template.ModelTemplate;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "purchaserequest")
public class PurchaseRequest extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "purchaserequest_Id")
	private Long purchaseRequestId;
	
	@Transient
	private String purchaseRequestIdEncrypt;
	
	@Column(name = "purchaserequest_code")
	private String purchaseRequestCode;
	
	@ManyToOne()
	@JoinColumn(name = "employee_id")
	private Employee employee;
	
	@Column(name = "requst_date")
	private Date requestDate;
	
	@Column(name = "demand_date")
	private Date demandDate;
	
	@Column(name = "reference_number")
	private String referenceNumber;
	
	@Column(name = "description")
	private String description;
	
	@ManyToOne()
	@JoinColumn(name = "godung_id")
	@JsonIgnore
	private Godung godung;
	
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval=true)
	@JoinTable(name = "purchaserequest_purchaserequestproduct", joinColumns = @JoinColumn(name = "purchaserequest_Id"), inverseJoinColumns = @JoinColumn(name = "purchaserequestproduct_Id"))
	private List<PurchaseRequestProduct> purchaseRequestProductList;
	
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval=true)
	@JoinTable(name = "purchaserequest_approver", joinColumns = @JoinColumn(name = "purchaserequest_Id"), inverseJoinColumns = @JoinColumn(name = "approver_id"))
	private List<Approver> approverList;
	
	public void setObject(PurchaseRequest purchaseRequest) {
		this.description  = purchaseRequest.getDescription();
	}
	
	public void encryptData() {
		this.purchaseRequestIdEncrypt = AESencrpUtils.encryptLong(this.purchaseRequestId);
		this.purchaseRequestId = null;
	}

	public Long getPurchaseRequestId() {
		return purchaseRequestId;
	}

	public void setPurchaseRequestId(Long purchaseRequestId) {
		this.purchaseRequestId = purchaseRequestId;
	}

	public String getPurchaseRequestCode() {
		return purchaseRequestCode;
	}

	public void setPurchaseRequestCode(String purchaseRequestCode) {
		this.purchaseRequestCode = purchaseRequestCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Godung getGodung() {
		return godung;
	}

	public void setGodung(Godung godung) {
		this.godung = godung;
	}

	public String getPurchaseRequestIdEncrypt() {
		return purchaseRequestIdEncrypt;
	}

	public void setPurchaseRequestIdEncrypt(String purchaseRequestIdEncrypt) {
		this.purchaseRequestIdEncrypt = purchaseRequestIdEncrypt;
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

	public Date getDemandDate() {
		return demandDate;
	}

	public void setDemandDate(Date demandDate) {
		this.demandDate = demandDate;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
	public List<PurchaseRequestProduct> getPurchaseRequestProductList() {
		return purchaseRequestProductList;
	}

	public void setPurchaseRequestProductList(List<PurchaseRequestProduct> purchaseRequestProductList) {
		this.purchaseRequestProductList = purchaseRequestProductList;
	}

	public List<Approver> getApproverList() {
		return approverList;
	}

	public void setApproverList(List<Approver> approverList) {
		this.approverList = approverList;
	}

	@Override
	public String toString() {
		return "PurchaseRequest [purchaseRequestId=" + purchaseRequestId + ", purchaseRequestIdEncrypt="
				+ purchaseRequestIdEncrypt + ", purchaseRequestCode=" + purchaseRequestCode + ", employee=" + employee
				+ ", requestDate=" + requestDate + ", demandDate=" + demandDate + ", referenceNumber=" + referenceNumber
				+ ", description=" + description + ", godung=" + godung + ", purchaseRequestProductList="
				+ purchaseRequestProductList + ", approverList=" + approverList + "]";
	}
	
}
