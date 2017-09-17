package com.yeamgood.godungonline.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public Dealer findByIdEncrypt(String idEncrypt,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("O:");
		Dealer dealer = dealerRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		dealer.encryptData(dealer);
		return dealer;
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(String productIdEncrypt,DealerForm dealerForm,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("I:productIdEncrypt:" + productIdEncrypt);
		if(StringUtils.isBlank(dealerForm.getDealerIdEncrypt())) {
			Dealer dealer = new Dealer();
			dealer.setPrice(new BigDecimal(dealerForm.getPrice().replace(",", "")));
			dealer.setStartDate(DateUtils.StringToDate(dealerForm.getStartDate(), DateUtils.ddMMyyyy));
			dealer.setEndDate(DateUtils.StringToDate(dealerForm.getEndDate(), DateUtils.ddMMyyyy));
			dealer.setSupplier(supplierRepository.findOne(AESencrpUtils.decryptLong(dealerForm.getSupplierIdEncrypt())));
			dealer.setMeasure(measureRepository.findOne(AESencrpUtils.decryptLong(dealerForm.getMeasureIdEncrypt())));
			dealer.setCurrency(currencyRepository.findOne(dealerForm.getCurrencyId()));
			dealer.setCreate(userSession.getEmail(), new Date());
			
			Product product = productRepository.findOne(AESencrpUtils.decryptLong(productIdEncrypt));
			product.getDealerList().add(dealer);
			productRepository.save(product);
		}else {
			Dealer dealerTemp = dealerRepository.findOne(AESencrpUtils.decryptLong(dealerForm.getDealerIdEncrypt()));
			dealerTemp.setPrice(new BigDecimal(dealerForm.getPrice().replace(",", "")));
			dealerTemp.setStartDate(DateUtils.StringToDate(dealerForm.getStartDate(), DateUtils.ddMMyyyy));
			dealerTemp.setEndDate(DateUtils.StringToDate(dealerForm.getEndDate(), DateUtils.ddMMyyyy));
			dealerTemp.setSupplier(supplierRepository.findOne(AESencrpUtils.decryptLong(dealerForm.getSupplierIdEncrypt())));
			dealerTemp.setMeasure(measureRepository.findOne(AESencrpUtils.decryptLong(dealerForm.getMeasureIdEncrypt())));
			dealerTemp.setCurrency(currencyRepository.findOne(dealerForm.getCurrencyId()));
			dealerTemp.setUpdate(userSession.getEmail(), new Date());
			dealerRepository.save(dealerTemp);
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String productIdEncrypt,String dealerIdEncrypt, User userSession) throws Exception{
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