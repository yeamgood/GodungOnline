package com.yeamgood.godungonline.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Role;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.RoleRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Service("roleService")
public class RoleServiceImpl implements RoleService{

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role findByIdEncrypt(String idEncrypt) {
		logger.debug("I");
		Role role = roleRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		roleRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		role.encryptData();
		logger.debug("O");
		return role;
	}

	@Override
	public List<Role> findAllOrderByRoleNameAsc() {
		logger.debug("I");
		List<Role> roleList = roleRepository.findAll(new Sort(Sort.Direction.ASC, "roleName"));
		for (Role role : roleList) {
			role.encryptData();
		}
		logger.debug("O");
		return roleList;
	}

	@Override
	public long count() {
		logger.debug("I");
		logger.debug("O");
		return roleRepository.count();
	}

	@Transactional(rollbackFor={Exception.class})
	public void save(Role role,User user)  {
		logger.debug("I");
		logger.debug("role:{}",role);
		logger.debug("user:{}",user);
		if(StringUtils.isBlank(role.getRoleIdEncrypt())) {
			role.setCreate(user);
			roleRepository.save(role);
		}else {
			Role roleTemp = roleRepository.findOne(AESencrpUtils.decryptLong(role.getRoleIdEncrypt()));
			roleTemp.setRoleName(role.getRoleName());
			roleTemp.setDescription(role.getDescription());
			roleTemp.setUpdate(user);
			roleRepository.save(roleTemp);
		}
		logger.debug("O");
	}

	@Override
	public void delete(Role role, User user) throws GodungIdException {
		logger.debug("I");
		Role roleTemp = roleRepository.findOne(AESencrpUtils.decryptLong(role.getRoleIdEncrypt()));
		roleRepository.delete(roleTemp);
		logger.debug("O");
	}

	

}