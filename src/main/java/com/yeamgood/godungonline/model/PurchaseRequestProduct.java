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
@Table(name = "purchaserequestproduct")
public class PurchaseRequestProduct extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "purchaserequestproduct_id")
	private Long purchaseRequestProductId;
	
	@Transient
	private String purchaseRequestProductIdEncrypt;
	
	@ManyToOne()
	@JoinColumn(name = "product_id")
	private Product product;
	
	@Column(name = "amount")
	private BigDecimal amount;
	
	@ManyToOne()
	@JoinColumn(name = "measure_id")
	private Measure measure;
	
	@Column(name = "price")
	private BigDecimal price;
	
	@Column(name = "description")
	private String description;
	
	
	public void encryptData() {
		this.purchaseRequestProductIdEncrypt = AESencrpUtils.encryptLong(this.purchaseRequestProductId);
		this.purchaseRequestProductId = null;
	}

	public Long getPurchaseRequestProductId() {
		return purchaseRequestProductId;
	}

	public void setPurchaseRequestProductId(Long purchaseRequestProductId) {
		this.purchaseRequestProductId = purchaseRequestProductId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPurchaseRequestProductIdEncrypt() {
		return purchaseRequestProductIdEncrypt;
	}

	public void setPurchaseRequestProductIdEncrypt(String purchaseRequestProductIdEncrypt) {
		this.purchaseRequestProductIdEncrypt = purchaseRequestProductIdEncrypt;
	}

	@Override
	public String toString() {
		return "PurchaseRequestProduct [purchaseRequestProductId=" + purchaseRequestProductId
				+ ", purchaseRequestProductIdEncrypt=" + purchaseRequestProductIdEncrypt + ", product=" + product
				+ ", amount=" + amount + ", measure=" + measure + ", price=" + price + ", description=" + description
				+ "]";
	}
	
}
