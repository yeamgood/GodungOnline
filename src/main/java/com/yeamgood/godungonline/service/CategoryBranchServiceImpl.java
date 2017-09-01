package com.yeamgood.godungonline.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.model.CategoryBranch;
import com.yeamgood.godungonline.repository.CategoryBranchRepository;

@Service("categoryBranchService")
public class CategoryBranchServiceImpl implements CategoryBranchService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CategoryBranchRepository categoryBranchRepository;

	@Override
	public List<CategoryBranch> findAllByCategoryId(Long categoryId) {
		logger.debug("I:");
		logger.debug("O:");
		return categoryBranchRepository.findAllByCategoryId(categoryId);
	}
	
	
}