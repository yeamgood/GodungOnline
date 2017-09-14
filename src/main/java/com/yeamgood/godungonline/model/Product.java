package com.yeamgood.godungonline.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yeamgood.godungonline.model.template.ModelTemplate;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "product")
public class Product extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "product_Id")
	private Long productId;
	
	@Transient
	private String productIdEncrypt;
	
	@Column(name = "product_code")
	private String productCode;
	
	@Column(name = "product_name")
	@NotEmpty(message = "{form.product.valid.name}")
	@Length(max = 100, message = "{validation.max.lenght}")
	private String productName;
	
	@Column(name = "description")
	@Length(max = 200, message = "{validation.max.lenght}")
	private String description;
	
	@ManyToOne()
	@JoinColumn(name = "godung_id")
	@JsonIgnore
	private Godung godung;
	
	@ManyToOne()
	@JoinColumn(name = "brand_id",nullable = true)
	private Brand brand;
	
	@ManyToOne()
	@JoinColumn(name = "measure_id",nullable = true)
	private Measure measure;
	
	@ManyToOne()
	@JoinColumn(name = "category_id",nullable = true)
	private Category category;
	
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval=true)
	@JoinTable(name = "product_price", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "price_id"))
	private List<Price> priceList;
	
	public void setObject(Product product) {
		this.productName = product.getProductName();
		this.description  = product.getDescription();
	}
	
	public void encryptData(Product product) throws Exception {
		this.productIdEncrypt = AESencrpUtils.encryptLong(product.getProductId());
		this.productId = null;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Godung getGodung() {
		return godung;
	}

	public void setGodung(Godung godung) {
		this.godung = godung;
	}

	public String getProductIdEncrypt() {
		return productIdEncrypt;
	}

	public void setProductIdEncrypt(String productIdEncrypt) {
		this.productIdEncrypt = productIdEncrypt;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Price> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<Price> priceList) {
		this.priceList = priceList;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productIdEncrypt=" + productIdEncrypt + ", productCode="
				+ productCode + ", productName=" + productName + ", description=" + description + ", godung=" + godung
				+ ", brand=" + brand + ", measure=" + measure + ", category=" + category + ", priceList=" + priceList
				+ "]";
	}

}
