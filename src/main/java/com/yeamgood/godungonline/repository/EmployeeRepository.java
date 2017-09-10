package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Employee;


@Repository("employeeRepository")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	public long countByGodungGodungId(Long godungId);
    public Employee findTopByGodungGodungIdOrderByEmployeeCodeDesc(Long godungId);
	public List<Employee> findAllByGodungGodungIdOrderByEmployeeCodeAsc(Long godungId);
}
