package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Country;


@Repository("countryRepository")
public interface CountryRepository extends JpaRepository<Country, Long> {
	public List<Country> findAllByOrderByCountryNameAsc();
}
