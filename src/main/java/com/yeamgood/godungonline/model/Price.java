package com.yeamgood.godungonline.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import com.yeamgood.godungonline.model.template.ModelTemplate;

@Entity
@Table(name = "price")
public class Price extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "price_id")
	private Long priceId;
	
	@Column(name = "price_type")
	@NotEmpty(message = "{form.price.validation.currencytype}")
	private String priceType;

	@Column(name = "start_date")
	@NotNull(message = "{form.price.validation.startdate}")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date startDate;

	@Column(name = "end_date")
	@NotNull(message = "{form.price.validation.enddate}")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date endDate;
	
	@Column(name = "price")
	@NotNull(message = "{form.price.validation.price}")
	private BigDecimal price;
	
	@Transient
	@Column(name = "measure_id")
	@NotEmpty(message = "{form.price.validation.measure}")
	private String measureId;
	
	@OneToOne(targetEntity = Measure.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "measure_id")
    private Measure measure;

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public Long getPriceId() {
		return priceId;
	}

	public void setPriceId(Long priceId) {
		this.priceId = priceId;
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

	public String getMeasureId() {
		return measureId;
	}

	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	@Override
	public String toString() {
		return "Price [priceId=" + priceId + ", priceType=" + priceType + ", startDate=" + startDate + ", endDate="
				+ endDate + ", price=" + price + ", measureId=" + measureId + ", measure=" + measure + "]";
	}
	
}
