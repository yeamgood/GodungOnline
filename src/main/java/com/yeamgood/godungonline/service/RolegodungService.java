package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Rolegodung;
import com.yeamgood.godungonline.model.User;

public interface RolegodungService {
	public Rolegodung findByIdEncrypt(String idEncrypt,User userSession) throws Exception;
	public List<Rolegodung> findAllByGodungGodungIdOrderByRolegodungNameAsc(Long godungId) throws Exception;
	public long count(Long godungId);
	public void save(Rolegodung rolegodung,User userSession) throws Exception;
	public void delete(String idEncrypt,User userSession) throws Exception , GodungIdException;
}