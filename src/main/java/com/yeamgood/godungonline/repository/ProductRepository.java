package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Product;


@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Long> {
	public long countByGodungGodungId(Long godungId);
    public Product findTopByGodungGodungIdOrderByProductCodeDesc(Long godungId);
	public List<Product> findAllByGodungGodungIdOrderByProductNameAsc(Long godungId);
    public List<Product> findByGodungGodungIdAndProductNameIgnoreCaseContaining(Long godungId,String productName,Pageable pageable);

}
