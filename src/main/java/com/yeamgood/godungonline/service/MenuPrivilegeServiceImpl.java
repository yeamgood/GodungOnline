package com.yeamgood.godungonline.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.model.MenuPrivilege;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.MenuPrivilegeRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Service("menuPrivilegeService")
public class MenuPrivilegeServiceImpl implements MenuPrivilegeService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MenuPrivilegeRepository menuPrivilegeRepository;

	@Override
	public MenuPrivilege findByIdEncrypt(String menuPrivilegeIdEncrypt){
		logger.debug("I");
		logger.debug("menuPrivilegeIdEncrypt:{}",menuPrivilegeIdEncrypt);
		MenuPrivilege menuPrivilege = menuPrivilegeRepository.findOne(AESencrpUtils.decryptLong(menuPrivilegeIdEncrypt));
		menuPrivilege.encryptData();
		logger.debug("O");
		return menuPrivilege;
	}
	
	@Override
	public MenuPrivilege findByIdEncrypt(String menuPrivilegeIdEncrypt, User userSession) {
		logger.debug("I");
		logger.debug("menuPrivilegeIdEncrypt:{}",menuPrivilegeIdEncrypt);
		MenuPrivilege menuPrivilege = menuPrivilegeRepository.findOne(AESencrpUtils.decryptLong(menuPrivilegeIdEncrypt));
		menuPrivilege.encryptData();
		logger.debug("O");
		return menuPrivilege;
	}

	@Override
	public List<MenuPrivilege> findAllByMenuIdOrderByMenuPrivilegeNameAsc(String menuIdEncrypt)  {
		logger.debug("I");
		logger.debug("menuId:{}",menuIdEncrypt);
		List<MenuPrivilege> menuPrivilegeList = menuPrivilegeRepository.findAllByMenuIdOrderByMenuPrivilegeNameAsc(AESencrpUtils.decryptLong(menuIdEncrypt));
		for (MenuPrivilege menuPrivilege : menuPrivilegeList) {
			menuPrivilege.setMenuPrivilegeIdEncrypt(AESencrpUtils.encryptLong(menuPrivilege.getMenuPrivilegeId()));
			menuPrivilege.encryptData();
		}
		logger.debug("O");
		return menuPrivilegeList;
	}

	@Override
	public long count(String menuIdEncrypt) {
		logger.debug("I");
		logger.debug("menuIdEncrypt:{}",menuIdEncrypt);
		logger.debug("O");
		return menuPrivilegeRepository.countByMenuId(AESencrpUtils.decryptLong(menuIdEncrypt));
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(MenuPrivilege menuPrivilege,User user)  {
		logger.debug("I");
		logger.debug("menuPrivilege:{}",menuPrivilege);
		logger.debug("user:{}",user);
		if(StringUtils.isBlank(menuPrivilege.getMenuPrivilegeIdEncrypt())) {
			menuPrivilege.setMenuId(AESencrpUtils.decryptLong(menuPrivilege.getMenuIdEncrypt()));
			menuPrivilege.setCreate(user);
			menuPrivilegeRepository.save(menuPrivilege);
		}else {
			MenuPrivilege menuPrivilegeTemp = menuPrivilegeRepository.findOne(AESencrpUtils.decryptLong(menuPrivilege.getMenuPrivilegeIdEncrypt()));
			menuPrivilegeTemp.setMenuPrivilegeCode(menuPrivilege.getMenuPrivilegeCode());
			menuPrivilegeTemp.setMenuPrivilegeName(menuPrivilege.getMenuPrivilegeName());
			menuPrivilegeTemp.setDescription(menuPrivilege.getDescription());
			menuPrivilegeTemp.setUpdate(user);
			menuPrivilegeRepository.save(menuPrivilegeTemp);
		}
		logger.debug("O");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(MenuPrivilege menuPrivilege, User user) {
		logger.debug("I");
		logger.debug("menuPrivilege:{}",menuPrivilege);
		logger.debug("user:{}",user);
		MenuPrivilege menuPrivilegeTemp = menuPrivilegeRepository.findOne(AESencrpUtils.decryptLong(menuPrivilege.getMenuPrivilegeIdEncrypt()));
		menuPrivilegeRepository.delete(menuPrivilegeTemp);
		logger.debug("O");
	}

}