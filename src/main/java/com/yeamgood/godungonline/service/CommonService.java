package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.model.Common;

public interface CommonService {
	public Common findCommonById(long id);

	public List<Common> findCommonByType(String string);
}