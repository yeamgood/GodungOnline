package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Customer;
import com.yeamgood.godungonline.model.User;

public interface CustomerService {
	public Customer findByIdEncrypt(String idEncrypt,User userSession) throws Exception;
	public List<Customer> findAllByGodungGodungIdOrderByCustomerNameAsc(Long godungId) throws Exception;
	public long count(Long godungId);
	public void save(Customer customer,User userSession) throws Exception;
	public void delete(String idEncrypt,User userSession) throws Exception , GodungIdException;
}