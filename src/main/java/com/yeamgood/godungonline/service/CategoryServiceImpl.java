package com.yeamgood.godungonline.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Category;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.CategoryRepository;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CategoryRepository categoryRepository;

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
	public List<Category> findAllByGodungGodungIdOrderByCategoryNameAsc(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return categoryRepository.findAllByGodungGodungIdOrderByCategoryNameAsc(godungId);
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
		}else {
			Category categoryTemp = categoryRepository.findOne(category.getCategoryId());
			categoryTemp.setCategoryName(category.getCategoryName());
			categoryTemp.setDescription(category.getDescription());
			categoryTemp.setUpdate(user.getEmail(), new Date());
			categoryRepository.save(categoryTemp);
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
		
		if(godungIdTemp ==  godungIdSession) {
			categoryRepository.delete(categoryTemp);
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