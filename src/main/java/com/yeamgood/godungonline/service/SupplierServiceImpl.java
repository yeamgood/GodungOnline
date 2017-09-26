package com.yeamgood.godungonline.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Country;
import com.yeamgood.godungonline.model.Province;
import com.yeamgood.godungonline.model.Supplier;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.CountryRepository;
import com.yeamgood.godungonline.repository.ProvinceRepository;
import com.yeamgood.godungonline.repository.SupplierRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("supplierService")
public class SupplierServiceImpl implements SupplierService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SupplierRepository supplierRepository;
	
	@Autowired
	private ProvinceRepository provinceRepository;
	
	@Autowired 
	private CountryRepository countryRepository;
	
	@Autowired
	private GodungService godungService;
	
	@Override
	public Supplier findByIdEncrypt(String idEncrypt,User userSession) throws GodungIdException  {
		logger.debug("I:");
		logger.debug("O:");
		Supplier supplier = supplierRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		supplier.encryptData();
		godungService.checkGodungId(supplier.getGodung().getGodungId(), userSession);
		return supplier;
	}

	@Override
	public List<Supplier> findAllByGodungGodungIdOrderBySupplierNameAsc(Long godungId)  {
		logger.debug(Constants.LOG_INPUT, godungId);
		logger.debug("O:");
		List<Supplier> supplierList = supplierRepository.findAllByGodungGodungIdOrderBySupplierCodeAsc(godungId);
		for (Supplier supplier : supplierList) {
			supplier.setSupplierIdEncrypt(AESencrpUtils.encryptLong(supplier.getSupplierId()));
			supplier.encryptData();
		}
		return supplierList;
	}

	@Override
	public long count(Long godungId) {
		logger.debug(Constants.LOG_INPUT, godungId);
		logger.debug("O:");
		return supplierRepository.countByGodungGodungId(godungId);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Supplier supplier,User userSession)  {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, supplier);
		if(StringUtils.isBlank(supplier.getSupplierIdEncrypt())) {
			Supplier maxSupplier = supplierRepository.findTopByGodungGodungIdOrderBySupplierCodeDesc(userSession.getGodung().getGodungId());
			if(maxSupplier == null) {
				logger.debug("I:Null Max Data");
				maxSupplier = new Supplier();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_SUPPLIER, maxSupplier.getSupplierCode());
			supplier.setSupplierCode(generateCode);
			supplier.setGodung(userSession.getGodung());
			supplier.setCreate(userSession);
			
			// PROVINCE
			Province provinceTemp = supplier.getAddress().getProvince();
			provinceTemp =  provinceRepository.findByProvinceCode(provinceTemp.getProvinceCode());
			supplier.getAddress().setProvince(provinceTemp);
			// COUNTRY
			Country countryTemp = supplier.getAddress().getCountry();
			countryTemp = countryRepository.findOne(countryTemp.getCountryId());
			supplier.getAddress().setCountry(countryTemp);
			
			// PROVINCE SEND
			Province provinceSendTemp = supplier.getAddressSend().getProvince();
			provinceSendTemp =  provinceRepository.findByProvinceCode(provinceSendTemp.getProvinceCode());
			supplier.getAddressSend().setProvince(provinceSendTemp);
			// COUNTRY SEND
			Country countrySendTemp = supplier.getAddressSend().getCountry();
			countrySendTemp = countryRepository.findOne(countrySendTemp.getCountryId());
			supplier.getAddressSend().setCountry(countrySendTemp);
			
			supplierRepository.save(supplier);
			supplier.setSupplierIdEncrypt(AESencrpUtils.encryptLong(supplier.getSupplierId()));
		}else {
			Long id = AESencrpUtils.decryptLong(supplier.getSupplierIdEncrypt());
			Supplier supplierTemp = supplierRepository.findOne(id);
			supplierTemp.setObject(supplier);
			supplierTemp.setUpdate(userSession);
			
			// ADDRESS
			supplierTemp.getAddress().setObject(supplier.getAddress());
			supplierTemp.getAddressSend().setObject(supplier.getAddressSend());
			
			// PROVINCE
			Province provinceTemp = supplier.getAddress().getProvince();
			provinceTemp =  provinceRepository.findByProvinceCode(provinceTemp.getProvinceCode());
			supplierTemp.getAddress().setProvince(provinceTemp);
			// COUNTRY
			Country countryTemp = supplier.getAddress().getCountry();
			countryTemp = countryRepository.findOne(countryTemp.getCountryId());
			supplierTemp.getAddress().setCountry(countryTemp);
			
			// PROVINCE SEND
			Province provinceSendTemp = supplier.getAddressSend().getProvince();
			provinceSendTemp =  provinceRepository.findByProvinceCode(provinceSendTemp.getProvinceCode());
			supplierTemp.getAddressSend().setProvince(provinceSendTemp);
			// COUNTRY SEND
			Country countrySendTemp = supplier.getAddressSend().getCountry();
			countrySendTemp = countryRepository.findOne(countrySendTemp.getCountryId());
			supplierTemp.getAddressSend().setCountry(countrySendTemp);
			
			supplierRepository.save(supplierTemp);
			supplier.setSupplierIdEncrypt(AESencrpUtils.encryptLong(supplierTemp.getSupplierId()));
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String idEncrypt, User userSession) throws GodungIdException{
		logger.debug("I:");
		Supplier supplierTemp = supplierRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		godungService.checkGodungId(supplierTemp.getGodung().getGodungId(), userSession);
		supplierRepository.delete(supplierTemp.getSupplierId());
		logger.debug("O:");
	}
	
}