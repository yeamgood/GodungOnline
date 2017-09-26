package com.yeamgood.godungonline.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.form.SaleForm;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.Sale;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.CurrencyRepository;
import com.yeamgood.godungonline.repository.MeasureRepository;
import com.yeamgood.godungonline.repository.ProductRepository;
import com.yeamgood.godungonline.repository.SaleRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.NumberUtils;

@Service("saleService")
public class SaleServiceImpl implements SaleService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private SaleRepository saleRepository;
	
	@Autowired
	private MeasureRepository measureRepository;
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Override
	public Sale findByIdEncrypt(String idEncrypt,User userSession)  {
		logger.debug("I:");
		logger.debug("O:");
		Sale sale = saleRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		sale.encryptData();
		return sale;
	}


	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(String productIdEncrypt,SaleForm saleForm,User userSession)  {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, productIdEncrypt);
		
		if(StringUtils.isBlank(saleForm.getSaleIdEncrypt())) {
			Product product = productRepository.findOne(AESencrpUtils.decryptLong(productIdEncrypt));
			Sale sale = new Sale();
			sale.setStartDate(saleForm.getStartDate());
			sale.setEndDate(saleForm.getEndDate());
			sale.setPrice(NumberUtils.stringToBigDecimal(saleForm.getPrice()));
			sale.setMeasure(measureRepository.findOne(AESencrpUtils.decryptLong(saleForm.getMeasureIdEncrypt())));
			sale.setCurrency(currencyRepository.findOne(Long.valueOf(saleForm.getCurrencyId())));
			sale.setUpdate(userSession);
			
			product.getSaleList().add(sale);
			productRepository.save(product);
		}else {
			Sale saleTemp = saleRepository.findOne(AESencrpUtils.decryptLong(saleForm.getSaleIdEncrypt()));
			saleTemp.setStartDate(saleForm.getStartDate());
			saleTemp.setEndDate(saleForm.getEndDate());
			saleTemp.setPrice(NumberUtils.stringToBigDecimal(saleForm.getPrice()));
			saleTemp.setMeasure(measureRepository.findOne(AESencrpUtils.decryptLong(saleForm.getMeasureIdEncrypt())));
			saleTemp.setCurrency(currencyRepository.findOne(Long.valueOf(saleForm.getCurrencyId())));
			saleTemp.setCreate(userSession);
			saleRepository.save(saleTemp);
		}
		
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String productIdEncrypt,String saleIdEncrypt, User userSession) {
		logger.debug("I:");
		Product product = productRepository.findOne(AESencrpUtils.decryptLong(productIdEncrypt));
		Long saleId = AESencrpUtils.decryptLong(saleIdEncrypt);
		for(Sale sale: product.getSaleList()) {
			if(sale.getSaleId() == saleId) {
				product.getSaleList().remove(sale);
				break;
			}
		}
		productRepository.save(product);
		logger.debug("O:");
	}
	
}