package com.yeamgood.godungonline.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Category;
import com.yeamgood.godungonline.model.User;

public interface CategoryService {
	public Category findByIdEncrypt(String idEncrypt) throws Exception;
	public Category findByIdEncrypt(String idEncrypt,User user) throws Exception;
	public List<Category> findAllOrderByCategoryNameAsc() throws Exception;
	public List<Category> findAllByGodungGodungIdOrderByCategoryNameAsc(Long godungId) throws Exception;
	public List<Category> findByGodungGodungIdAndCategoryNameIgnoreCaseContaining(Long godungId, String category, Pageable pageable) throws Exception;
	public long count(Long godungId);
	public void save(Category category,User user) throws Exception;
	public void delete(Category category,User user) throws GodungIdException, Exception;
}