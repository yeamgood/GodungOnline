package com.yeamgood.godungonline.service;

import com.yeamgood.godungonline.form.StockForm;
import com.yeamgood.godungonline.model.Stock;
import com.yeamgood.godungonline.model.User;

public interface StockService {
	public Stock findByIdEncrypt(String stockIdEncrypt,User userSession) throws Exception;
	public void save(String productIdEncrypt,StockForm stockForm,User userSession) throws Exception;
	public void delete(String productIdEncrypt,String stockIdEncrypt,User userSession) throws Exception;
}