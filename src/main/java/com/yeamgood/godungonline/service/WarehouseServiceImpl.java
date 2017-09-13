package com.yeamgood.godungonline.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Location;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.model.Warehouse;
import com.yeamgood.godungonline.repository.LocationRepository;
import com.yeamgood.godungonline.repository.WarehouseRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("warehouseService")
public class WarehouseServiceImpl implements WarehouseService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WarehouseRepository warehouseRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Override
	public Warehouse findByIdEncrypt(String idEncrypt,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("O:");
		Warehouse warehouseTemp = warehouseRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		warehouseTemp.setWarehouseIdEncrypt(idEncrypt);
		checkGodungId(warehouseTemp, userSession);
		return warehouseTemp;
	}

	@Override
	public List<Warehouse> findAllByGodungGodungIdOrderByWarehouseNameAsc(Long godungId) throws Exception {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		List<Warehouse> warehouseList = warehouseRepository.findAllByGodungGodungIdOrderByWarehouseCodeAsc(godungId);
		for (Warehouse warehouse : warehouseList) {
			warehouse.setWarehouseIdEncrypt(AESencrpUtils.encryptLong(warehouse.getWarehouseId()));
		}
		return warehouseList;
	}

	@Override
	public long count(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return warehouseRepository.countByGodungGodungId(godungId);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Warehouse warehouse,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("I:" +  warehouse.toString());
		
		if(warehouse.getLocationList() == null) {
			warehouse.setLocationList(new ArrayList<Location>());
		}
		
		List<Location> locationTempList = new ArrayList<Location>();
		for (Location locationTemp : warehouse.getLocationList()) {
			if(StringUtils.isNotBlank(locationTemp.getLocationCode())) {
				locationTempList.add(locationTemp);
			}
		}
		
		if(StringUtils.isBlank(warehouse.getWarehouseIdEncrypt())) {
			Warehouse maxWarehouse = warehouseRepository.findTopByGodungGodungIdOrderByWarehouseCodeDesc(userSession.getGodung().getGodungId());
			if(maxWarehouse == null) {
				logger.debug("I:Null Max Data");
				maxWarehouse = new Warehouse();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_WAREHOUSE , maxWarehouse.getWarehouseCode());
			warehouse.setWarehouseCode(generateCode);
			warehouse.setGodung(userSession.getGodung());
			warehouse.setCreate(userSession.getEmail(), new Date());
			warehouse.getLocationList().clear();
			warehouse.getLocationList().addAll(locationTempList);
			warehouse = warehouseRepository.save(warehouse);
			warehouse.setWarehouseIdEncrypt(AESencrpUtils.encryptLong(warehouse.getWarehouseId()));
		}else {
			Long id = AESencrpUtils.decryptLong(warehouse.getWarehouseIdEncrypt());
			Warehouse warehouseTemp = warehouseRepository.findOne(id);
			warehouseTemp.setObject(warehouse);
			warehouseTemp.setUpdate(userSession.getEmail(), new Date());
			warehouseTemp.getLocationList().clear();
			warehouseTemp.getLocationList().addAll(locationTempList);
			warehouse = warehouseRepository.save(warehouseTemp);
			warehouse.setWarehouseIdEncrypt(AESencrpUtils.encryptLong(warehouse.getWarehouseId()));
			logger.debug("I:Step6");
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String idEncrypt, User userSession) throws Exception,GodungIdException{
		logger.debug("I:");
		Warehouse warehouseTemp = warehouseRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		checkGodungId(warehouseTemp, userSession);
		warehouseRepository.delete(warehouseTemp.getWarehouseId());
		logger.debug("O:");
	}
	
	public void checkGodungId(Warehouse warehouseTemp,User userSession) throws GodungIdException {
		long godungIdTemp = warehouseTemp.getGodung().getGodungId().longValue();
		long godungIdSession = userSession.getGodung().getGodungId().longValue();
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void saveLocation(String warehouseIdEncrypt,List<Location> locationList,User userSession) throws Exception {
		logger.debug("I:");
		Warehouse warehouseTemp = warehouseRepository.findOne(AESencrpUtils.decryptLong(warehouseIdEncrypt));
		
		Location locaitonTemp;
		for (Location location : locationList) {
			if(StringUtils.isBlank(location.getLocationIdEncrypt())) {
				location.setCreate(userSession.getEmail(), new Date());
				warehouseTemp.getLocationList().add(location);
			}else {
				locaitonTemp = locationRepository.findOne(AESencrpUtils.decryptLong(location.getLocationIdEncrypt()));
				locaitonTemp.setObject(location);
				locaitonTemp.setUpdate(userSession.getEmail(), new Date());
				locationRepository.save(locaitonTemp);
			}
		}
		warehouseRepository.save(warehouseTemp);
		logger.debug("O:");
	}

	@Override
	public void deleteLocation(String warehouseIdEncrypt, String locationIdEncrypt, User userSession)throws Exception, GodungIdException {
		logger.debug("I:");
		Warehouse warehouseTemp = warehouseRepository.findOne(AESencrpUtils.decryptLong(warehouseIdEncrypt));
		Long locationId = AESencrpUtils.decryptLong(locationIdEncrypt);
		checkGodungId(warehouseTemp, userSession);
		for (Location locationTemp : warehouseTemp.getLocationList()) {
			if(locationTemp.getLocationId() == locationId) {
				warehouseTemp.getLocationList().remove(locationTemp);
				break;
			}
		}
		warehouseRepository.save(warehouseTemp);
		logger.debug("O:");
	}
}