package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Location;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.model.Warehouse;

public interface WarehouseService {
	public Warehouse findByIdEncrypt(String idEncrypt,User userSession) throws Exception;
	public List<Warehouse> findAllByGodungGodungIdOrderByWarehouseNameAsc(Long godungId) throws Exception;
	public long count(Long godungId);
	public void save(Warehouse warehouse,User userSession) throws Exception;
	public void delete(String idEncrypt,User userSession) throws Exception , GodungIdException;
	public void deleteLocation(String warehouseIdEncrypt,String locationIdEncrypt,User userSession) throws Exception , GodungIdException;
	public void saveLocation(String warehouseIdEncrypt,List<Location> locationList,User userSession) throws Exception;
}