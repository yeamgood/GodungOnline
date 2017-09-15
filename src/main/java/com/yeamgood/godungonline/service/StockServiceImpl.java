package com.yeamgood.godungonline.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.form.StockForm;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.Stock;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.LocationRepository;
import com.yeamgood.godungonline.repository.ProductRepository;
import com.yeamgood.godungonline.repository.StockRepository;
import com.yeamgood.godungonline.repository.WarehouseRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Service("stockService")
public class StockServiceImpl implements StockService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private StockRepository stockRepository;
	
	@Autowired
	private WarehouseRepository warehouseRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Override
	public Stock findByIdEncrypt(String idEncrypt,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("O:");
		Stock stock = stockRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		stock.encryptData(stock);
		return stock;
	}


	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(String productIdEncrypt,StockForm stockForm,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("I:productIdEncrypt:" + productIdEncrypt);
		if(StringUtils.isBlank(stockForm.getStockIdEncrypt())) {
			Stock stock = new Stock();
			stock.setRemindNumber(new BigDecimal(stockForm.getRemindNumber().replace(",", "")));
			stock.setWarehouse(warehouseRepository.findOne(AESencrpUtils.decryptLong(stockForm.getWarehouseIdEncrypt())));
			stock.setLocation(locationRepository.findOne(AESencrpUtils.decryptLong(stockForm.getLocationIdEncrypt())));
			stock.setCreate(userSession.getEmail(), new Date());
			
			Product product = productRepository.findOne(AESencrpUtils.decryptLong(productIdEncrypt));
			product.getStockList().add(stock);
			productRepository.save(product);
		}else {
			Stock stockTemp = stockRepository.findOne(AESencrpUtils.decryptLong(stockForm.getStockIdEncrypt()));
			stockTemp.setRemindNumber(new BigDecimal(stockForm.getRemindNumber().replace(",", "")));
			stockTemp.setWarehouse(warehouseRepository.findOne(AESencrpUtils.decryptLong(stockForm.getWarehouseIdEncrypt())));
			stockTemp.setLocation(locationRepository.findOne(AESencrpUtils.decryptLong(stockForm.getLocationIdEncrypt())));
			stockTemp.setUpdate(userSession.getEmail(), new Date());
			stockRepository.save(stockTemp);
			
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String productIdEncrypt,String stockIdEncrypt, User userSession) throws Exception{
		logger.debug("I:");
		Product product = productRepository.findOne(AESencrpUtils.decryptLong(productIdEncrypt));
		Long stockId = AESencrpUtils.decryptLong(stockIdEncrypt);
		for(Stock stock: product.getStockList()) {
			if(stock.getStockId() == stockId) {
				product.getStockList().remove(stock);
				break;
			}
		}
		productRepository.save(product);
		logger.debug("O:");
	}
	
}