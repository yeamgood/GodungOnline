package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.model.MenuPrivilege;
import com.yeamgood.godungonline.model.User;

public interface MenuPrivilegeService {
	public MenuPrivilege findByIdEncrypt(String idEncrypt);
	public MenuPrivilege findByIdEncrypt(String idEncrypt,User user);
	public List<MenuPrivilege> findAllByMenuIdOrderByMenuPrivilegeNameAsc(String menuIdEncrypt);
	public long count(String menuIdEncrypt);
	public void save(MenuPrivilege menuPrivilege,User user);
	public void delete(MenuPrivilege menuPrivilege,User user);
}