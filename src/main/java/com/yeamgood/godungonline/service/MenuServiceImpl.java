package com.yeamgood.godungonline.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.repository.MenuRepository;

@Service("menuService")
public class MenuServiceImpl implements MenuService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MenuRepository menuRepository;

	@Override
	public Menu findById(Long menuId) {
		logger.debug("I");
		logger.debug("menuId:{}", menuId);
		logger.debug("O");
		return menuRepository.findOne(menuId);
	}
	
}