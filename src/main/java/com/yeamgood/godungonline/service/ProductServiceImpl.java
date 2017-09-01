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
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.ProductRepository;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("productService")
public class ProductServiceImpl implements ProductService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Product findById(Long id) {
		logger.debug("I:");
		logger.debug("O:");
		return productRepository.findOne(id);
	}

	@Override
	public List<Product> findAllOrderByProductNameAsc() {
		logger.debug("I:");
		logger.debug("O:");
		return productRepository.findAll(sortByProductNameAsc());
	}

	@Override
	public List<Product> findAllByGodungGodungIdOrderByProductNameAsc(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return productRepository.findAllByGodungGodungIdOrderByProductNameAsc(godungId);
	}
	
	private Sort sortByProductNameAsc() {
        return new Sort(Sort.Direction.ASC, "productName");
    }

	@Override
	public long count(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return productRepository.countByGodungGodungId(godungId);
	}

	@Override
	public List<Product> findByGodungGodungIdAndProductNameIgnoreCaseContaining(Long godungId, String productName, Pageable pageable) {
		return productRepository.findByGodungGodungIdAndProductNameIgnoreCaseContaining(godungId, productName, pageable);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Product product,User user) {
		logger.debug("I:");
		if(product.getProductId() == null) {
			Product maxProduct = productRepository.findTopByGodungGodungIdOrderByProductCodeDesc(user.getGodung().getGodungId());
			if(maxProduct == null) {
				logger.debug("I:Null Max Data");
				maxProduct = new Product();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_PRODUCT, maxProduct.getProductCode());
			
			product.setGodung(user.getGodung());
			product.setCreate(user.getEmail(), new Date());
			product.setProductCode(generateCode);
			product.setGodung(user.getGodung());
			productRepository.save(product);
		}else {
			Product productTemp = productRepository.findOne(product.getProductId());
			productTemp.setProductName(product.getProductName());
			productTemp.setDescription(product.getDescription());
			productTemp.setUpdate(user.getEmail(), new Date());
			productRepository.save(productTemp);
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(Product product, User user) throws GodungIdException {
		logger.debug("I:");
		Product productTemp = productRepository.findOne(product.getProductId());
		long godungIdTemp = productTemp.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		
		if(godungIdTemp ==  godungIdSession) {
			productRepository.delete(productTemp);
		}else {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		logger.debug("O:");
	}

	@Override
	public Product findById(Long id, User user) throws GodungIdException {
		logger.debug("I:");
		Product product = productRepository.findOne(id);
		long godungIdTemp = product.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		
		logger.debug("O:");
		return product;
	}
}