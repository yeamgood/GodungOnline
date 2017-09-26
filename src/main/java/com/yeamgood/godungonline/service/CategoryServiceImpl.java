package com.yeamgood.godungonline.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Category;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.CategoryRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private GodungService godungService;

	@Override
	public Category findByIdEncrypt(String idEncrypt)  {
		logger.debug("I:");
		Category category = categoryRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		category.encryptData();
		logger.debug("O:");
		return category;
	}
	
	@Override
	public Category findByIdEncrypt(String idEncrypt, User userSession) throws GodungIdException  {
		logger.debug("I:");
		Category category = categoryRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		godungService.checkGodungId(category.getGodung().getGodungId(), userSession);
		category.encryptData();
		logger.debug("O:");
		return category;
	}

	@Override
	public List<Category> findAllOrderByCategoryNameAsc()  {
		logger.debug("I:");
		List<Category> categoryList = categoryRepository.findAll(sortByCategoryNameAsc());
		for (Category category : categoryList) {
			category.setCategoryIdEncrypt(AESencrpUtils.encryptLong(category.getCategoryId()));
			category.encryptData();
		}
		logger.debug("O:");
		return categoryList;
	}

	@Override
	public List<Category> findAllByGodungGodungIdOrderByCategoryNameAsc(Long godungId)  {
		logger.debug(Constants.LOG_INPUT, godungId);
		List<Category> categoryList = categoryRepository.findAllByGodungGodungIdOrderByCategoryNameAsc(godungId);
		for (Category category : categoryList) {
			category.setCategoryIdEncrypt(AESencrpUtils.encryptLong(category.getCategoryId()));
			category.encryptData();
		}
		logger.debug("O:");
		return categoryList;
	}
	
	private Sort sortByCategoryNameAsc() {
        return new Sort(Sort.Direction.ASC, "categoryName");
    }

	@Override
	public long count(Long godungId) {
		logger.debug(Constants.LOG_INPUT, godungId);
		logger.debug("O:");
		return categoryRepository.countByGodungGodungId(godungId);
	}

	@Override
	public List<Category> findByGodungGodungIdAndCategoryNameIgnoreCaseContaining(Long godungId, String categoryName, Pageable pageable)  {
		logger.debug("I:");
		List<Category> categoryList = categoryRepository.findByGodungGodungIdAndCategoryNameIgnoreCaseContaining(godungId, categoryName, pageable);
		for (Category category : categoryList) {
			category.setCategoryIdEncrypt(AESencrpUtils.encryptLong(category.getCategoryId()));
			category.encryptData();
		}
		logger.debug("O:");
		return categoryList;
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Category category,User user)  {
		logger.debug("I:");
		if(StringUtils.isBlank(category.getCategoryIdEncrypt())) {
			Category maxCategory = categoryRepository.findTopByGodungGodungIdOrderByCategoryCodeDesc(user.getGodung().getGodungId());
			if(maxCategory == null) {
				logger.debug("I:Null Max Data");
				maxCategory = new Category();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_CATEGORY, maxCategory.getCategoryCode());
			category.setGodung(user.getGodung());
			category.setCreate(user);
			category.setCategoryCode(generateCode);
			category.setGodung(user.getGodung());
			categoryRepository.save(category);
		}else {
			Category categoryTemp = categoryRepository.findOne(AESencrpUtils.decryptLong(category.getCategoryIdEncrypt()));
			categoryTemp.setCategoryName(category.getCategoryName());
			categoryTemp.setDescription(category.getDescription());
			categoryTemp.setUpdate(user);
			categoryRepository.save(categoryTemp);
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(Category category, User user) throws GodungIdException {
		logger.debug("I:");
		Category categoryTemp = categoryRepository.findOne(AESencrpUtils.decryptLong(category.getCategoryIdEncrypt()));
		godungService.checkGodungId(categoryTemp.getGodung().getGodungId(), user);
		categoryRepository.delete(categoryTemp);
		logger.debug("O:");
	}
	
}