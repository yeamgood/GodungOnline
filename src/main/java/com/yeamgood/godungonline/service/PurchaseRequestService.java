package com.yeamgood.godungonline.service;

import java.text.ParseException;
import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.form.ApproverForm;
import com.yeamgood.godungonline.form.PurchaseRequestForm;
import com.yeamgood.godungonline.model.Document;
import com.yeamgood.godungonline.model.PurchaseRequest;
import com.yeamgood.godungonline.model.User;

public interface PurchaseRequestService {
	public PurchaseRequest findByIdEncrypt(String idEncrypt,User userSession) throws GodungIdException ;
	public List<PurchaseRequest> findAllByGodungGodungIdOrderByPurchaseRequestNameAsc(Long godungId) ;
	public long count(Long godungId);
	public void save(PurchaseRequestForm purchaseRequestForm,User userSession) throws ParseException ;
	public void delete(String idEncrypt,User userSession) throws GodungIdException;
	public void approverSave(String purchaseRequestIdEncrypt,ApproverForm approverForm,User userSession) throws GodungIdException;
	public void approverDelete(String purchaseRequestIdEncrypt,String approverIdEncrypt,User userSession) throws GodungIdException;
	public void documentUpload(String purchaseRequestIdEncrypt,Document document,User userSession) throws GodungIdException;
	public void documentDelete(String purchaseRequestIdEncrypt,String documentIdEncrypt,User userSession) throws GodungIdException;
}