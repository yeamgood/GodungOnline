package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Supplier;


@Repository("supplierRepository")
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
	public long countByGodungGodungId(Long godungId);
    public Supplier findTopByGodungGodungIdOrderBySupplierCodeDesc(Long godungId);
	public List<Supplier> findAllByGodungGodungIdOrderBySupplierCodeAsc(Long godungId);
}
