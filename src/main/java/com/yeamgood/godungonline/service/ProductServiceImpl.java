package com.yeamgood.godungonline.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Brand;
import com.yeamgood.godungonline.model.Category;
import com.yeamgood.godungonline.model.Measure;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.BrandRepository;
import com.yeamgood.godungonline.repository.CategoryRepository;
import com.yeamgood.godungonline.repository.MeasureRepository;
import com.yeamgood.godungonline.repository.ProductRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

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
	
	@Override
	public Product findByIdEncrypt(String idEncrypt,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("O:");
		Product product = productRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		product.encryptData(product);
		checkGodungId(product, userSession);
		return product;
	}

	@Override
	public List<Product> findAllByGodungGodungIdOrderByProductNameAsc(Long godungId) throws Exception {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		List<Product> productList = productRepository.findAllByGodungGodungIdOrderByProductCodeAsc(godungId);
		for (Product product : productList) {
			product.setProductIdEncrypt(AESencrpUtils.encryptLong(product.getProductId()));
			product.encryptData(product);
		}
		return productList;
	}

	@Override
	public long count(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return productRepository.countByGodungGodungId(godungId);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Product product,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("I:" +  product.toString());
		if(StringUtils.isBlank(product.getProductIdEncrypt())) {
			Product maxProduct = productRepository.findTopByGodungGodungIdOrderByProductCodeDesc(userSession.getGodung().getGodungId());
			if(maxProduct == null) {
				logger.debug("I:Null Max Data");
				maxProduct = new Product();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_PRODUCT , maxProduct.getProductCode());
			product.setProductCode(generateCode);
			product.setGodung(userSession.getGodung());
			product.setCreate(userSession.getEmail(), new Date());
			
			Brand brand = brandRepository.findOne(AESencrpUtils.decryptLong(product.getBrand().getBrandIdEncrypt()));
			Measure measure = measureRepository.findOne(AESencrpUtils.decryptLong(product.getMeasure().getMeasureIdEncrypt()));
			Category category = categoryRepository.findOne(AESencrpUtils.decryptLong(product.getCategory().getCategoryIdEncrypt()));
			product.setBrand(brand);
			product.setMeasure(measure);
			product.setCategory(category);
			
			product = productRepository.save(product);
			product.setProductIdEncrypt(AESencrpUtils.encryptLong(product.getProductId()));
		}else {
			Long id = AESencrpUtils.decryptLong(product.getProductIdEncrypt());
			Product productTemp = productRepository.findOne(id);
			productTemp.setObject(product);
			productTemp.setUpdate(userSession.getEmail(), new Date());
			
			Brand brand = brandRepository.findOne(AESencrpUtils.decryptLong(product.getBrand().getBrandIdEncrypt()));
			Measure measure = measureRepository.findOne(AESencrpUtils.decryptLong(product.getMeasure().getMeasureIdEncrypt()));
			Category category = categoryRepository.findOne(AESencrpUtils.decryptLong(product.getCategory().getCategoryIdEncrypt()));
			productTemp.setBrand(brand);
			productTemp.setMeasure(measure);
			productTemp.setCategory(category);
			
			product = productRepository.save(productTemp);
			product.setProductIdEncrypt(AESencrpUtils.encryptLong(product.getProductId()));
			logger.debug("I:Step6");
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String idEncrypt, User userSession) throws Exception,GodungIdException{
		logger.debug("I:");
		Product productTemp = productRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		checkGodungId(productTemp, userSession);
		productRepository.delete(productTemp.getProductId());
		logger.debug("O:");
	}
	
	public void checkGodungId(Product productTemp,User userSession) throws GodungIdException {
		long godungIdTemp = productTemp.getGodung().getGodungId().longValue();
		long godungIdSession = userSession.getGodung().getGodungId().longValue();
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
	}
}