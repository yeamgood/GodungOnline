package com.yeamgood.godungonline.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.model.Country;
import com.yeamgood.godungonline.repository.CountryRepository;

@Service("countryService")
public class CountryServiceImpl implements CountryService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CountryRepository countryRepository;

	@Override
	public List<Country> findAll() {
		logger.debug("I:");
		logger.debug("O:");
		return countryRepository.findAll();
	}

	@Override
	public List<Country> findAllByOrderByCountryNameAsc() {
		logger.debug("I:");
		logger.debug("O:");
		return countryRepository.findAllByOrderByCountryNameAsc();
	}

	
}