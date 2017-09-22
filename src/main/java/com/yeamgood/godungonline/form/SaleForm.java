package com.yeamgood.godungonline.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.yeamgood.godungonline.model.template.ModelTemplate;

public class SaleForm extends ModelTemplate{

	private Long saleId;
	
	private String saleIdEncrypt;

	@NotNull(message = "{form.sale.validation.startdate}")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date startDate;

	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date endDate;
	
	@NotNull(message = "{form.sale.validation.price}")
	@Length(max = 16, message = "{currency.max.16}")
	private String price;
	
	@NotNull(message = "{form.sale.validation.measure}")
	private String measureIdEncrypt;
	
	@NotNull(message = "{form.sale.validation.currency}")
	private Long currencyId;
	
	private String productIdEncrypt;
	
	private String startDateText;
	
	private String endDateText;
	
	private String measureName;
	
	private String currencyName;

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public String getSaleIdEncrypt() {
		return saleIdEncrypt;
	}

	public void setSaleIdEncrypt(String saleIdEncrypt) {
		this.saleIdEncrypt = saleIdEncrypt;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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
		return "SaleForm [saleId=" + saleId + ", saleIdEncrypt=" + saleIdEncrypt + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", price=" + price + ", measureIdEncrypt=" + measureIdEncrypt
				+ ", currencyId=" + currencyId + ", productIdEncrypt=" + productIdEncrypt + ", startDateText="
				+ startDateText + ", endDateText=" + endDateText + ", measureName=" + measureName + ", currencyName="
				+ currencyName + "]";
	}
	
}
