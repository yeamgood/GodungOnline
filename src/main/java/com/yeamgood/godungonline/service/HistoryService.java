package com.yeamgood.godungonline.service;

import java.text.ParseException;
import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.History;
import com.yeamgood.godungonline.model.User;

public interface HistoryService {
	public History findByIdEncrypt(String historyIdEncrypt,User userSession) ;
	public void save(History history,User userSession) throws ParseException ;
	public void delete(String historyIdEncrypt,User userSession) throws GodungIdException ;
	public List<History> findAll(Long godungId,String historyCode);
}