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
import com.yeamgood.godungonline.model.Province;
import com.yeamgood.godungonline.model.Customer;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.ProvinceRepository;
import com.yeamgood.godungonline.repository.CustomerRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ProvinceRepository provinceRepository;
	
	@Override
	public Customer findByIdEncrypt(String idEncrypt,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("O:");
		Customer customerTemp = customerRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		customerTemp.setCustomerIdEncrypt(idEncrypt);
		checkGodungId(customerTemp, userSession);
		return customerTemp;
	}

	@Override
	public List<Customer> findAllByGodungGodungIdOrderByCustomerNameAsc(Long godungId) throws Exception {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		List<Customer> customerList = customerRepository.findAllByGodungGodungIdOrderByCustomerCodeAsc(godungId);
		for (Customer customer : customerList) {
			customer.setCustomerIdEncrypt(AESencrpUtils.encryptLong(customer.getCustomerId()));
		}
		return customerList;
	}

	@Override
	public long count(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return customerRepository.countByGodungGodungId(godungId);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Customer customer,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("I:" +  customer.toString());
		if(StringUtils.isBlank(customer.getCustomerIdEncrypt())) {
			Customer maxCustomer = customerRepository.findTopByGodungGodungIdOrderByCustomerCodeDesc(userSession.getGodung().getGodungId());
			if(maxCustomer == null) {
				logger.debug("I:Null Max Data");
				maxCustomer = new Customer();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_CUSTOMER, maxCustomer.getCustomerCode());
			customer.setCustomerCode(generateCode);
			customer.setGodung(userSession.getGodung());
			customer.setCreate(userSession.getEmail(), new Date());
			
			// PROVINCE
			Province provinceTemp = customer.getAddress().getProvince();
			provinceTemp =  provinceRepository.findByProvinceCode(provinceTemp.getProvinceCode());
			customer.getAddress().setProvince(provinceTemp);
			
			Province provinceSendTemp = customer.getAddressSend().getProvince();
			provinceSendTemp =  provinceRepository.findByProvinceCode(provinceSendTemp.getProvinceCode());
			customer.getAddressSend().setProvince(provinceSendTemp);
			
			customer = customerRepository.save(customer);
			customer.setCustomerIdEncrypt(AESencrpUtils.encryptLong(customer.getCustomerId()));
		}else {
			Long id = AESencrpUtils.decryptLong(customer.getCustomerIdEncrypt());
			Customer customerTemp = customerRepository.findOne(id);
			customerTemp.setObject(customer);
			customerTemp.setUpdate(userSession.getEmail(), new Date());
			
			// ADDRESS
			customerTemp.getAddress().setObject(customer.getAddress());
			customerTemp.getAddressSend().setObject(customer.getAddressSend());
			
			// PROVINCE
			Province provinceTemp = customer.getAddress().getProvince();
			provinceTemp =  provinceRepository.findByProvinceCode(provinceTemp.getProvinceCode());
			customerTemp.getAddress().setProvince(provinceTemp);
			
			Province provinceSendTemp = customer.getAddressSend().getProvince();
			provinceSendTemp =  provinceRepository.findByProvinceCode(provinceSendTemp.getProvinceCode());
			customerTemp.getAddressSend().setProvince(provinceSendTemp);
			
			customer = customerRepository.save(customerTemp);
			customer.setCustomerIdEncrypt(AESencrpUtils.encryptLong(customer.getCustomerId()));
			logger.debug("I:Step6");
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String idEncrypt, User userSession) throws Exception,GodungIdException{
		logger.debug("I:");
		Customer customerTemp = customerRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		checkGodungId(customerTemp, userSession);
		customerRepository.delete(customerTemp.getCustomerId());
		logger.debug("O:");
	}
	
	public void checkGodungId(Customer customerTemp,User userSession) throws GodungIdException {
		long godungIdTemp = customerTemp.getGodung().getGodungId().longValue();
		long godungIdSession = userSession.getGodung().getGodungId().longValue();
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
	}
}