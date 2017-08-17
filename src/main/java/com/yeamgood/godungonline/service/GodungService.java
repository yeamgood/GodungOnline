package com.yeamgood.godungonline.service;

import com.yeamgood.godungonline.model.Godung;

public interface GodungService {
	public void saveGodung(Godung godung);
	public Godung findGodungById(long id);
}