package com.yeamgood.godungonline.form;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

public class PurchaseRequestForm {
	
	private String purchaseRequestIdEncrypt;
	
	private String purchaseRequestCode;
	
	private String employeeIdEncrypt;
	
	@NotEmpty(message = "{form.purchaseRequest.valid.employeename}")
	@Length(max = 150, message = "{validation.max.lenght}")
	private String employeeName;
	
	@NotEmpty(message = "{form.purchaseRequest.valid.requestdate}")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private String requestDate;
	
	@NotEmpty(message = "{form.purchaseRequest.valid.demanddate}")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private String demandDate;
	
	@Length(max = 50, message = "{validation.max.lenght}")
	private String referenceNumber;
	
	@Length(max = 200, message = "{validation.max.lenght}")
	private String description;
	
	private String totalAmount;
	
	private String totalPrice;
	
	private List<PurchaseRequestProductForm> purchaseRequestProductFormList;

	public String getPurchaseRequestIdEncrypt() {
		return purchaseRequestIdEncrypt;
	}

	public void setPurchaseRequestIdEncrypt(String purchaseRequestIdEncrypt) {
		this.purchaseRequestIdEncrypt = purchaseRequestIdEncrypt;
	}

	public String getPurchaseRequestCode() {
		return purchaseRequestCode;
	}

	public void setPurchaseRequestCode(String purchaseRequestCode) {
		this.purchaseRequestCode = purchaseRequestCode;
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

	public String getDemandDate() {
		return demandDate;
	}

	public void setDemandDate(String demandDate) {
		this.demandDate = demandDate;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getEmployeeIdEncrypt() {
		return employeeIdEncrypt;
	}

	public void setEmployeeIdEncrypt(String employeeIdEncrypt) {
		this.employeeIdEncrypt = employeeIdEncrypt;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public List<PurchaseRequestProductForm> getPurchaseRequestProductFormList() {
		return purchaseRequestProductFormList;
	}

	public void setPurchaseRequestProductFormList(List<PurchaseRequestProductForm> purchaseRequestProductFormList) {
		this.purchaseRequestProductFormList = purchaseRequestProductFormList;
	}

	@Override
	public String toString() {
		return "PurchaseRequestForm [purchaseRequestIdEncrypt=" + purchaseRequestIdEncrypt + ", purchaseRequestCode="
				+ purchaseRequestCode + ", employeeIdEncrypt=" + employeeIdEncrypt + ", employeeName=" + employeeName
				+ ", requestDate=" + requestDate + ", demandDate=" + demandDate + ", referenceNumber=" + referenceNumber
				+ ", description=" + description + ", totalAmount=" + totalAmount + ", totalPrice=" + totalPrice
				+ ", purchaseRequestProductFormList=" + purchaseRequestProductFormList + "]";
	}
	
}
