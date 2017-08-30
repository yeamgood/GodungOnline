package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Brand;


@Repository("brandRepository")
public interface BrandRepository extends JpaRepository<Brand, Long> {
	public long countByGodungGodungId(Long godungId);
    public Brand findTopByGodungGodungIdOrderByBrandCodeDesc(Long godungId);
	public List<Brand> findAllByGodungGodungIdOrderByBrandNameAsc(Long godungId);
    public List<Brand> findByGodungGodungIdAndBrandNameIgnoreCaseContaining(Long godungId,String brandName,Pageable pageable);

}
