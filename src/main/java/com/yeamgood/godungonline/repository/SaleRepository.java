package com.yeamgood.godungonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Sale;


@Repository("saleRepository")
public interface SaleRepository extends JpaRepository<Sale, Long> {
}
