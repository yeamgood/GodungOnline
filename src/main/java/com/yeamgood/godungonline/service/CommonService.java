package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.model.Common;

public interface CommonService {
	public Common findById(long id);
	public List<Common> findByType(String string);
}