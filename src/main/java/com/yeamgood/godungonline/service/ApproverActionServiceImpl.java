package com.yeamgood.godungonline.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.model.ApproverAction;
import com.yeamgood.godungonline.repository.ApproverActionRepository;

@Service("approverActionService")
public class ApproverActionServiceImpl implements ApproverActionService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ApproverActionRepository approverActionRepository;
	
	@Override
	public ApproverAction findOneByApproverActionCode(String code) {
		logger.debug("I");
		logger.debug("O");
		return approverActionRepository.findOneByApproverActionCode(code);
	}
	
}