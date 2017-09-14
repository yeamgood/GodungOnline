package com.yeamgood.godungonline.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.form.PriceForm;
import com.yeamgood.godungonline.model.Price;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.CurrencyRepository;
import com.yeamgood.godungonline.repository.MeasureRepository;
import com.yeamgood.godungonline.repository.PriceRepository;
import com.yeamgood.godungonline.repository.ProductRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Service("priceService")
public class PriceServiceImpl implements PriceService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private PriceRepository priceRepository;
	
	@Autowired
	private MeasureRepository measureRepository;
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Override
	public Price findByIdEncrypt(String idEncrypt,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("O:");
		Price price = priceRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		price.encryptData(price);
		return price;
	}


	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(String productIdEncrypt,PriceForm priceForm,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("I:productIdEncrypt:" + productIdEncrypt);
		
		if(StringUtils.isBlank(priceForm.getPriceIdEncrypt())) {
			Product product = productRepository.findOne(AESencrpUtils.decryptLong(productIdEncrypt));
			Price price = new Price();
			price.setStartDate(priceForm.getStartDate());
			price.setEndDate(priceForm.getEndDate());
			price.setPrice(new BigDecimal(priceForm.getPriceText().replace(",", "")));
			price.setMeasure(measureRepository.findOne(AESencrpUtils.decryptLong(priceForm.getMeasureIdEncrypt())));
			price.setCurrency(currencyRepository.findOne(Long.valueOf(priceForm.getCurrencyId())));
			price.setUpdate(userSession.getEmail(), new Date());
			
			product.getPriceList().add(price);
			productRepository.save(product);
		}else {
			Price priceTemp = priceRepository.findOne(AESencrpUtils.decryptLong(priceForm.getPriceIdEncrypt()));
			priceTemp.setStartDate(priceForm.getStartDate());
			priceTemp.setEndDate(priceForm.getEndDate());
			priceTemp.setPrice(new BigDecimal(priceForm.getPriceText().replace(",", "")));
			priceTemp.setMeasure(measureRepository.findOne(AESencrpUtils.decryptLong(priceForm.getMeasureIdEncrypt())));
			priceTemp.setCurrency(currencyRepository.findOne(Long.valueOf(priceForm.getCurrencyId())));
			priceTemp.setCreate(userSession.getEmail(), new Date());
			priceTemp = priceRepository.save(priceTemp);
		}
		
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String productIdEncrypt,String priceIdEncrypt, User userSession) throws Exception{
		logger.debug("I:");
		Product product = productRepository.findOne(AESencrpUtils.decryptLong(productIdEncrypt));
		Long priceId = AESencrpUtils.decryptLong(priceIdEncrypt);
		for(Price price: product.getPriceList()) {
			if(price.getPriceId() == priceId) {
				product.getPriceList().remove(price);
				break;
			}
		}
		productRepository.save(product);
		logger.debug("O:");
	}
	
	
//	@Override
//	public void deleteLocation(String warehouseIdEncrypt, String locationIdEncrypt, User userSession)throws Exception, GodungIdException {
//		logger.debug("I:");
//		Warehouse warehouseTemp = warehouseRepository.findOne(AESencrpUtils.decryptLong(warehouseIdEncrypt));
//		Long locationId = AESencrpUtils.decryptLong(locationIdEncrypt);
//		checkGodungId(warehouseTemp, userSession);
//		for (Location locationTemp : warehouseTemp.getLocationList()) {
//			if(locationTemp.getLocationId() == locationId) {
//				warehouseTemp.getLocationList().remove(locationTemp);
//				break;
//			}
//		}
//		warehouseRepository.save(warehouseTemp);
//		logger.debug("O:");
//	}
//	
}