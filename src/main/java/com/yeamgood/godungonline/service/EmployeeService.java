package com.yeamgood.godungonline.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Employee;
import com.yeamgood.godungonline.model.User;

public interface EmployeeService {
	public Employee findById(Long id);
	public Employee findById(Long id,User user) throws GodungIdException;
	public List<Employee> findAllOrderByEmployeeNameAsc();
	public List<Employee> findAllByGodungGodungIdOrderByEmployeeNameAsc(Long godungId);
	public List<Employee> findByGodungGodungIdAndEmployeeNameIgnoreCaseContaining(Long godungId, String employee, Pageable pageable);
	public long count(Long godungId);
	public void save(Employee employee,User user);
	public void delete(Employee employee,User user) throws GodungIdException;
}