package com.yeamgood.godungonline.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.model.Location;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.LocationRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Service("locationService")
public class LocationServiceImpl implements LocationService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	LocationRepository locationRepository;
	
	@Override
	public Location findByIdEncrypt(String idEncrypt,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("O:");
		Location location = locationRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		location.setLocationIdEncrypt(idEncrypt);
		return location;
	}
	
}