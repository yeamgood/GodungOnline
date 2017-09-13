package com.yeamgood.godungonline.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yeamgood.godungonline.model.template.ModelTemplate;

@Entity
@Table(name = "category")
public class Category extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "category_Id")
	private Long categoryId;
	
	@Transient
	private String categoryIdEncrypt;
	
	@Column(name = "category_code")
	private String categoryCode;
	
	@Column(name = "category_name")
	@NotEmpty(message = "{form.category.valid.name}")
	@Length(max = 100, message = "{validation.max.lenght}")
	private String categoryName;
	
	@Column(name = "description")
	@Length(max = 200, message = "{validation.max.lenght}")
	private String description;
	
	@ManyToOne()
	@JoinColumn(name = "godung_id")
	@JsonIgnore
	private Godung godung;
	
	@Transient
	@JsonIgnore
	List<Long> catogoryBranchList;
	
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

	public List<Long> getCatogoryBranchList() {
		return catogoryBranchList;
	}

	public void setCatogoryBranchList(List<Long> catogoryBranchList) {
		this.catogoryBranchList = catogoryBranchList;
	}

	public String getCategoryIdEncrypt() {
		return categoryIdEncrypt;
	}

	public void setCategoryIdEncrypt(String categoryIdEncrypt) {
		this.categoryIdEncrypt = categoryIdEncrypt;
	}

	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", categoryIdEncrypt=" + categoryIdEncrypt + ", categoryCode="
				+ categoryCode + ", categoryName=" + categoryName + ", description=" + description + ", godung="
				+ godung + ", catogoryBranchList=" + catogoryBranchList + "]";
	}
	
}
