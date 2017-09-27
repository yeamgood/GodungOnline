package com.yeamgood.godungonline.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class PurchaseRequestProductForm{
	
	private String purchaseRequestIdEncrypt;

	private String purchaseRequestProductIdEncrypt;
	
	@NotBlank(message = "{form.purchaseRequestProduct.valid.product}")
	private String productIdEncrypt;
	
	private String productName;

	@NotBlank(message = "{form.purchaseRequestProduct.valid.amount}")
	@Length(max = 16, message = "{currency.max.16}")
	private String amount;

	@NotBlank(message = "{form.purchaseRequestProduct.valid.measure}")
	private String measureIdEncrypt;
	
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public String toString() {
		return "PurchaseRequestProductForm [purchaseRequestIdEncrypt=" + purchaseRequestIdEncrypt
				+ ", purchaseRequestProductIdEncrypt=" + purchaseRequestProductIdEncrypt + ", productIdEncrypt="
				+ productIdEncrypt + ", productName=" + productName + ", amount=" + amount + ", measureIdEncrypt="
				+ measureIdEncrypt + ", price=" + price + ", totalPrice=" + totalPrice + ", description=" + description
				+ "]";
	}
	
}
