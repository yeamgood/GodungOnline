package com.yeamgood.godungonline.form;

import java.util.List;

import com.yeamgood.godungonline.model.Category;
import com.yeamgood.godungonline.model.Godung;
import com.yeamgood.godungonline.model.Menu;

public class CategoryForm {
	
	private Long categoryId;
	
	private String categoryCode;
	
	private String categoryName;
	
	private String description;
	
	private Godung godung;
	
	private Menu menu;
	
	private List<Category> categoryDataList;
	
	private List<Category> catogoryBranchShowList;
	private List<Long> catogoryBranchList;
	
	
	public void mapObjectToForm(Category category) {
		this.categoryId = category.getCategoryId();
		this.categoryCode = category.getCategoryCode();
		this.categoryName = category.getCategoryName();
		this.description = category.getDescription();
	}


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


	public Menu getMenu() {
		return menu;
	}


	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public List<Category> getCategoryDataList() {
		return categoryDataList;
	}


	public void setCategoryDataList(List<Category> categoryDataList) {
		this.categoryDataList = categoryDataList;
	}

	public List<Long> getCatogoryBranchList() {
		return catogoryBranchList;
	}

	public void setCatogoryBranchList(List<Long> catogoryBranchList) {
		this.catogoryBranchList = catogoryBranchList;
	}


	public List<Category> getCatogoryBranchShowList() {
		return catogoryBranchShowList;
	}


	public void setCatogoryBranchShowList(List<Category> catogoryBranchShowList) {
		this.catogoryBranchShowList = catogoryBranchShowList;
	}
	
}
