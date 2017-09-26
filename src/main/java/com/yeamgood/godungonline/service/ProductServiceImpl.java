package com.yeamgood.godungonline.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Brand;
import com.yeamgood.godungonline.model.Category;
import com.yeamgood.godungonline.model.Dealer;
import com.yeamgood.godungonline.model.Measure;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.BrandRepository;
import com.yeamgood.godungonline.repository.CategoryRepository;
import com.yeamgood.godungonline.repository.MeasureRepository;
import com.yeamgood.godungonline.repository.ProductRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;
import com.yeamgood.godungonline.utils.NumberUtils;

@Service("productService")
public class ProductServiceImpl implements ProductService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private MeasureRepository measureRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private GodungService godungService;
	
	@Override
	public Product findByIdEncrypt(String idEncrypt,User userSession) throws GodungIdException  {
		logger.debug("I:");
		logger.debug("O:");
		Product productTemp = productRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		godungService.checkGodungId(productTemp.getGodung().getGodungId(), userSession);
		productTemp.encryptData();
		return productTemp;
	}

	@Override
	public List<Product> findAllByGodungGodungIdOrderByProductNameAsc(Long godungId)  {
		logger.debug(Constants.LOG_INPUT, godungId);
		logger.debug("O:");

		List<Product> productList = productRepository.findAllByGodungGodungIdOrderByProductCodeAsc(godungId);
		for (Product product : productList) {
			product.encryptData();
			if(product.getBrand() == null) {product.setBrand(new Brand());}
			if(product.getCategory() == null) {product.setCategory(new Category());}
			if(product.getMeasure() == null) {product.setMeasure(new Measure());}
			
			product.getBrand().setBrandIdEncrypt(AESencrpUtils.encryptLong(product.getBrand().getBrandId()));
			product.getMeasure().setMeasureIdEncrypt(AESencrpUtils.encryptLong(product.getMeasure().getMeasureId()));
			product.getCategory().setCategoryIdEncrypt(AESencrpUtils.encryptLong(product.getCategory().getCategoryId()));
			
			product.setPrice(getDealerCurrent(product.getDealerList()));
			
		}
		return productList;
	}
	
	public String getDealerCurrent(List<Dealer> dealerList) {
	    String price = NumberUtils.EMPLY_DATA;
		Date todayDate = new Date();
		
		Collections.sort(dealerList, (o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()));
		
		for (Dealer dealerTemp : dealerList) {
			if(todayDate.after(dealerTemp.getStartDate())) {
				if(dealerTemp.getEndDate() != null) {
					if(todayDate.before(dealerTemp.getEndDate())) {
						price = NumberUtils.bigDecimalToString(dealerTemp.getPrice());
					}
				}else {
					price = NumberUtils.bigDecimalToString(dealerTemp.getPrice());
				}
			}
		}
		return price;
	}

	@Override
	public long count(Long godungId) {
		logger.debug(Constants.LOG_INPUT, godungId);
		logger.debug("O:");
		return productRepository.countByGodungGodungId(godungId);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Product product,User userSession)  {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, product);
		Product productTemp;
		
		if(StringUtils.isBlank(product.getProductIdEncrypt())) {
			productTemp = new Product();
			productTemp.setProductCode(generateCode(userSession));
			productTemp.setGodung(userSession.getGodung());
			productTemp.setCreate(userSession);
		}else {
			productTemp = productRepository.findOne(AESencrpUtils.decryptLong(product.getProductIdEncrypt()));
			productTemp.setUpdate(userSession);
		}
		
		Brand brand = null;
		if(!StringUtils.isBlank(product.getBrand().getBrandIdEncrypt())) {
			brand = brandRepository.findOne(AESencrpUtils.decryptLong(product.getBrand().getBrandIdEncrypt()));
		}
		
		Measure measure = null;
		if(!StringUtils.isBlank(product.getMeasure().getMeasureIdEncrypt())) {
			measure = measureRepository.findOne(AESencrpUtils.decryptLong(product.getMeasure().getMeasureIdEncrypt()));
		}
		
		Category category = null;
		if(!StringUtils.isBlank(product.getCategory().getCategoryIdEncrypt())) {
			category = categoryRepository.findOne(AESencrpUtils.decryptLong(product.getCategory().getCategoryIdEncrypt()));
		}
		
		productTemp.setBrand(brand);
		productTemp.setMeasure(measure);
		productTemp.setCategory(category);
		productTemp.setObject(product);
		productRepository.save(productTemp);
		product.setProductIdEncrypt(AESencrpUtils.encryptLong(productTemp.getProductId()));
		
		logger.debug("O:");
	}
	
	public String generateCode(User userSession) {
		logger.debug("I");
		Product maxProduct = productRepository.findTopByGodungGodungIdOrderByProductCodeDesc(userSession.getGodung().getGodungId());
		if(maxProduct == null) {
			logger.debug("I:Null Max Data");
			maxProduct = new Product();
		}
		String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_PRODUCT , maxProduct.getProductCode());
		logger.debug(Constants.LOG_INPUT, generateCode);
		logger.debug("O");
		return generateCode;
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String idEncrypt, User userSession) throws GodungIdException{
		logger.debug("I:");
		Product productTemp = productRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		godungService.checkGodungId(productTemp.getGodung().getGodungId(), userSession);
		godungService.checkGodungId(productTemp.getGodung().getGodungId(), userSession);
		productRepository.delete(productTemp.getProductId());
		logger.debug("O:");
	}
	
}