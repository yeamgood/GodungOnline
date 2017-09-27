package com.yeamgood.godungonline.service;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Approver;
import com.yeamgood.godungonline.model.User;

public interface ApproverService {
	public Approver findByIdEncrypt(String idEncrypt,User userSession) throws GodungIdException ;
}