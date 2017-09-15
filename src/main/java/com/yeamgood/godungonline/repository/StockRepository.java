package com.yeamgood.godungonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Stock;


@Repository("stockRepository")
public interface StockRepository extends JpaRepository<Stock, Long> {
}
