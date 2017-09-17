package com.yeamgood.godungonline.service;

import com.yeamgood.godungonline.form.DealerForm;
import com.yeamgood.godungonline.model.Dealer;
import com.yeamgood.godungonline.model.User;

public interface DealerService {
	public Dealer findByIdEncrypt(String dealerIdEncrypt,User userSession) throws Exception;
	public void save(String productIdEncrypt,DealerForm dealerForm,User userSession) throws Exception;
	public void delete(String productIdEncrypt,String dealerIdEncrypt,User userSession) throws Exception;
}