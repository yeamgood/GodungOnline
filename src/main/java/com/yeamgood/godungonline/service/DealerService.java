package com.yeamgood.godungonline.service;

import java.text.ParseException;

import com.yeamgood.godungonline.form.DealerForm;
import com.yeamgood.godungonline.model.Dealer;
import com.yeamgood.godungonline.model.User;

public interface DealerService {
	public Dealer findByIdEncrypt(String dealerIdEncrypt,User userSession) ;
	public void save(String productIdEncrypt,DealerForm dealerForm,User userSession) throws ParseException ;
	public void delete(String productIdEncrypt,String dealerIdEncrypt,User userSession) ;
}