package com.yeamgood.godungonline.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

public class DealerForm {


	private Long dealerId;
	
	private String dealerIdEncrypt;
	
	private String productIdEncrypt;

	@NotBlank(message = "{form.dealer.validation.price}")
	@Length(max = 16, message = "{currency.max.16}")
	private String price;
	
	@NotBlank(message = "{form.dealer.validation.startdate}")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private String startDate;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private String endDate;
	
	@NotBlank(message = "{form.dealer.validation.supplier}")
	private String supplierIdEncrypt;
	
	@NotBlank(message = "{form.dealer.validation.measure}")
	private String measureIdEncrypt;
	
	@NotNull(message = "{form.dealer.validation.currency}")
	private Long currencyId;

	public Long getDealerId() {
		return dealerId;
	}

	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}

	public String getDealerIdEncrypt() {
		return dealerIdEncrypt;
	}

	public void setDealerIdEncrypt(String dealerIdEncrypt) {
		this.dealerIdEncrypt = dealerIdEncrypt;
	}

	public String getProductIdEncrypt() {
		return productIdEncrypt;
	}

	public void setProductIdEncrypt(String productIdEncrypt) {
		this.productIdEncrypt = productIdEncrypt;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSupplierIdEncrypt() {
		return supplierIdEncrypt;
	}

	public void setSupplierIdEncrypt(String supplierIdEncrypt) {
		this.supplierIdEncrypt = supplierIdEncrypt;
	}

	public String getMeasureIdEncrypt() {
		return measureIdEncrypt;
	}

	public void setMeasureIdEncrypt(String measureIdEncrypt) {
		this.measureIdEncrypt = measureIdEncrypt;
	}

	public Long getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}

	@Override
	public String toString() {
		return "DealerForm [dealerId=" + dealerId + ", dealerIdEncrypt=" + dealerIdEncrypt + ", productIdEncrypt="
				+ productIdEncrypt + ", price=" + price + ", startdate=" + startDate
				+ ", enddate=" + endDate + ", supplierIdEncrypt=" + supplierIdEncrypt
				+ ", measureIdEncrypt=" + measureIdEncrypt + ", currencyId=" + currencyId + "]";
	}
	
}
