package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Province;


@Repository("provinceRepository")
public interface ProvinceRepository extends JpaRepository<Province, Long> {
	public List<Province> findAllByOrderByProvinceNameAsc();
	public Province findByProvinceCode(String provinceCode);
}
