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

import org.springframework.format.annotation.DateTimeFormat;

import com.yeamgood.godungonline.model.template.ModelTemplate;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "price")
public class Price extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "price_id")
	private Long priceId;
	
	@Transient
	private String priceIdEncrypt;

	@Column(name = "start_date")
	@NotNull(message = "{form.price.validation.startdate}")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date startDate;

	@Column(name = "end_date")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date endDate;
	
	@Column(name = "price")
	private BigDecimal price;
	
	
	@ManyToOne()
	@JoinColumn(name = "measure_id")
	private Measure measure;
	
	
	@ManyToOne()
	@JoinColumn(name = "currency_id")
	private Currency currency;
	
	
	
	public void setObject(Price price) {
		this.startDate = price.getStartDate();
		this.endDate = price.getEndDate();
	}
	
	public void encryptData(Price price) throws Exception {
		this.priceIdEncrypt = AESencrpUtils.encryptLong(price.getPriceId());
		this.priceId = null;
	}

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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "Price [priceId=" + priceId + ", priceIdEncrypt=" + priceIdEncrypt + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", price=" + price + ", measure=" + measure + ", currency=" + currency + "]";
	}
	
}
