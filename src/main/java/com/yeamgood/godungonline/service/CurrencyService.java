package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.model.Currency;

public interface CurrencyService {
	public List<Currency> findAll();
	public List<Currency> findAllByOrderByCurrencyNameAsc();
}