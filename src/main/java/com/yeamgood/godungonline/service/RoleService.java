package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Role;
import com.yeamgood.godungonline.model.User;

public interface RoleService {
	public Role findByIdEncrypt(String idEncrypt) ;
	public List<Role> findAllOrderByRoleNameAsc() ;
	public long count();
	public void save(Role role,User user) ;
	public void delete(Role role,User user) throws GodungIdException;
}