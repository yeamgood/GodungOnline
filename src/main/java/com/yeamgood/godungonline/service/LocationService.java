package com.yeamgood.godungonline.service;

import com.yeamgood.godungonline.model.Location;
import com.yeamgood.godungonline.model.User;

public interface LocationService {

	Location findByIdEncrypt(String idEncrypt, User userSession) throws Exception;
	
}