package com.yeamgood.godungonline.service;

import java.text.ParseException;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.form.PurchaseRequestProductForm;
import com.yeamgood.godungonline.model.PurchaseRequestProduct;
import com.yeamgood.godungonline.model.User;

public interface PurchaseRequestProductService {
	public PurchaseRequestProduct findByIdEncrypt(String idEncrypt,User userSession) throws GodungIdException ;
	public void save(String purchaseRequestIdEncrypt,PurchaseRequestProductForm purchaseRequestProductForm,User userSession) throws ParseException ;
	public void delete(String purchaseRequestIdEncrypt,String purchaseRequestProductIdEncrypt,User userSession) throws GodungIdException;
}