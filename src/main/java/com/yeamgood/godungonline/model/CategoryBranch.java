package com.yeamgood.godungonline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.yeamgood.godungonline.model.template.ModelTemplate;

@Entity
@Table(name = "category_branch")
public class CategoryBranch extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "category_branch_Id")
	private Long categoryBranchId;
	
	@Column(name = "category_id")
	private Long categoryId;
	
	@Column(name = "category_ref_id")
	private Long categoryRefId;
	
	@Column(name = "type")
	private String type;

	public Long getCategoryBranchId() {
		return categoryBranchId;
	}

	public void setCategoryBranchId(Long categoryBranchId) {
		this.categoryBranchId = categoryBranchId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getCategoryRefId() {
		return categoryRefId;
	}

	public void setCategoryRefId(Long categoryRefId) {
		this.categoryRefId = categoryRefId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "CategoryBranch [categoryBranchId=" + categoryBranchId + ", categoryId=" + categoryId
				+ ", categoryRefId=" + categoryRefId + ", type=" + type + "]";
	}
	
}
