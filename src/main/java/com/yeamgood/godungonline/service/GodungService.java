package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Godung;
import com.yeamgood.godungonline.model.User;

public interface GodungService {
	public void saveGodung(Godung godung);
	public Godung findGodungByGodungId(long godungId);
	public Godung findByIdEncrypt(String godungIdEncrypt);
	void checkGodungId(Long godungId, User userSession) throws GodungIdException;
	public List<Godung> findAllOrderByGodungNameAsc();
	public void save(Godung godung,User user);
	public void delete(Godung godung);
}