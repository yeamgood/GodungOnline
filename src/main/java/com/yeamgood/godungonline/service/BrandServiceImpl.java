package com.yeamgood.godungonline.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Brand;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.BrandRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("brandService")
public class BrandServiceImpl implements BrandService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BrandRepository brandRepository;

	@Override
	public Brand findByIdEncrypt(String idEncrypt) throws Exception {
		logger.debug("I:");
		Brand brand = brandRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		brand.encryptData(brand);
		logger.debug("O:");
		return brand;
	}
	
	@Override
	public Brand findByIdEncrypt(String idEncrypt, User userSession) throws Exception {
		logger.debug("I:");
		Brand brand = brandRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		checkGodungId(brand, userSession);
		brand.encryptData(brand);
		logger.debug("O:");
		return brand;
	}

	@Override
	public List<Brand> findAllOrderByBrandNameAsc() throws Exception {
		logger.debug("I:");
		List<Brand> brandList = brandRepository.findAll(sortByBrandNameAsc());
		for (Brand brand : brandList) {
			brand.setBrandIdEncrypt(AESencrpUtils.encryptLong(brand.getBrandId()));
			brand.encryptData(brand);
		}
		logger.debug("O:");
		return brandList;
	}

	@Override
	public List<Brand> findAllByGodungGodungIdOrderByBrandNameAsc(Long godungId) throws Exception {
		logger.debug("I:[godungId]:" + godungId);
		List<Brand> brandList = brandRepository.findAllByGodungGodungIdOrderByBrandNameAsc(godungId);
		for (Brand brand : brandList) {
			brand.setBrandIdEncrypt(AESencrpUtils.encryptLong(brand.getBrandId()));
			brand.encryptData(brand);
		}
		logger.debug("O:");
		return brandList;
	}
	
	private Sort sortByBrandNameAsc() {
        return new Sort(Sort.Direction.ASC, "brandName");
    }

	@Override
	public long count(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return brandRepository.countByGodungGodungId(godungId);
	}

	@Override
	public List<Brand> findByGodungGodungIdAndBrandNameIgnoreCaseContaining(Long godungId, String brandName, Pageable pageable) throws Exception {
		logger.debug("I:");
		List<Brand> brandList = brandRepository.findByGodungGodungIdAndBrandNameIgnoreCaseContaining(godungId, brandName, pageable);
		for (Brand brand : brandList) {
			brand.setBrandIdEncrypt(AESencrpUtils.encryptLong(brand.getBrandId()));
			brand.encryptData(brand);
		}
		logger.debug("O:");
		return brandList;
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Brand brand,User user) throws Exception {
		logger.debug("I:");
		if(StringUtils.isBlank(brand.getBrandIdEncrypt())) {
			Brand maxBrand = brandRepository.findTopByGodungGodungIdOrderByBrandCodeDesc(user.getGodung().getGodungId());
			if(maxBrand == null) {
				logger.debug("I:Null Max Data");
				maxBrand = new Brand();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_BRAND, maxBrand.getBrandCode());
			brand.setGodung(user.getGodung());
			brand.setCreate(user.getEmail(), new Date());
			brand.setBrandCode(generateCode);
			brand.setGodung(user.getGodung());
			brandRepository.save(brand);
		}else {
			Brand brandTemp = brandRepository.findOne(AESencrpUtils.decryptLong(brand.getBrandIdEncrypt()));
			brandTemp.setBrandName(brand.getBrandName());
			brandTemp.setDescription(brand.getDescription());
			brandTemp.setUpdate(user.getEmail(), new Date());
			brandRepository.save(brandTemp);
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(Brand brand, User user) throws Exception{
		logger.debug("I:");
		Brand brandTemp = brandRepository.findOne(AESencrpUtils.decryptLong(brand.getBrandIdEncrypt()));
		checkGodungId(brandTemp, user);
		brandRepository.delete(brandTemp);
		logger.debug("O:");
	}

	public void checkGodungId(Brand brand,User userSession) throws GodungIdException {
		long godungIdTemp = brand.getGodung().getGodungId().longValue();
		long godungIdSession = userSession.getGodung().getGodungId().longValue();
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
	}
	
}