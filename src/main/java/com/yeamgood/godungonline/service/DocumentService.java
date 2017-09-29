package com.yeamgood.godungonline.service;

import java.text.ParseException;
import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Document;
import com.yeamgood.godungonline.model.User;

public interface DocumentService {
	public Document findByIdEncrypt(String documentIdEncrypt,User userSession) ;
	public void save(Document document,User userSession) throws ParseException ;
	public void delete(String documentIdEncrypt,User userSession) throws GodungIdException ;
	public List<Document> findAllByGodungGodungIdAndReferenceCode(Long godungId,String referenceCode);
}