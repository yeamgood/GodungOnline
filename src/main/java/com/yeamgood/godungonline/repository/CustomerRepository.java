package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Customer;


@Repository("customerRepository")
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	public long countByGodungGodungId(Long godungId);
    public Customer findTopByGodungGodungIdOrderByCustomerCodeDesc(Long godungId);
	public List<Customer> findAllByGodungGodungIdOrderByCustomerNameAsc(Long godungId);
    public List<Customer> findByGodungGodungIdAndCustomerNameIgnoreCaseContaining(Long godungId,String customerName,Pageable pageable);

}
