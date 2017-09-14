package com.yeamgood.godungonline.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Brand;
import com.yeamgood.godungonline.model.User;

public interface BrandService {
	public Brand findByIdEncrypt(String idEncrypt) throws Exception;
	public Brand findByIdEncrypt(String idEncrypt,User user) throws Exception;
	public List<Brand> findAllOrderByBrandNameAsc() throws Exception;
	public List<Brand> findAllByGodungGodungIdOrderByBrandNameAsc(Long godungId) throws Exception;
	public List<Brand> findByGodungGodungIdAndBrandNameIgnoreCaseContaining(Long godungId, String brand, Pageable pageable) throws Exception;
	public long count(Long godungId);
	public void save(Brand brand,User user) throws Exception;
	public void delete(Brand brand,User user) throws GodungIdException, Exception;
}