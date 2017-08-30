package com.yeamgood.godungonline.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Brand;
import com.yeamgood.godungonline.model.User;

public interface BrandService {
	public Brand findById(Long id);
	public Brand findById(Long id,User user) throws GodungIdException;
	public List<Brand> findAllOrderByBrandNameAsc();
	public List<Brand> findAllByGodungGodungIdOrderByBrandNameAsc(Long godungId);
	public List<Brand> findByGodungGodungIdAndBrandNameIgnoreCaseContaining(Long godungId, String brand, Pageable pageable);
	public long count(Long godungId);
	public void save(Brand brand,User user);
	public void delete(Brand brand,User user) throws GodungIdException;
}