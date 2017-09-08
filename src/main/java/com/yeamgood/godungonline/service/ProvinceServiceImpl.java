package com.yeamgood.godungonline.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.model.Province;
import com.yeamgood.godungonline.repository.ProvinceRepository;

@Service("provinceService")
public class ProvinceServiceImpl implements ProvinceService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProvinceRepository provinceRepository;

	@Override
	public List<Province> findAll() {
		logger.debug("I:");
		logger.debug("O:");
		return provinceRepository.findAll();
	}

	@Override
	public List<Province> findAllByOrderByProvinceNameAsc() {
		logger.debug("I:");
		logger.debug("O:");
		return provinceRepository.findAllByOrderByProvinceNameAsc();
	}

	
}