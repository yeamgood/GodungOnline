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
import com.yeamgood.godungonline.model.Rolegodung;
import com.yeamgood.godungonline.model.Province;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.CountryRepository;
import com.yeamgood.godungonline.repository.RolegodungRepository;
import com.yeamgood.godungonline.repository.ProvinceRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("rolegodungService")
public class RolegodungServiceImpl implements RolegodungService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RolegodungRepository rolegodungRepository;
	
	@Autowired
	private ProvinceRepository provinceRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Override
	public Rolegodung findByIdEncrypt(String idEncrypt,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("O:");
		Rolegodung rolegodungTemp = rolegodungRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		rolegodungTemp.setRolegodungIdEncrypt(idEncrypt);
		checkGodungId(rolegodungTemp, userSession);
		return rolegodungTemp;
	}

	@Override
	public List<Rolegodung> findAllByGodungGodungIdOrderByRolegodungNameAsc(Long godungId) throws Exception {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		List<Rolegodung> rolegodungList = rolegodungRepository.findAllByGodungGodungIdOrderByRolegodungCodeAsc(godungId);
		for (Rolegodung rolegodung : rolegodungList) {
			rolegodung.setRolegodungIdEncrypt(AESencrpUtils.encryptLong(rolegodung.getRolegodungId()));
		}
		return rolegodungList;
	}

	@Override
	public long count(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return rolegodungRepository.countByGodungGodungId(godungId);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Rolegodung rolegodung,User userSession) throws Exception {
		logger.debug("I:");
		logger.debug("I:" +  rolegodung.toString());
		if(StringUtils.isBlank(rolegodung.getRolegodungIdEncrypt())) {
			Rolegodung maxRolegodung = rolegodungRepository.findTopByGodungGodungIdOrderByRolegodungCodeDesc(userSession.getGodung().getGodungId());
			if(maxRolegodung == null) {
				logger.debug("I:Null Max Data");
				maxRolegodung = new Rolegodung();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_EMPLOYEE, maxRolegodung.getRolegodungCode());
			rolegodung.setRolegodungCode(generateCode);
			rolegodung.setGodung(userSession.getGodung());
			rolegodung.setCreate(userSession.getEmail(), new Date());
			
			// PROVINCE
			Province provinceTemp = rolegodung.getAddress().getProvince();
			provinceTemp =  provinceRepository.findByProvinceCode(provinceTemp.getProvinceCode());
			rolegodung.getAddress().setProvince(provinceTemp);
			
			// COUNTRY
			Country countryTemp = rolegodung.getAddress().getCountry();
			countryTemp = countryRepository.findOne(countryTemp.getCountryId());
			rolegodung.getAddress().setCountry(countryTemp);
			
			rolegodung = rolegodungRepository.save(rolegodung);
			rolegodung.setRolegodungIdEncrypt(AESencrpUtils.encryptLong(rolegodung.getRolegodungId()));
		}else {
			Long id = AESencrpUtils.decryptLong(rolegodung.getRolegodungIdEncrypt());
			Rolegodung rolegodungTemp = rolegodungRepository.findOne(id);
			rolegodungTemp.setObject(rolegodung);
			rolegodungTemp.setUpdate(userSession.getEmail(), new Date());
			
			// ADDRESS
			rolegodungTemp.getAddress().setObject(rolegodung.getAddress());
			
			// PROVINCE
			Province provinceTemp = rolegodung.getAddress().getProvince();
			provinceTemp =  provinceRepository.findByProvinceCode(provinceTemp.getProvinceCode());
			rolegodungTemp.getAddress().setProvince(provinceTemp);
			
			// COUNTRY
			Country countryTemp = rolegodung.getAddress().getCountry();
			countryTemp = countryRepository.findOne(countryTemp.getCountryId());
			rolegodungTemp.getAddress().setCountry(countryTemp);
			
			rolegodung = rolegodungRepository.save(rolegodungTemp);
			rolegodung.setRolegodungIdEncrypt(AESencrpUtils.encryptLong(rolegodung.getRolegodungId()));
			logger.debug("I:Step6");
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String idEncrypt, User userSession) throws Exception,GodungIdException{
		logger.debug("I:");
		Rolegodung rolegodungTemp = rolegodungRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		checkGodungId(rolegodungTemp, userSession);
		rolegodungRepository.delete(rolegodungTemp.getRolegodungId());
		logger.debug("O:");
	}
	
	public void checkGodungId(Rolegodung rolegodungTemp,User userSession) throws GodungIdException {
		long godungIdTemp = rolegodungTemp.getGodung().getGodungId().longValue();
		long godungIdSession = userSession.getGodung().getGodungId().longValue();
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
	}
}