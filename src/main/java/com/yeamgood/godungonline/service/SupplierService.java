package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Supplier;
import com.yeamgood.godungonline.model.User;

public interface SupplierService {
	public Supplier findById(Long id);
	public Supplier findById(Long id,User user) throws GodungIdException;
	public List<Supplier> findAllOrderBySupplierNameAsc();
	public List<Supplier> findAllByGodungGodungIdOrderBySupplierNameAsc(Long godungId);
	public long count(Long godungId);
	public void save(Supplier supplier,User user);
	public void delete(Supplier supplier,User user) throws GodungIdException;
}