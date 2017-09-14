package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.User;

public interface ProductService {
	public Product findByIdEncrypt(String idEncrypt,User userSession) throws Exception;
	public List<Product> findAllByGodungGodungIdOrderByProductNameAsc(Long godungId) throws Exception;
	public long count(Long godungId);
	public void save(Product product,User userSession) throws Exception;
	public void delete(String idEncrypt,User userSession) throws Exception , GodungIdException;
}