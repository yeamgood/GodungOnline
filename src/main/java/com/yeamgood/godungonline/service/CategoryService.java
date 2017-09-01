package com.yeamgood.godungonline.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Category;
import com.yeamgood.godungonline.model.User;

public interface CategoryService {
	public Category findById(Long id);
	public Category findById(Long id,User user) throws GodungIdException;
	public List<Category> findAllOrderByCategoryNameAsc();
	public List<Category> findAllByGodungGodungIdOrderByCategoryCodeAsc(Long godungId);
	public List<Category> findByGodungGodungIdAndCategoryNameIgnoreCaseContaining(Long godungId, String category, Pageable pageable);
	public long count(Long godungId);
	public void save(Category category,User user);
	public void delete(Category category,User user) throws GodungIdException;
}