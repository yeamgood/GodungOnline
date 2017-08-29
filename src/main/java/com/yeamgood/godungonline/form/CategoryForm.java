package com.yeamgood.godungonline.form;

import com.yeamgood.godungonline.model.Godung;
import com.yeamgood.godungonline.model.Menu;

public class CategoryForm {
	
	private Long categoryId;
	
	private String categoryName;
	
	private String description;
	
	private Godung godung;
	
	private Menu menu;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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
	
}
