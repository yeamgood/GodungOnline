package com.yeamgood.godungonline.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.model.Currency;
import com.yeamgood.godungonline.repository.CurrencyRepository;

@Service("currencyService")
public class CurrencyServiceImpl implements CurrencyService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CurrencyRepository currencyRepository;

	@Override
	public List<Currency> findAll() {
		logger.debug("I:");
		logger.debug("O:");
		return currencyRepository.findAll();
	}

	@Override
	public List<Currency> findAllByOrderByCurrencyNameAsc() {
		logger.debug("I:");
		logger.debug("O:");
		return currencyRepository.findAllByOrderByCurrencyNameAsc();
	}

	
}