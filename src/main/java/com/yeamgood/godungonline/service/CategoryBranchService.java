package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.model.CategoryBranch;

public interface CategoryBranchService {
	List<CategoryBranch> findAllByCategoryId(Long categoryId);
}