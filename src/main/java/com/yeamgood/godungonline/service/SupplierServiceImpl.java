package com.yeamgood.godungonline.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Province;
import com.yeamgood.godungonline.model.Supplier;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.ProvinceRepository;
import com.yeamgood.godungonline.repository.SupplierRepository;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("supplierService")
public class SupplierServiceImpl implements SupplierService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SupplierRepository supplierRepository;
	
	@Autowired
	private ProvinceRepository provinceRepository;

	@Override
	public Supplier findById(Long id) {
		logger.debug("I:");
		logger.debug("O:");
		return supplierRepository.findOne(id);
	}

	@Override
	public List<Supplier> findAllOrderBySupplierNameAsc() {
		logger.debug("I:");
		logger.debug("O:");
		return supplierRepository.findAll(sortBySupplierNameAsc());
	}

	@Override
	public List<Supplier> findAllByGodungGodungIdOrderBySupplierNameAsc(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return supplierRepository.findAllByGodungGodungIdOrderBySupplierCodeAsc(godungId);
	}
	
	private Sort sortBySupplierNameAsc() {
        return new Sort(Sort.Direction.ASC, "supplierName");
    }

	@Override
	public long count(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return supplierRepository.countByGodungGodungId(godungId);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Supplier supplier,User user) {
		logger.debug("I:");
		if(supplier.getSupplierId() == null) {
			Supplier maxSupplier = supplierRepository.findTopByGodungGodungIdOrderBySupplierCodeDesc(user.getGodung().getGodungId());
			if(maxSupplier == null) {
				logger.debug("I:Null Max Data");
				maxSupplier = new Supplier();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_SUPPLIER, maxSupplier.getSupplierCode());
			supplier.setSupplierCode(generateCode);
			supplier.setGodung(user.getGodung());
			supplier.setCreate(user.getEmail(), new Date());
			
			// PROVINCE
			Province provinceTemp = supplier.getAddress().getProvince();
			provinceTemp =  provinceRepository.findByProvinceCode(provinceTemp.getProvinceCode());
			supplier.getAddress().setProvince(provinceTemp);
			
			Province provinceSendTemp = supplier.getAddressSend().getProvince();
			provinceSendTemp =  provinceRepository.findByProvinceCode(provinceSendTemp.getProvinceCode());
			supplier.getAddressSend().setProvince(provinceSendTemp);
			
			supplierRepository.save(supplier);
		}else {
			Supplier supplierTemp = supplierRepository.findOne(supplier.getSupplierId());
			supplierTemp.setObject(supplier);
			supplierTemp.setUpdate(user.getEmail(), new Date());
			
			// ADDRESS
			supplierTemp.getAddress().setObject(supplier.getAddress());
			supplierTemp.getAddressSend().setObject(supplier.getAddressSend());
			
			// PROVINCE
			Province provinceTemp = supplier.getAddress().getProvince();
			provinceTemp =  provinceRepository.findByProvinceCode(provinceTemp.getProvinceCode());
			supplierTemp.getAddress().setProvince(provinceTemp);
			
			Province provinceSendTemp = supplier.getAddressSend().getProvince();
			provinceSendTemp =  provinceRepository.findByProvinceCode(provinceSendTemp.getProvinceCode());
			supplierTemp.getAddressSend().setProvince(provinceSendTemp);
			
			supplierRepository.save(supplierTemp);
			logger.debug("I:Step6");
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(Supplier supplier, User user) throws GodungIdException {
		logger.debug("I:");
		Supplier supplierTemp = supplierRepository.findOne(supplier.getSupplierId());
		long godungIdTemp = supplierTemp.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		
		if(godungIdTemp ==  godungIdSession) {
			supplierRepository.delete(supplierTemp);
		}else {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		logger.debug("O:");
	}

	@Override
	public Supplier findById(Long id, User user) throws GodungIdException {
		logger.debug("I:");
		Supplier supplier = supplierRepository.findOne(id);
		long godungIdTemp = supplier.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		
		logger.debug("O:");
		return supplier;
	}
}