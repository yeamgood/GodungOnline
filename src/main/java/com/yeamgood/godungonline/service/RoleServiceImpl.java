package com.yeamgood.godungonline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.model.Role;
import com.yeamgood.godungonline.repository.RoleRepository;

@Service("roleService")
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role findRoleById(long id) {
		roleRepository.findById(id);
		return null;
	}

}