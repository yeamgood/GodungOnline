package com.yeamgood.godungonline.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Employee;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.EmployeeRepository;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Employee findById(Long id) {
		logger.debug("I:");
		logger.debug("O:");
		return employeeRepository.findOne(id);
	}

	@Override
	public List<Employee> findAllOrderByEmployeeNameAsc() {
		logger.debug("I:");
		logger.debug("O:");
		return employeeRepository.findAll(sortByEmployeeNameAsc());
	}

	@Override
	public List<Employee> findAllByGodungGodungIdOrderByEmployeeNameAsc(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return employeeRepository.findAllByGodungGodungIdOrderByEmployeeNameAsc(godungId);
	}
	
	private Sort sortByEmployeeNameAsc() {
        return new Sort(Sort.Direction.ASC, "employeeName");
    }

	@Override
	public long count(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return employeeRepository.countByGodungGodungId(godungId);
	}

	@Override
	public List<Employee> findByGodungGodungIdAndEmployeeNameIgnoreCaseContaining(Long godungId, String employeeName, Pageable pageable) {
		return employeeRepository.findByGodungGodungIdAndEmployeeNameIgnoreCaseContaining(godungId, employeeName, pageable);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Employee employee,User user) {
		logger.debug("I:");
		if(employee.getEmployeeId() == null) {
			Employee maxEmployee = employeeRepository.findTopByGodungGodungIdOrderByEmployeeCodeDesc(user.getGodung().getGodungId());
			if(maxEmployee == null) {
				logger.debug("I:Null Max Data");
				maxEmployee = new Employee();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_EMPLOYEE, maxEmployee.getEmployeeCode());
			
			employee.setGodung(user.getGodung());
			employee.setCreate(user.getEmail(), new Date());
			employee.setEmployeeCode(generateCode);
			employee.setGodung(user.getGodung());
			employeeRepository.save(employee);
		}else {
			Employee employeeTemp = employeeRepository.findOne(employee.getEmployeeId());
			employeeTemp.setEmployeeName(employee.getEmployeeName());
			employeeTemp.setDescription(employee.getDescription());
			employeeTemp.setUpdate(user.getEmail(), new Date());
			employeeRepository.save(employeeTemp);
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(Employee employee, User user) throws GodungIdException {
		logger.debug("I:");
		Employee employeeTemp = employeeRepository.findOne(employee.getEmployeeId());
		long godungIdTemp = employeeTemp.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		
		if(godungIdTemp ==  godungIdSession) {
			employeeRepository.delete(employeeTemp);
		}else {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		logger.debug("O:");
	}

	@Override
	public Employee findById(Long id, User user) throws GodungIdException {
		logger.debug("I:");
		Employee employee = employeeRepository.findOne(id);
		long godungIdTemp = employee.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		
		logger.debug("O:");
		return employee;
	}
}