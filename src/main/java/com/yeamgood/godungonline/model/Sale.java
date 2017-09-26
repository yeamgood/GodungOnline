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
@Table(name = "sale")
public class Sale extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "sale_id")
	private Long saleId;
	
	@Transient
	private String saleIdEncrypt;

	@Column(name = "start_date")
	@NotNull(message = "{form.sale.validation.startdate}")
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
	
	public void setObject(Sale sale) {
		this.startDate = sale.getStartDate();
		this.endDate = sale.getEndDate();
	}
	
	public void encryptData() {
		this.saleIdEncrypt = AESencrpUtils.encryptLong(this.saleId);
		this.saleId = null;
	}

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
		return "Sale [saleId=" + saleId + ", saleIdEncrypt=" + saleIdEncrypt + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", price=" + price + ", measure=" + measure + ", currency=" + currency + "]";
	}
	
}
