package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Currency;


@Repository("currencyRepository")
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
	public List<Currency> findAllByOrderByCurrencyNameAsc();
	public Currency findByCurrencyCode(String currencyCode);
}
