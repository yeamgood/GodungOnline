package com.yeamgood.godungonline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.model.Common;
import com.yeamgood.godungonline.repository.CommonRepository;

@Service("commonService")
public class CommonServiceImpl implements CommonService{

	@Autowired
	private CommonRepository commonRepository;

	@Override
	public Common findById(long id) {
		return commonRepository.findOne(id);
	}

	@Override
	public List<Common> findByType(String type) {
		return commonRepository.findByType(type);
	}

}