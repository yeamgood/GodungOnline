package com.yeamgood.godungonline.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Warehouse;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.WarehouseRepository;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("warehouseService")
public class WarehouseServiceImpl implements WarehouseService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WarehouseRepository warehouseRepository;

	@Override
	public Warehouse findById(Long id) {
		logger.debug("I:");
		logger.debug("O:");
		return warehouseRepository.findOne(id);
	}

	@Override
	public List<Warehouse> findAllOrderByWarehouseNameAsc() {
		logger.debug("I:");
		logger.debug("O:");
		return warehouseRepository.findAll(sortByWarehouseNameAsc());
	}

	@Override
	public List<Warehouse> findAllByGodungGodungIdOrderByWarehouseNameAsc(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return warehouseRepository.findAllByGodungGodungIdOrderByWarehouseNameAsc(godungId);
	}
	
	private Sort sortByWarehouseNameAsc() {
        return new Sort(Sort.Direction.ASC, "warehouseName");
    }

	@Override
	public long count(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return warehouseRepository.countByGodungGodungId(godungId);
	}

	@Override
	public List<Warehouse> findByGodungGodungIdAndWarehouseNameIgnoreCaseContaining(Long godungId, String warehouseName, Pageable pageable) {
		return warehouseRepository.findByGodungGodungIdAndWarehouseNameIgnoreCaseContaining(godungId, warehouseName, pageable);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Warehouse warehouse,User user) {
		logger.debug("I:");
		if(warehouse.getWarehouseId() == null) {
			Warehouse maxWarehouse = warehouseRepository.findTopByGodungGodungIdOrderByWarehouseCodeDesc(user.getGodung().getGodungId());
			if(maxWarehouse == null) {
				logger.debug("I:Null Max Data");
				maxWarehouse = new Warehouse();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_WAREHOUSE, maxWarehouse.getWarehouseCode());
			
			warehouse.setGodung(user.getGodung());
			warehouse.setCreate(user.getEmail(), new Date());
			warehouse.setWarehouseCode(generateCode);
			warehouse.setGodung(user.getGodung());
			warehouseRepository.save(warehouse);
		}else {
			Warehouse warehouseTemp = warehouseRepository.findOne(warehouse.getWarehouseId());
			warehouseTemp.setWarehouseName(warehouse.getWarehouseName());
			warehouseTemp.setDescription(warehouse.getDescription());
			warehouseTemp.setUpdate(user.getEmail(), new Date());
			warehouseRepository.save(warehouseTemp);
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(Warehouse warehouse, User user) throws GodungIdException {
		logger.debug("I:");
		Warehouse warehouseTemp = warehouseRepository.findOne(warehouse.getWarehouseId());
		long godungIdTemp = warehouseTemp.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		
		if(godungIdTemp ==  godungIdSession) {
			warehouseRepository.delete(warehouseTemp);
		}else {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		logger.debug("O:");
	}

	@Override
	public Warehouse findById(Long id, User user) throws GodungIdException {
		logger.debug("I:");
		Warehouse warehouse = warehouseRepository.findOne(id);
		long godungIdTemp = warehouse.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		
		logger.debug("O:");
		return warehouse;
	}
}