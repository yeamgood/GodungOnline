package com.yeamgood.godungonline.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.model.ApproverRole;
import com.yeamgood.godungonline.repository.ApproverRoleRepository;

@Service("approverRoleService")
public class ApproverRoleServiceImpl implements ApproverRoleService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ApproverRoleRepository approverRoleRepository;
	
	@Autowired
	MessageSource messageSource;
	
	@Override
	public ApproverRole findOneByApproverRoleCode(String code) {
		logger.debug("I");
		logger.debug("O");
		return approverRoleRepository.findOneByApproverRoleCode(code);
	}

	@Override
	public List<ApproverRole> findAll() {
		logger.debug("I");
		logger.debug("O");
		List<ApproverRole> approverRoleList = approverRoleRepository.findAll(new Sort(Sort.Direction.ASC, "sequence"));
		for(ApproverRole obj : approverRoleList) {
			obj.setMessage(messageSource.getMessage(obj.getMessage(),null,LocaleContextHolder.getLocale()));
		}
		return approverRoleList;
	}
	
}