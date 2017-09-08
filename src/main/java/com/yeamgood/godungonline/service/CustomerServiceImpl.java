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
import com.yeamgood.godungonline.model.Customer;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.CustomerRepository;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Customer findById(Long id) {
		logger.debug("I:");
		logger.debug("O:");
		return customerRepository.findOne(id);
	}

	@Override
	public List<Customer> findAllOrderByCustomerNameAsc() {
		logger.debug("I:");
		logger.debug("O:");
		return customerRepository.findAll(sortByCustomerNameAsc());
	}

	@Override
	public List<Customer> findAllByGodungGodungIdOrderByCustomerNameAsc(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return customerRepository.findAllByGodungGodungIdOrderByCustomerNameAsc(godungId);
	}
	
	private Sort sortByCustomerNameAsc() {
        return new Sort(Sort.Direction.ASC, "customerName");
    }

	@Override
	public long count(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return customerRepository.countByGodungGodungId(godungId);
	}

	@Override
	public List<Customer> findByGodungGodungIdAndCustomerNameIgnoreCaseContaining(Long godungId, String customerName, Pageable pageable) {
		return customerRepository.findByGodungGodungIdAndCustomerNameIgnoreCaseContaining(godungId, customerName, pageable);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Customer customer,User user) {
		logger.debug("I:");
		if(customer.getCustomerId() == null) {
			Customer maxCustomer = customerRepository.findTopByGodungGodungIdOrderByCustomerCodeDesc(user.getGodung().getGodungId());
			if(maxCustomer == null) {
				logger.debug("I:Null Max Data");
				maxCustomer = new Customer();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_CUSTOMER, maxCustomer.getCustomerCode());
			
			customer.setGodung(user.getGodung());
			customer.setCreate(user.getEmail(), new Date());
			customer.setCustomerCode(generateCode);
			customer.setGodung(user.getGodung());
			customerRepository.save(customer);
		}else {
			Customer customerTemp = customerRepository.findOne(customer.getCustomerId());
			customerTemp.setCustomerName(customer.getCustomerName());
			customerTemp.setDescription(customer.getDescription());
			customerTemp.setUpdate(user.getEmail(), new Date());
			customerRepository.save(customerTemp);
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(Customer customer, User user) throws GodungIdException {
		logger.debug("I:");
		Customer customerTemp = customerRepository.findOne(customer.getCustomerId());
		long godungIdTemp = customerTemp.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		
		if(godungIdTemp ==  godungIdSession) {
			customerRepository.delete(customerTemp);
		}else {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		logger.debug("O:");
	}

	@Override
	public Customer findById(Long id, User user) throws GodungIdException {
		logger.debug("I:");
		Customer customer = customerRepository.findOne(id);
		long godungIdTemp = customer.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		
		logger.debug("O:");
		return customer;
	}
}