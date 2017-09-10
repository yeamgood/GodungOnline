package com.yeamgood.godungonline.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Country;
import com.yeamgood.godungonline.model.Employee;
import com.yeamgood.godungonline.model.Province;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.CountryRepository;
import com.yeamgood.godungonline.repository.EmployeeRepository;
import com.yeamgood.godungonline.repository.ProvinceRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private ProvinceRepository provinceRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Override
	public Employee findByIdEncrypt(String idEncrypt,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("O:");
		Employee employeeTemp = employeeRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		employeeTemp.setEmployeeIdEncrypt(idEncrypt);
		checkGodungId(employeeTemp, userSession);
		return employeeTemp;
	}

	@Override
	public List<Employee> findAllByGodungGodungIdOrderByEmployeeNameAsc(Long godungId) throws Exception {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		List<Employee> employeeList = employeeRepository.findAllByGodungGodungIdOrderByEmployeeCodeAsc(godungId);
		for (Employee employee : employeeList) {
			employee.setEmployeeIdEncrypt(AESencrpUtils.encryptLong(employee.getEmployeeId()));
		}
		return employeeList;
	}

	@Override
	public long count(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return employeeRepository.countByGodungGodungId(godungId);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Employee employee,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("I:" +  employee.toString());
		if(StringUtils.isBlank(employee.getEmployeeIdEncrypt())) {
			Employee maxEmployee = employeeRepository.findTopByGodungGodungIdOrderByEmployeeCodeDesc(userSession.getGodung().getGodungId());
			if(maxEmployee == null) {
				logger.debug("I:Null Max Data");
				maxEmployee = new Employee();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_EMPLOYEE, maxEmployee.getEmployeeCode());
			employee.setEmployeeCode(generateCode);
			employee.setGodung(userSession.getGodung());
			employee.setCreate(userSession.getEmail(), new Date());
			
			// PROVINCE
			Province provinceTemp = employee.getAddress().getProvince();
			provinceTemp =  provinceRepository.findByProvinceCode(provinceTemp.getProvinceCode());
			employee.getAddress().setProvince(provinceTemp);
			
			// COUNTRY
			Country countryTemp = employee.getAddress().getCountry();
			countryTemp = countryRepository.findOne(countryTemp.getCountryId());
			employee.getAddress().setCountry(countryTemp);
			
			employee = employeeRepository.save(employee);
			employee.setEmployeeIdEncrypt(AESencrpUtils.encryptLong(employee.getEmployeeId()));
		}else {
			Long id = AESencrpUtils.decryptLong(employee.getEmployeeIdEncrypt());
			Employee employeeTemp = employeeRepository.findOne(id);
			employeeTemp.setObject(employee);
			employeeTemp.setUpdate(userSession.getEmail(), new Date());
			
			// ADDRESS
			employeeTemp.getAddress().setObject(employee.getAddress());
			
			// PROVINCE
			Province provinceTemp = employee.getAddress().getProvince();
			provinceTemp =  provinceRepository.findByProvinceCode(provinceTemp.getProvinceCode());
			employeeTemp.getAddress().setProvince(provinceTemp);
			
			// COUNTRY
			Country countryTemp = employee.getAddress().getCountry();
			countryTemp = countryRepository.findOne(countryTemp.getCountryId());
			employeeTemp.getAddress().setCountry(countryTemp);
			
			employee = employeeRepository.save(employeeTemp);
			employee.setEmployeeIdEncrypt(AESencrpUtils.encryptLong(employee.getEmployeeId()));
			logger.debug("I:Step6");
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String idEncrypt, User userSession) throws Exception,GodungIdException{
		logger.debug("I:");
		Employee employeeTemp = employeeRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		checkGodungId(employeeTemp, userSession);
		employeeRepository.delete(employeeTemp.getEmployeeId());
		logger.debug("O:");
	}
	
	public void checkGodungId(Employee employeeTemp,User userSession) throws GodungIdException {
		long godungIdTemp = employeeTemp.getGodung().getGodungId().longValue();
		long godungIdSession = userSession.getGodung().getGodungId().longValue();
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
	}
}