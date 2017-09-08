package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.model.Province;

public interface ProvinceService {
	public List<Province> findAll();
	public List<Province> findAllByOrderByProvinceNameAsc();
}