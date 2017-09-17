package com.yeamgood.godungonline.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.yeamgood.godungonline.model.template.ModelTemplate;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "dealer")
public class Dealer extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "dealer_id")
	private Long dealerId;
	
	@Column(name = "price")
	@NotNull(message = "{form.dealer.validation.price}")
	private BigDecimal price;
	
	@Column(name = "start_date")
	@NotNull(message = "{form.dealer.validation.startdate}")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;
	
	@ManyToOne()
	@JoinColumn(name = "supplier_id")
	@NotNull(message = "{form.dealer.validation.supplier}")
	private Supplier supplier;
	
	@ManyToOne()
	@JoinColumn(name = "measure_id")
	@NotNull(message = "{form.dealer.validation.measure}")
	private Measure measure;
	
	@ManyToOne()
	@JoinColumn(name = "currency_id")
	private Currency currency;
	
	@Transient
	private String dealerIdEncrypt;
	
	@Transient
	private String productIdEncrypt;
	
	
	public void encryptData(Dealer dealer) throws Exception {
		this.dealerIdEncrypt = AESencrpUtils.encryptLong(dealer.getDealerId());
		this.dealerId = null;
	}

	public Long getDealerId() {
		return dealerId;
	}


	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}


	public BigDecimal getPrice() {
		return price;
	}


	public void setPrice(BigDecimal price) {
		this.price = price;
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

	public Supplier getSupplier() {
		return supplier;
	}


	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}


	public Measure getMeasure() {
		return measure;
	}


	public void setMeasure(Measure measure) {
		this.measure = measure;
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

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "Dealer [dealerId=" + dealerId + ", price=" + price + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", supplier=" + supplier + ", measure=" + measure
				+ ", currency=" + currency + ", dealerIdEncrypt=" + dealerIdEncrypt + ", productIdEncrypt="
				+ productIdEncrypt + "]";
	}
	
}
