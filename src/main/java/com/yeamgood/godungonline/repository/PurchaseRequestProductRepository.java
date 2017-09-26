package com.yeamgood.godungonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.PurchaseRequestProduct;


@Repository("purchaseRequestProductRepository")
public interface PurchaseRequestProductRepository extends JpaRepository<PurchaseRequestProduct, Long> {
}
