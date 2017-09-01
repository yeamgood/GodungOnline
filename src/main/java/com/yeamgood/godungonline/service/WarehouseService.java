package com.yeamgood.godungonline.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Warehouse;
import com.yeamgood.godungonline.model.User;

public interface WarehouseService {
	public Warehouse findById(Long id);
	public Warehouse findById(Long id,User user) throws GodungIdException;
	public List<Warehouse> findAllOrderByWarehouseNameAsc();
	public List<Warehouse> findAllByGodungGodungIdOrderByWarehouseNameAsc(Long godungId);
	public List<Warehouse> findByGodungGodungIdAndWarehouseNameIgnoreCaseContaining(Long godungId, String warehouse, Pageable pageable);
	public long count(Long godungId);
	public void save(Warehouse warehouse,User user);
	public void delete(Warehouse warehouse,User user) throws GodungIdException;
}