package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Employee;
import com.yeamgood.godungonline.model.User;

public interface EmployeeService {
	public Employee findByIdEncrypt(String idEncrypt,User userSession) throws Exception;
	public List<Employee> findAllByGodungGodungIdOrderByEmployeeNameAsc(Long godungId) throws Exception;
	public long count(Long godungId);
	public void save(Employee employee,User userSession) throws Exception;
	public void delete(String idEncrypt,User userSession) throws Exception , GodungIdException;
}