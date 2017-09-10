package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.model.Country;

public interface CountryService {
	public List<Country> findAll();
	public List<Country> findAllByOrderByCountryNameAsc();
}