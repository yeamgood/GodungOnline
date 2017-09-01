package com.yeamgood.godungonline.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.User;

public interface ProductService {
	public Product findById(Long id);
	public Product findById(Long id,User user) throws GodungIdException;
	public List<Product> findAllOrderByProductNameAsc();
	public List<Product> findAllByGodungGodungIdOrderByProductNameAsc(Long godungId);
	public List<Product> findByGodungGodungIdAndProductNameIgnoreCaseContaining(Long godungId, String product, Pageable pageable);
	public long count(Long godungId);
	public void save(Product product,User user);
	public void delete(Product product,User user) throws GodungIdException;
}