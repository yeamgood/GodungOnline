package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Warehouse;


@Repository("warehouseRepository")
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
	public long countByGodungGodungId(Long godungId);
    public Warehouse findTopByGodungGodungIdOrderByWarehouseCodeDesc(Long godungId);
	public List<Warehouse> findAllByGodungGodungIdOrderByWarehouseNameAsc(Long godungId);
    public List<Warehouse> findByGodungGodungIdAndWarehouseNameIgnoreCaseContaining(Long godungId,String warehouseName,Pageable pageable);

}
