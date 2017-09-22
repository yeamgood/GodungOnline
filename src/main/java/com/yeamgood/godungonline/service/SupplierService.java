package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Supplier;
import com.yeamgood.godungonline.model.User;

public interface SupplierService {
	public Supplier findByIdEncrypt(String idEncrypt,User userSession) throws GodungIdException ;
	public List<Supplier> findAllByGodungGodungIdOrderBySupplierNameAsc(Long godungId) ;
	public long count(Long godungId);
	public void save(Supplier supplier,User userSession) ;
	public void delete(String idEncrypt,User userSession) throws GodungIdException;
}