package com.yeamgood.godungonline.service;

import com.yeamgood.godungonline.form.PriceForm;
import com.yeamgood.godungonline.model.Price;
import com.yeamgood.godungonline.model.User;

public interface PriceService {
	public Price findByIdEncrypt(String idEncrypt,User userSession) throws Exception;
//	public List<Price> findAllByGodungGodungIdOrderByPriceNameAsc(Long godungId) throws Exception;
//	public long count(Long godungId);
	public void save(String productIdEncrypt,PriceForm priceForm,User userSession) throws Exception;
	public void delete(String productIdEncrypt,String priceIdEncrypt,User userSession) throws Exception;
}