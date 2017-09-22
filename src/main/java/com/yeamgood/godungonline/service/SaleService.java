package com.yeamgood.godungonline.service;

import com.yeamgood.godungonline.form.SaleForm;
import com.yeamgood.godungonline.model.Sale;
import com.yeamgood.godungonline.model.User;

public interface SaleService {
	public Sale findByIdEncrypt(String idEncrypt,User userSession) ;
	public void save(String productIdEncrypt,SaleForm saleForm,User userSession) ;
	public void delete(String productIdEncrypt,String saleIdEncrypt,User userSession) ;
}