package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Brand;
import com.yeamgood.godungonline.model.User;

public interface BrandService {
	public Brand findByIdEncrypt(String idEncrypt);
	public Brand findByIdEncrypt(String idEncrypt,User user) throws GodungIdException;
	public List<Brand> findAllOrderByBrandNameAsc();
	public List<Brand> findAllByGodungGodungIdOrderByBrandNameAsc(Long godungId);
	public long count(Long godungId);
	public void save(Brand brand,User user);
	public void delete(Brand brand,User user) throws GodungIdException;
}