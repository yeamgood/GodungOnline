package com.yeamgood.godungonline.service;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.form.DealerForm;
import com.yeamgood.godungonline.model.Dealer;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.CurrencyRepository;
import com.yeamgood.godungonline.repository.DealerRepository;
import com.yeamgood.godungonline.repository.MeasureRepository;
import com.yeamgood.godungonline.repository.ProductRepository;
import com.yeamgood.godungonline.repository.SupplierRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.DateUtils;
import com.yeamgood.godungonline.utils.NumberUtils;

@Service("dealerService")
public class DealerServiceImpl implements DealerService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private DealerRepository dealerRepository;
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	@Autowired
	private MeasureRepository measureRepository;
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Override
	public Dealer findByIdEncrypt(String idEncrypt,User userSession)  {
		logger.debug("I:");
		logger.debug("O:");
		Dealer dealer = dealerRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		dealer.encryptData();
		return dealer;
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(String productIdEncrypt,DealerForm dealerForm,User userSession) throws ParseException  {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, productIdEncrypt);
		if(StringUtils.isBlank(dealerForm.getDealerIdEncrypt())) {
			Dealer dealer = new Dealer();
			dealer.setPrice(NumberUtils.stringToBigDecimal(dealerForm.getPrice()));
			dealer.setStartDate(DateUtils.stringToDate(dealerForm.getStartDate(), DateUtils.DDMMYYYY));
			dealer.setEndDate(DateUtils.stringToDate(dealerForm.getEndDate(), DateUtils.DDMMYYYY));
			dealer.setSupplier(supplierRepository.findOne(AESencrpUtils.decryptLong(dealerForm.getSupplierIdEncrypt())));
			dealer.setMeasure(measureRepository.findOne(AESencrpUtils.decryptLong(dealerForm.getMeasureIdEncrypt())));
			dealer.setCurrency(currencyRepository.findOne(dealerForm.getCurrencyId()));
			dealer.setCreate(userSession);
			
			Product product = productRepository.findOne(AESencrpUtils.decryptLong(productIdEncrypt));
			product.getDealerList().add(dealer);
			productRepository.save(product);
		}else {
			Dealer dealerTemp = dealerRepository.findOne(AESencrpUtils.decryptLong(dealerForm.getDealerIdEncrypt()));
			dealerTemp.setPrice(NumberUtils.stringToBigDecimal(dealerForm.getPrice()));
			dealerTemp.setStartDate(DateUtils.stringToDate(dealerForm.getStartDate(), DateUtils.DDMMYYYY));
			dealerTemp.setEndDate(DateUtils.stringToDate(dealerForm.getEndDate(), DateUtils.DDMMYYYY));
			dealerTemp.setSupplier(supplierRepository.findOne(AESencrpUtils.decryptLong(dealerForm.getSupplierIdEncrypt())));
			dealerTemp.setMeasure(measureRepository.findOne(AESencrpUtils.decryptLong(dealerForm.getMeasureIdEncrypt())));
			dealerTemp.setCurrency(currencyRepository.findOne(dealerForm.getCurrencyId()));
			dealerTemp.setUpdate(userSession);
			dealerRepository.save(dealerTemp);
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String productIdEncrypt,String dealerIdEncrypt, User userSession) {
		logger.debug("I:");
		Product product = productRepository.findOne(AESencrpUtils.decryptLong(productIdEncrypt));
		Long dealerId = AESencrpUtils.decryptLong(dealerIdEncrypt);
		for(Dealer dealer: product.getDealerList()) {
			if(dealer.getDealerId() == dealerId) {
				product.getDealerList().remove(dealer);
				break;
			}
		}
		productRepository.save(product);
		logger.debug("O:");
	}
	
}