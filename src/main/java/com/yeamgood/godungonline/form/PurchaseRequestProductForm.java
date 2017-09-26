package com.yeamgood.godungonline.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class PurchaseRequestProductForm{

	private String purchaseRequestProductIdEncrypt;
	
	@NotBlank(message = "{form.purchaseRequestProduct.valid.product}")
	private String productIdEncrypt;
	
	private String productCode;
	
	private String productName;
	
	@NotBlank(message = "{form.purchaseRequestProduct.valid.amount}")
	@Length(max = 16, message = "{currency.max.16}")
	private String amount;

	@NotBlank(message = "{form.purchaseRequestProduct.valid.measure}")
	private String measureIdEncrypt;
	
	private String measureName;
	
	@NotBlank(message = "{form.purchaseRequestProduct.valid.price}")
	@Length(max = 16, message = "{currency.max.16}")
	private String price;
	
	private String totalPrice;

	@Length(max = 200, message = "{validation.max.lenght}")
	private String description;

	public String getPurchaseRequestProductIdEncrypt() {
		return purchaseRequestProductIdEncrypt;
	}

	public void setPurchaseRequestProductIdEncrypt(String purchaseRequestProductIdEncrypt) {
		this.purchaseRequestProductIdEncrypt = purchaseRequestProductIdEncrypt;
	}

	public String getProductIdEncrypt() {
		return productIdEncrypt;
	}

	public void setProductIdEncrypt(String productIdEncrypt) {
		this.productIdEncrypt = productIdEncrypt;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMeasureIdEncrypt() {
		return measureIdEncrypt;
	}

	public void setMeasureIdEncrypt(String measureIdEncrypt) {
		this.measureIdEncrypt = measureIdEncrypt;
	}

	public String getMeasureName() {
		return measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "PurchaseRequestProductForm [purchaseRequestProductIdEncrypt=" + purchaseRequestProductIdEncrypt
				+ ", productIdEncrypt=" + productIdEncrypt + ", productCode=" + productCode + ", productName="
				+ productName + ", amount=" + amount + ", measureIdEncrypt=" + measureIdEncrypt + ", measureName="
				+ measureName + ", price=" + price + ", totalPrice=" + totalPrice + ", description=" + description
				+ "]";
	}
	
}
