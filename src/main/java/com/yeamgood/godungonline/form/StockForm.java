package com.yeamgood.godungonline.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class StockForm {


	private Long stockId;
	
	private String stockIdEncrypt;
	
	private String productIdEncrypt;

	@Length(max = 16, message = "{currency.max.16}")
	private String remindNumber;
	
	@NotBlank(message = "{form.stock.validation.warehouse}")
	private String warehouseIdEncrypt;
	
	private String locationIdEncrypt;
	
	// Grid
	private String warehouseName;
	
	private String locationCode;
	
	private String remainNumber;

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public String getStockIdEncrypt() {
		return stockIdEncrypt;
	}

	public void setStockIdEncrypt(String stockIdEncrypt) {
		this.stockIdEncrypt = stockIdEncrypt;
	}

	public String getProductIdEncrypt() {
		return productIdEncrypt;
	}

	public void setProductIdEncrypt(String productIdEncrypt) {
		this.productIdEncrypt = productIdEncrypt;
	}

	public String getRemindNumber() {
		return remindNumber;
	}

	public void setRemindNumber(String remindNumber) {
		this.remindNumber = remindNumber;
	}

	public String getWarehouseIdEncrypt() {
		return warehouseIdEncrypt;
	}

	public void setWarehouseIdEncrypt(String warehouseIdEncrypt) {
		this.warehouseIdEncrypt = warehouseIdEncrypt;
	}

	public String getLocationIdEncrypt() {
		return locationIdEncrypt;
	}

	public void setLocationIdEncrypt(String locationIdEncrypt) {
		this.locationIdEncrypt = locationIdEncrypt;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getRemainNumber() {
		return remainNumber;
	}

	public void setRemainNumber(String remainNumber) {
		this.remainNumber = remainNumber;
	}

	@Override
	public String toString() {
		return "StockForm [stockId=" + stockId + ", stockIdEncrypt=" + stockIdEncrypt + ", productIdEncrypt="
				+ productIdEncrypt + ", remindNumber=" + remindNumber + ", warehouseIdEncrypt=" + warehouseIdEncrypt
				+ ", locationIdEncrypt=" + locationIdEncrypt + ", warehouseName=" + warehouseName + ", locationCode="
				+ locationCode + ", remainNumber=" + remainNumber + "]";
	}

	
}
