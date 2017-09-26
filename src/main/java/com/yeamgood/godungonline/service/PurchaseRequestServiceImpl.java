package com.yeamgood.godungonline.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.form.PurchaseRequestForm;
import com.yeamgood.godungonline.form.PurchaseRequestProductForm;
import com.yeamgood.godungonline.model.Employee;
import com.yeamgood.godungonline.model.Measure;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.PurchaseRequest;
import com.yeamgood.godungonline.model.PurchaseRequestProduct;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.EmployeeRepository;
import com.yeamgood.godungonline.repository.MeasureRepository;
import com.yeamgood.godungonline.repository.ProductRepository;
import com.yeamgood.godungonline.repository.PurchaseRequestProductRepository;
import com.yeamgood.godungonline.repository.PurchaseRequestRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.DateUtils;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;
import com.yeamgood.godungonline.utils.NumberUtils;

@Service("purchaseRequestService")
public class PurchaseRequestServiceImpl implements PurchaseRequestService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PurchaseRequestRepository purchaseRequestRepository;
	
	@Autowired
	private PurchaseRequestProductRepository purchaseRequestProductRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private GodungService godungService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private MeasureRepository measureRepository;
	
	@Override
	public PurchaseRequest findByIdEncrypt(String idEncrypt,User userSession) throws GodungIdException  {
		logger.debug("I:");
		logger.debug("O:");
		PurchaseRequest purchaseRequest = purchaseRequestRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		purchaseRequest.encryptData();
		godungService.checkGodungId(purchaseRequest.getGodung().getGodungId(), userSession);
		return purchaseRequest;
	}

	@Override
	public List<PurchaseRequest> findAllByGodungGodungIdOrderByPurchaseRequestNameAsc(Long godungId)  {
		logger.debug(Constants.LOG_INPUT, godungId);
		logger.debug("O:");
		List<PurchaseRequest> purchaseRequestList = purchaseRequestRepository.findAllByGodungGodungIdOrderByPurchaseRequestCodeAsc(godungId);
		for (PurchaseRequest purchaseRequest : purchaseRequestList) {
			purchaseRequest.setPurchaseRequestIdEncrypt(AESencrpUtils.encryptLong(purchaseRequest.getPurchaseRequestId()));
			purchaseRequest.encryptData();
		}
		return purchaseRequestList;
	}

	@Override
	public long count(Long godungId) {
		logger.debug(Constants.LOG_INPUT, godungId);
		logger.debug("O:");
		return purchaseRequestRepository.countByGodungGodungId(godungId);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(PurchaseRequestForm purchaseRequestForm,User userSession) throws ParseException  {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, purchaseRequestForm);
		
		PurchaseRequest purchaseRequestTemp = new PurchaseRequest();
		if(StringUtils.isBlank(purchaseRequestForm.getPurchaseRequestIdEncrypt())) {
			PurchaseRequest maxPurchaseRequest = purchaseRequestRepository.findTopByGodungGodungIdOrderByPurchaseRequestCodeDesc(userSession.getGodung().getGodungId());
			if(maxPurchaseRequest == null) {
				logger.debug("I:Null Max Data");
				maxPurchaseRequest = new PurchaseRequest();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_PURCHASE_REQUEST , maxPurchaseRequest.getPurchaseRequestCode());
			purchaseRequestTemp.setPurchaseRequestCode(generateCode);
			purchaseRequestTemp.setGodung(userSession.getGodung());
			purchaseRequestTemp.setCreate(userSession);
			purchaseRequestRepository.save(purchaseRequestTemp);
			purchaseRequestForm.setPurchaseRequestIdEncrypt(AESencrpUtils.encryptLong(purchaseRequestTemp.getPurchaseRequestId()));
		}else {
			Long id = AESencrpUtils.decryptLong(purchaseRequestForm.getPurchaseRequestIdEncrypt());
			purchaseRequestTemp = purchaseRequestRepository.findOne(id);
			purchaseRequestTemp.setUpdate(userSession);
		}
		
		Employee employee = null;
		if(!StringUtils.isBlank(purchaseRequestForm.getEmployeeIdEncrypt())) {
			employee = employeeRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestForm.getEmployeeIdEncrypt()));
		}
		
		purchaseRequestTemp.setEmployee(employee);
		purchaseRequestTemp.setRequestDate(DateUtils.stringToDate(purchaseRequestForm.getRequestDate(), DateUtils.DDMMYYYY));
		purchaseRequestTemp.setDemandDate(DateUtils.stringToDate(purchaseRequestForm.getDemandDate(), DateUtils.DDMMYYYY));
		purchaseRequestTemp.setReferenceNumber(purchaseRequestForm.getReferenceNumber());
		purchaseRequestTemp.setDescription(purchaseRequestForm.getDescription());
		
		
		
		
		
		//Manage List
		PurchaseRequestProduct purchaseRequestProduct;
		List<PurchaseRequestProduct> prProductFormListTemp = new ArrayList<>();
		if(purchaseRequestForm.getPurchaseRequestProductFormList() != null) {
			for (PurchaseRequestProductForm purchaseRequestProductForm : purchaseRequestForm.getPurchaseRequestProductFormList()) {
				if(!StringUtils.isBlank(purchaseRequestProductForm.getProductIdEncrypt())) {// check delete row null pointer
					
					if(StringUtils.isBlank(purchaseRequestProductForm.getPurchaseRequestProductIdEncrypt())) {// check create new data
						purchaseRequestProduct = new PurchaseRequestProduct();
						purchaseRequestProduct.setCreate(userSession);
						purchaseRequestProduct.setAmount(NumberUtils.stringToBigDecimal(purchaseRequestProductForm.getAmount()));
						purchaseRequestProduct.setPrice(NumberUtils.stringToBigDecimal(purchaseRequestProductForm.getPrice()));
						
						//Product
						Product product = null;
						if(!StringUtils.isBlank(purchaseRequestProductForm.getProductIdEncrypt())) {
							product = productRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestProductForm.getProductIdEncrypt()));
						}
						purchaseRequestProduct.setProduct(product);
						//Measure
						Measure measure = null;
						if(!StringUtils.isBlank(purchaseRequestProductForm.getProductIdEncrypt())) {
							measure = measureRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestProductForm.getMeasureIdEncrypt()));
						}
						purchaseRequestProduct.setMeasure(measure);
						
						prProductFormListTemp.add(purchaseRequestProduct);
					}else {
						purchaseRequestProduct = purchaseRequestProductRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestProductForm.getPurchaseRequestProductIdEncrypt()));
						purchaseRequestProduct.setUpdate(userSession);
						purchaseRequestProduct.setAmount(NumberUtils.stringToBigDecimal(purchaseRequestProductForm.getAmount()));
						purchaseRequestProduct.setPrice(NumberUtils.stringToBigDecimal(purchaseRequestProductForm.getPrice()));
						//Product
						Product product = null;
						if(!StringUtils.isBlank(purchaseRequestProductForm.getProductIdEncrypt())) {
							product = productRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestProductForm.getProductIdEncrypt()));
						}
						purchaseRequestProduct.setProduct(product);
						
						//Measure
						Measure measure = null;
						if(!StringUtils.isBlank(purchaseRequestProductForm.getProductIdEncrypt())) {
							measure = measureRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestProductForm.getMeasureIdEncrypt()));
						}
						purchaseRequestProduct.setMeasure(measure);
						
						prProductFormListTemp.add(purchaseRequestProduct);
						
					}
					
				}
			}
		}
		
		if(purchaseRequestTemp.getPurchaseRequestProductList() == null) {
			purchaseRequestTemp.setPurchaseRequestProductList(new ArrayList<>());
		}
		purchaseRequestTemp.getPurchaseRequestProductList().clear();
		purchaseRequestTemp.getPurchaseRequestProductList().addAll(prProductFormListTemp);
		
		purchaseRequestRepository.save(purchaseRequestTemp);
		purchaseRequestForm.setPurchaseRequestIdEncrypt(AESencrpUtils.encryptLong(purchaseRequestTemp.getPurchaseRequestId()));
		
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String idEncrypt, User userSession) throws GodungIdException{
		logger.debug("I:");
		PurchaseRequest purchaseRequestTemp = purchaseRequestRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		godungService.checkGodungId(purchaseRequestTemp.getGodung().getGodungId(), userSession);
		purchaseRequestRepository.delete(purchaseRequestTemp.getPurchaseRequestId());
		logger.debug("O:");
	}
	
}