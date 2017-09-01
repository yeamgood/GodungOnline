package com.yeamgood.godungonline.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.bean.CategoryBranchType;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Category;
import com.yeamgood.godungonline.model.CategoryBranch;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.CategoryBranchRepository;
import com.yeamgood.godungonline.repository.CategoryRepository;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CategoryBranchRepository categoryBranchRepository;

	@Override
	public Category findById(Long id) {
		logger.debug("I:");
		logger.debug("O:");
		return categoryRepository.findOne(id);
	}

	@Override
	public List<Category> findAllOrderByCategoryNameAsc() {
		logger.debug("I:");
		logger.debug("O:");
		return categoryRepository.findAll(sortByCategoryNameAsc());
	}

	@Override
	public List<Category> findAllByGodungGodungIdOrderByCategoryCodeAsc(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return categoryRepository.findAllByGodungGodungIdOrderByCategoryCodeAsc(godungId);
	}
	
	private Sort sortByCategoryNameAsc() {
        return new Sort(Sort.Direction.ASC, "categoryName");
    }

	@Override
	public long count(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return categoryRepository.countByGodungGodungId(godungId);
	}

	@Override
	public List<Category> findByGodungGodungIdAndCategoryNameIgnoreCaseContaining(Long godungId, String categoryName, Pageable pageable) {
		return categoryRepository.findByGodungGodungIdAndCategoryNameIgnoreCaseContaining(godungId, categoryName, pageable);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Category category,User user) {
		logger.debug("I:");
		if(category.getCategoryId() == null) {
			Category maxCategory = categoryRepository.findTopByGodungGodungIdOrderByCategoryCodeDesc(user.getGodung().getGodungId());
			if(maxCategory == null) {
				logger.debug("I:Null Max Data");
				maxCategory = new Category();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_CATEGORY, maxCategory.getCategoryCode());
			
			category.setGodung(user.getGodung());
			category.setCreate(user.getEmail(), new Date());
			category.setCategoryCode(generateCode);
			category.setGodung(user.getGodung());
			categoryRepository.save(category);
			
			if(category.getCatogoryBranchList() != null) {
				CategoryBranch categoryBranch;
				List<CategoryBranch> categoryBranchList = new ArrayList<CategoryBranch>();
				for (Long categoryId : category.getCatogoryBranchList()) {
					categoryBranch = new CategoryBranch();
					categoryBranch.setCategoryId(category.getCategoryId());
					categoryBranch.setCategoryRefId(categoryId);
					categoryBranch.setType("CHILD");
					categoryBranch.setCreate(user.getEmail(), new Date());
					categoryBranchList.add(categoryBranch);
				}
				categoryBranchRepository.save(categoryBranchList);
			}
			
		}else {
			Category categoryTemp = categoryRepository.findOne(category.getCategoryId());
			categoryTemp.setCategoryName(category.getCategoryName());
			categoryTemp.setDescription(category.getDescription());
			categoryTemp.setUpdate(user.getEmail(), new Date());
			categoryRepository.save(categoryTemp);
			
			if(category.getCatogoryBranchList() != null) {
				CategoryBranch categoryBranch;
				List<CategoryBranch> categoryBranchList = new ArrayList<CategoryBranch>();
				for (Long categoryId : category.getCatogoryBranchList()) {
					categoryBranch = new CategoryBranch();
					categoryBranch.setCategoryId(categoryTemp.getCategoryId());
					categoryBranch.setCategoryRefId(categoryId);
					categoryBranch.setType(CategoryBranchType.CHILD.toString());
					categoryBranchList.add(categoryBranch);
				}
				List<CategoryBranch> categoryBranchDeleteList = categoryBranchRepository.findAllByCategoryId(categoryTemp.getCategoryId());
				categoryBranchRepository.delete(categoryBranchDeleteList);
				categoryBranchRepository.save(categoryBranchList);
			}
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(Category category, User user) throws GodungIdException {
		logger.debug("I:");
		Category categoryTemp = categoryRepository.findOne(category.getCategoryId());
		long godungIdTemp = categoryTemp.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		List<CategoryBranch> categoryBranchDeleteList;
		
		if(godungIdTemp ==  godungIdSession) {
			categoryRepository.delete(categoryTemp);
			categoryBranchDeleteList = categoryBranchRepository.findAllByCategoryId(categoryTemp.getCategoryId());
			categoryBranchRepository.delete(categoryBranchDeleteList);
			
			categoryBranchDeleteList = categoryBranchRepository.findAllByCategoryRefId(categoryTemp.getCategoryId());
			categoryBranchRepository.delete(categoryBranchDeleteList);
		}else {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		logger.debug("O:");
	}

	@Override
	public Category findById(Long id, User user) throws GodungIdException {
		logger.debug("I:");
		Category category = categoryRepository.findOne(id);
		long godungIdTemp = category.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		
		logger.debug("O:");
		return category;
	}
}