package com.yeamgood.godungonline.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.model.Godung;
import com.yeamgood.godungonline.repository.GodungRepository;

@Service("godungService")
public class GodungServiceImpl implements GodungService{

	@Autowired
	private GodungRepository godungRepository;
	
	@Override
	public void saveGodung(Godung godung) {
		godung.setCreateUser("SYSTEM");
		godung.setCreateDate(new Date());
		godungRepository.save(godung);
	}

	@Override
	public Godung findGodungById(long id) {
		return godungRepository.findById(id);
	}

	

}