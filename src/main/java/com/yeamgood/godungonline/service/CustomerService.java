package com.yeamgood.godungonline.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Customer;
import com.yeamgood.godungonline.model.User;

public interface CustomerService {
	public Customer findById(Long id);
	public Customer findById(Long id,User user) throws GodungIdException;
	public List<Customer> findAllOrderByCustomerNameAsc();
	public List<Customer> findAllByGodungGodungIdOrderByCustomerNameAsc(Long godungId);
	public List<Customer> findByGodungGodungIdAndCustomerNameIgnoreCaseContaining(Long godungId, String customer, Pageable pageable);
	public long count(Long godungId);
	public void save(Customer customer,User user);
	public void delete(Customer customer,User user) throws GodungIdException;
}