package com.yeamgood.godungonline.service;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Godung;
import com.yeamgood.godungonline.model.User;

public interface GodungService {
	public void saveGodung(Godung godung);
	public Godung findGodungByGodungId(long godungId);
	void checkGodungId(Long godungId, User userSession) throws GodungIdException;
}