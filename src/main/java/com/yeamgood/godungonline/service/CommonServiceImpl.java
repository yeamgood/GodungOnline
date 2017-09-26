package com.yeamgood.godungonline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.model.Common;
import com.yeamgood.godungonline.repository.CommonRepository;

@Service("commonService")
public class CommonServiceImpl implements CommonService{

	@Autowired
	private CommonRepository commonRepository;
	
	@Autowired
    MessageSource messageSource;

	@Override
	public Common findById(long id) {
		return commonRepository.findOne(id);
	}

	@Override
	public List<Common> findByType(String type) {
		return commonRepository.findByType(type);
	}
	
	@Override
	public List<Common> findByTypeMapValueMessageSource(String type) {
		List<Common> commonList = commonRepository.findByType(type);
		for(Common common : commonList) {
			common.setValue( messageSource.getMessage(common.getValue(),null,LocaleContextHolder.getLocale()));
		}
		return commonList;
	}

}