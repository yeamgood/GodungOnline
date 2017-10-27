package com.yeamgood.godungonline.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.model.RoleLogin;
import com.yeamgood.godungonline.repository.RoleLoginRepository;

@Service("roleLoginService")
public class RoleLoginServiceImpl implements RoleLoginService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RoleLoginRepository roleLoginRepository;

	@Override
	public List<RoleLogin> findAll() {
		logger.debug("I");
		logger.debug("O");
		return roleLoginRepository.findAll();
	}

}