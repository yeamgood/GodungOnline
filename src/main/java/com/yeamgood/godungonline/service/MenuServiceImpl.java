package com.yeamgood.godungonline.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.MenuRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;

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
	
	@Override
	public Menu findOneByMenuCode(String menuCode) {
		logger.debug("I");
		logger.debug("menuCode:{}", menuCode);
		logger.debug("0");
		return menuRepository.findOneByMenuCode(menuCode);
	}
	
	@Override
	public Menu findByIdEncrypt(String idEncrypt,User userSession) {
		logger.debug("I:");
		logger.debug("O:");
		Menu menu = menuRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		menu.encryptData();
		return menu;
	}

	@Override
	public List<Menu> findAllOrderBySequenceAsc()  {
		logger.debug("I:");
		List<Menu> menuList = menuRepository.findAllByOrderBySequenceAsc();
		for (Menu menu : menuList) {
			menu.setMenuIdEncrypt(AESencrpUtils.encryptLong(menu.getMenuId()));
			menu.encryptData();
		}
		logger.debug("O:");
		return menuList;
	}

	@Override
	public long count() {
		logger.debug("I:");
		logger.debug("O:");
		return menuRepository.count();
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Menu menu,User userSession)  {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, menu);
		
		if(menu.getActive() == null) {
			menu.setActive((long) 0);
		}
		
		if(StringUtils.isBlank(menu.getMenuIdEncrypt())) {
			menu.setCreate(userSession);
			menuRepository.save(menu);
			menu.setMenuIdEncrypt(AESencrpUtils.encryptLong(menu.getMenuId()));
		}else {
			Long id = AESencrpUtils.decryptLong(menu.getMenuIdEncrypt());
			Menu menuTemp = menuRepository.findOne(id);
			menuTemp.setObject(menu);
			menuTemp.setUpdate(userSession);
			menuRepository.save(menuTemp);
			menu.setMenuIdEncrypt(AESencrpUtils.encryptLong(menuTemp.getMenuId()));
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String idEncrypt, User userSession) throws GodungIdException{
		logger.debug("I:");
		Menu menuTemp = menuRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		menuRepository.delete(menuTemp);
		logger.debug("O:");
	}
	
}