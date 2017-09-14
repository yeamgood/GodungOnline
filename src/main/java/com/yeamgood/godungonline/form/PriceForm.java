package com.yeamgood.godungonline.form;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.yeamgood.godungonline.model.template.ModelTemplate;

@Entity
@Table(name = "price")
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
	private String currencyId;
	
	private String productIdEncrypt;

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

	public String getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}

	public String getProductIdEncrypt() {
		return productIdEncrypt;
	}

	public void setProductIdEncrypt(String productIdEncrypt) {
		this.productIdEncrypt = productIdEncrypt;
	}

	@Override
	public String toString() {
		return "PriceForm [priceId=" + priceId + ", priceIdEncrypt=" + priceIdEncrypt + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", priceText=" + priceText + ", measureIdEncrypt=" + measureIdEncrypt
				+ ", currencyId=" + currencyId + ", productIdEncrypt=" + productIdEncrypt + "]";
	}
	
}
