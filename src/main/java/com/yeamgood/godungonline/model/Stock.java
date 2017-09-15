package com.yeamgood.godungonline.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.yeamgood.godungonline.model.template.ModelTemplate;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "stock")
public class Stock extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "stock")
	private Long stockId;
	
	@Transient
	private String stockIdEncrypt;
	
	@Transient
	private String productIdEncrypt;
	
	@ManyToOne()
	@JoinColumn(name = "warehouse_id",nullable = true)
	private Warehouse warehouse;
	
	@ManyToOne()
	@JoinColumn(name = "location_id",nullable = true)
	private Location location;
	
	@Column(name = "remind_number")
	private BigDecimal remindNumber;
	
	public void encryptData(Stock stock) throws Exception {
		this.stockIdEncrypt = AESencrpUtils.encryptLong(stock.getStockId());
		this.stockId = null;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public BigDecimal getRemindNumber() {
		return remindNumber;
	}

	public void setRemindNumber(BigDecimal remindNumber) {
		this.remindNumber = remindNumber;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
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
	
}
