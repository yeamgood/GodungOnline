package com.yeamgood.godungonline.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	@Override
	public Supplier findByIdEncrypt(String idEncrypt,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("O:");
		Supplier supplier = supplierRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		supplier.encryptData(supplier);
		checkGodungId(supplier, userSession);
		return supplier;
	}

	@Override
	public List<Supplier> findAllByGodungGodungIdOrderBySupplierNameAsc(Long godungId) throws Exception {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		List<Supplier> supplierList = supplierRepository.findAllByGodungGodungIdOrderBySupplierCodeAsc(godungId);
		for (Supplier supplier : supplierList) {
			supplier.setSupplierIdEncrypt(AESencrpUtils.encryptLong(supplier.getSupplierId()));
			supplier.encryptData(supplier);
		}
		return supplierList;
	}

	@Override
	public long count(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return supplierRepository.countByGodungGodungId(godungId);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Supplier supplier,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("I:" +  supplier.toString());
		if(StringUtils.isBlank(supplier.getSupplierIdEncrypt())) {
			Supplier maxSupplier = supplierRepository.findTopByGodungGodungIdOrderBySupplierCodeDesc(userSession.getGodung().getGodungId());
			if(maxSupplier == null) {
				logger.debug("I:Null Max Data");
				maxSupplier = new Supplier();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_SUPPLIER, maxSupplier.getSupplierCode());
			supplier.setSupplierCode(generateCode);
			supplier.setGodung(userSession.getGodung());
			supplier.setCreate(userSession.getEmail(), new Date());
			
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
			
			supplier = supplierRepository.save(supplier);
			supplier.setSupplierIdEncrypt(AESencrpUtils.encryptLong(supplier.getSupplierId()));
		}else {
			Long id = AESencrpUtils.decryptLong(supplier.getSupplierIdEncrypt());
			Supplier supplierTemp = supplierRepository.findOne(id);
			supplierTemp.setObject(supplier);
			supplierTemp.setUpdate(userSession.getEmail(), new Date());
			
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
			
			supplier = supplierRepository.save(supplierTemp);
			supplier.setSupplierIdEncrypt(AESencrpUtils.encryptLong(supplier.getSupplierId()));
			logger.debug("I:Step6");
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String idEncrypt, User userSession) throws Exception,GodungIdException{
		logger.debug("I:");
		Supplier supplierTemp = supplierRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		checkGodungId(supplierTemp, userSession);
		supplierRepository.delete(supplierTemp.getSupplierId());
		logger.debug("O:");
	}
	
	public void checkGodungId(Supplier supplierTemp,User userSession) throws GodungIdException {
		long godungIdTemp = supplierTemp.getGodung().getGodungId().longValue();
		long godungIdSession = userSession.getGodung().getGodungId().longValue();
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
	}
}