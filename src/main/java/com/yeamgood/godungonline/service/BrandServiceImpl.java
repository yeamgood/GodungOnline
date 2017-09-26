package com.yeamgood.godungonline.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	private GodungService godungService;

	@Override
	public Brand findByIdEncrypt(String brandIdEncrypt){
		logger.debug("I");
		logger.debug("brandIdEncrypt:{}",brandIdEncrypt);
		Brand brand = brandRepository.findOne(AESencrpUtils.decryptLong(brandIdEncrypt));
		brand.encryptData();
		logger.debug("O");
		return brand;
	}
	
	@Override
	public Brand findByIdEncrypt(String brandIdEncrypt, User userSession) throws GodungIdException {
		logger.debug("I");
		logger.debug("brandIdEncrypt:{}",brandIdEncrypt);
		Brand brand = brandRepository.findOne(AESencrpUtils.decryptLong(brandIdEncrypt));
		godungService.checkGodungId(brand.getGodung().getGodungId(), userSession);
		brand.encryptData();
		logger.debug("O");
		return brand;
	}

	@Override
	public List<Brand> findAllOrderByBrandNameAsc()  {
		logger.debug("I");
		List<Brand> brandList = brandRepository.findAll(new Sort(Sort.Direction.ASC, "brandName"));
		for (Brand brand : brandList) {
			brand.setBrandIdEncrypt(AESencrpUtils.encryptLong(brand.getBrandId()));
			brand.encryptData();
		}
		logger.debug("O");
		return brandList;
	}

	@Override
	public List<Brand> findAllByGodungGodungIdOrderByBrandNameAsc(Long godungId)  {
		logger.debug("I");
		logger.debug("godungId:{}",godungId);
		List<Brand> brandList = brandRepository.findAllByGodungGodungIdOrderByBrandNameAsc(godungId);
		for (Brand brand : brandList) {
			brand.setBrandIdEncrypt(AESencrpUtils.encryptLong(brand.getBrandId()));
			brand.encryptData();
		}
		logger.debug("O");
		return brandList;
	}

	@Override
	public long count(Long godungId) {
		logger.debug("I");
		logger.debug("godungId:{}",godungId);
		logger.debug("O");
		return brandRepository.countByGodungGodungId(godungId);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Brand brand,User user)  {
		logger.debug("I");
		logger.debug("brand:{}",brand);
		logger.debug("user:{}",user);
		if(StringUtils.isBlank(brand.getBrandIdEncrypt())) {
			Brand maxBrand = brandRepository.findTopByGodungGodungIdOrderByBrandCodeDesc(user.getGodung().getGodungId());
			if(maxBrand == null) {
				logger.debug("I:Null Max Data");
				maxBrand = new Brand();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_BRAND, maxBrand.getBrandCode());
			brand.setGodung(user.getGodung());
			brand.setCreate(user);
			brand.setBrandCode(generateCode);
			brand.setGodung(user.getGodung());
			brandRepository.save(brand);
		}else {
			Brand brandTemp = brandRepository.findOne(AESencrpUtils.decryptLong(brand.getBrandIdEncrypt()));
			brandTemp.setBrandName(brand.getBrandName());
			brandTemp.setDescription(brand.getDescription());
			brandTemp.setUpdate(user);
			brandRepository.save(brandTemp);
		}
		logger.debug("O");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(Brand brand, User user) throws GodungIdException {
		logger.debug("I");
		logger.debug("brand:{}",brand);
		logger.debug("user:{}",user);
		Brand brandTemp = brandRepository.findOne(AESencrpUtils.decryptLong(brand.getBrandIdEncrypt()));
		godungService.checkGodungId(brandTemp.getGodung().getGodungId(), user);
		brandRepository.delete(brandTemp);
		logger.debug("O");
	}

}