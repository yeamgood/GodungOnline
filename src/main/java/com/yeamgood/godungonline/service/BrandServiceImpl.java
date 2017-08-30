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
import com.yeamgood.godungonline.model.Brand;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.BrandRepository;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("brandService")
public class BrandServiceImpl implements BrandService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BrandRepository brandRepository;

	@Override
	public Brand findById(Long id) {
		logger.debug("I:");
		logger.debug("O:");
		return brandRepository.findOne(id);
	}

	@Override
	public List<Brand> findAllOrderByBrandNameAsc() {
		logger.debug("I:");
		logger.debug("O:");
		return brandRepository.findAll(sortByBrandNameAsc());
	}

	@Override
	public List<Brand> findAllByGodungGodungIdOrderByBrandNameAsc(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return brandRepository.findAllByGodungGodungIdOrderByBrandNameAsc(godungId);
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
	public List<Brand> findByGodungGodungIdAndBrandNameIgnoreCaseContaining(Long godungId, String brandName, Pageable pageable) {
		return brandRepository.findByGodungGodungIdAndBrandNameIgnoreCaseContaining(godungId, brandName, pageable);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Brand brand,User user) {
		logger.debug("I:");
		if(brand.getBrandId() == null) {
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
			Brand brandTemp = brandRepository.findOne(brand.getBrandId());
			brandTemp.setBrandName(brand.getBrandName());
			brandTemp.setDescription(brand.getDescription());
			brandTemp.setUpdate(user.getEmail(), new Date());
			brandRepository.save(brandTemp);
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(Brand brand, User user) throws GodungIdException {
		logger.debug("I:");
		Brand brandTemp = brandRepository.findOne(brand.getBrandId());
		long godungIdTemp = brandTemp.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		
		if(godungIdTemp ==  godungIdSession) {
			brandRepository.delete(brandTemp);
		}else {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		logger.debug("O:");
	}

	@Override
	public Brand findById(Long id, User user) throws GodungIdException {
		logger.debug("I:");
		Brand brand = brandRepository.findOne(id);
		long godungIdTemp = brand.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		
		logger.debug("O:");
		return brand;
	}
}