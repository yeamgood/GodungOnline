package com.yeamgood.godungonline.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yeamgood.godungonline.model.Category;
import com.yeamgood.godungonline.model.User;

public interface CategoryService {
	public Category findById(long id);
	public List<Category> findAllOrderByCategoryNameAsc();
	public List<Category> findAllByGodungGodungIdOrderByCategoryNameAsc(Long godungId);
	public List<Category> findByGodungGodungIdAndCategoryNameIgnoreCaseContaining(Long godungId, String category, Pageable pageable);
	public long count(Long godungId);
	public void save(Category category,User user);
}