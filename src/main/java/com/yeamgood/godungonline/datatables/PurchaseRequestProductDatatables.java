package com.yeamgood.godungonline.datatables;

public class PurchaseRequestProductDatatables{
	
	private String purchaseRequestIdEncrypt;

	private String purchaseRequestProductIdEncrypt;
	
	private String productCode;
	
	private String productName;

	private String amount;

	private String measureName;
	
	private String price;
	
	private String totalPrice;
	
	private String description;

	public String getPurchaseRequestIdEncrypt() {
		return purchaseRequestIdEncrypt;
	}

	public void setPurchaseRequestIdEncrypt(String purchaseRequestIdEncrypt) {
		this.purchaseRequestIdEncrypt = purchaseRequestIdEncrypt;
	}

	public String getPurchaseRequestProductIdEncrypt() {
		return purchaseRequestProductIdEncrypt;
	}

	public void setPurchaseRequestProductIdEncrypt(String purchaseRequestProductIdEncrypt) {
		this.purchaseRequestProductIdEncrypt = purchaseRequestProductIdEncrypt;
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
		return "PurchaseRequestProductDatatables [purchaseRequestIdEncrypt=" + purchaseRequestIdEncrypt
				+ ", purchaseRequestProductIdEncrypt=" + purchaseRequestProductIdEncrypt + ", productCode="
				+ productCode + ", productName=" + productName + ", amount=" + amount + ", measureName=" + measureName
				+ ", price=" + price + ", totalPrice=" + totalPrice + ", description=" + description + "]";
	}
	
}
