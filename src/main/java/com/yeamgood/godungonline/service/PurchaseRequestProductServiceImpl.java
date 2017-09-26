package com.yeamgood.godungonline.service;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.form.PurchaseRequestProductForm;
import com.yeamgood.godungonline.model.Measure;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.PurchaseRequest;
import com.yeamgood.godungonline.model.PurchaseRequestProduct;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.MeasureRepository;
import com.yeamgood.godungonline.repository.ProductRepository;
import com.yeamgood.godungonline.repository.PurchaseRequestProductRepository;
import com.yeamgood.godungonline.repository.PurchaseRequestRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.NumberUtils;

@Service("purchaseRequestProductService")
public class PurchaseRequestProductServiceImpl implements PurchaseRequestProductService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PurchaseRequestProductRepository purchaseRequestProductRepository;
	
	@Autowired
	private PurchaseRequestRepository purchaseRequestRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private MeasureRepository measureRepository;
	
	
	@Override
	public PurchaseRequestProduct findByIdEncrypt(String idEncrypt,User userSession) throws GodungIdException  {
		logger.debug("I:");
		logger.debug("O:");
		PurchaseRequestProduct purchaseRequestProduct = purchaseRequestProductRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		purchaseRequestProduct.encryptData();
		return purchaseRequestProduct;
	}


	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(String purchaseRequestIdEncrypt,PurchaseRequestProductForm purchaseRequestProductForm,User userSession) throws ParseException  {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, purchaseRequestProductForm);
		
		PurchaseRequestProduct purchaseRequestProductTemp = new PurchaseRequestProduct();
		if(StringUtils.isBlank(purchaseRequestProductForm.getPurchaseRequestProductIdEncrypt())) {
			purchaseRequestProductTemp.setAmount(NumberUtils.stringToBigDecimal(purchaseRequestProductForm.getAmount()));
			purchaseRequestProductTemp.setPrice(NumberUtils.stringToBigDecimal(purchaseRequestProductForm.getPrice()));
			purchaseRequestProductTemp.setDescription(purchaseRequestProductForm.getDescription());
			
			Product product = null;
			if(!StringUtils.isBlank(purchaseRequestProductForm.getProductIdEncrypt())) {
				product = productRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestProductForm.getProductIdEncrypt()));
			}
			purchaseRequestProductTemp.setProduct(product);
			
			Measure measure = null;
			if(!StringUtils.isBlank(purchaseRequestProductForm.getMeasureIdEncrypt())) {
				measure = measureRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestProductForm.getMeasureIdEncrypt()));
			}
			purchaseRequestProductTemp.setMeasure(measure);
			
			PurchaseRequest purchaseRequest = purchaseRequestRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestIdEncrypt));
			purchaseRequest.getPurchaseRequestProductList().add(purchaseRequestProductTemp);
			purchaseRequestRepository.save(purchaseRequest);
			
		}else {
			purchaseRequestProductTemp = purchaseRequestProductRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestProductForm.getPurchaseRequestProductIdEncrypt()));
			purchaseRequestProductTemp.setAmount(NumberUtils.stringToBigDecimal(purchaseRequestProductForm.getAmount()));
			purchaseRequestProductTemp.setPrice(NumberUtils.stringToBigDecimal(purchaseRequestProductForm.getPrice()));
			purchaseRequestProductTemp.setDescription(purchaseRequestProductForm.getDescription());
			Product product = null;
			if(!StringUtils.isBlank(purchaseRequestProductForm.getProductIdEncrypt())) {
				product = productRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestProductForm.getProductIdEncrypt()));
			}
			purchaseRequestProductTemp.setProduct(product);
			
			Measure measure = null;
			if(!StringUtils.isBlank(purchaseRequestProductForm.getMeasureIdEncrypt())) {
				measure = measureRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestProductForm.getMeasureIdEncrypt()));
			}
			purchaseRequestProductTemp.setMeasure(measure);
			purchaseRequestProductTemp.setUpdate(userSession);
			
			purchaseRequestProductRepository.save(purchaseRequestProductTemp);
		}
		
		
		purchaseRequestProductForm.setPurchaseRequestProductIdEncrypt(AESencrpUtils.encryptLong(purchaseRequestProductTemp.getPurchaseRequestProductId()));
		
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String purchaseRequestIdEncrypt,String purchaseRequestProductIdEncrypt, User userSession) throws GodungIdException{
		logger.debug("I:");
		PurchaseRequest purchaseRequest = purchaseRequestRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestIdEncrypt));
		Long purchaseRequestProductId = AESencrpUtils.decryptLong(purchaseRequestProductIdEncrypt);
		for (PurchaseRequestProduct purchaseRequestProduct : purchaseRequest.getPurchaseRequestProductList()) {
			if(Long.valueOf(purchaseRequestProductId) == Long.valueOf(purchaseRequestProduct.getPurchaseRequestProductId())) {
				purchaseRequest.getPurchaseRequestProductList().remove(purchaseRequestProduct);
				break;
			}
		}
		purchaseRequestRepository.save(purchaseRequest);
		logger.debug("O:");
	}
	
}