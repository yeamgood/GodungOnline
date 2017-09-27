package com.yeamgood.godungonline.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Approver;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.ApproverRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Service("approverService")
public class ApproverServiceImpl implements ApproverService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ApproverRepository approverRepository;
	
	@Override
	public Approver findByIdEncrypt(String idEncrypt,User userSession) throws GodungIdException  {
		logger.debug("I:");
		logger.debug("O:");
		Approver approver = approverRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		approver.encryptData();
		return approver;
	}
	
}