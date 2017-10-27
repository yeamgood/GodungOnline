package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.User;

public interface MenuService {
	public Menu findById(Long id);
	public Menu findOneByMenuCode(String menuCode);
	public Menu findByIdEncrypt(String idEncrypt,User userSession) ;
	public List<Menu> findAllOrderBySequenceAsc() ;
	public long count();
	public void save(Menu menu,User userSession) ;
	public void delete(String idEncrypt,User userSession) throws GodungIdException;
}