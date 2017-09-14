package com.yeamgood.godungonline.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.yeamgood.godungonline.model.template.ModelTemplate;

public class PriceForm extends ModelTemplate{

	private Long priceId;
	
	private String priceIdEncrypt;

	@NotNull(message = "{form.price.validation.startdate}")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date startDate;

	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date endDate;
	
	@NotNull(message = "{form.price.validation.price}")
	private String priceText;
	
	@NotNull(message = "{form.price.validation.measure}")
	private String measureIdEncrypt;
	
	@NotNull(message = "{form.price.validation.currency}")
	private Long currencyId;
	
	private String productIdEncrypt;
	
	private String startDateText;
	
	private String endDateText;
	
	private String measureName;
	
	private String currencyName;

	public Long getPriceId() {
		return priceId;
	}

	public void setPriceId(Long priceId) {
		this.priceId = priceId;
	}

	public String getPriceIdEncrypt() {
		return priceIdEncrypt;
	}

	public void setPriceIdEncrypt(String priceIdEncrypt) {
		this.priceIdEncrypt = priceIdEncrypt;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getPriceText() {
		return priceText;
	}

	public void setPriceText(String priceText) {
		this.priceText = priceText;
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

	public String getProductIdEncrypt() {
		return productIdEncrypt;
	}

	public void setProductIdEncrypt(String productIdEncrypt) {
		this.productIdEncrypt = productIdEncrypt;
	}

	public String getStartDateText() {
		return startDateText;
	}

	public void setStartDateText(String startDateText) {
		this.startDateText = startDateText;
	}

	public String getEndDateText() {
		return endDateText;
	}

	public void setEndDateText(String endDateText) {
		this.endDateText = endDateText;
	}

	public String getMeasureName() {
		return measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	@Override
	public String toString() {
		return "PriceForm [priceId=" + priceId + ", priceIdEncrypt=" + priceIdEncrypt + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", priceText=" + priceText + ", measureIdEncrypt=" + measureIdEncrypt
				+ ", currencyId=" + currencyId + ", productIdEncrypt=" + productIdEncrypt + ", startDateText="
				+ startDateText + ", endDateText=" + endDateText + ", measureName=" + measureName + ", currencyName="
				+ currencyName + "]";
	}
	
}
