package com.yeamgood.godungonline.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.bean.MenuCode;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.datatables.PurchaseRequestProductDatatables;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.form.PurchaseRequestForm;
import com.yeamgood.godungonline.form.PurchaseRequestProductForm;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.PurchaseRequest;
import com.yeamgood.godungonline.model.PurchaseRequestProduct;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.CountryService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProvinceService;
import com.yeamgood.godungonline.service.PurchaseRequestProductService;
import com.yeamgood.godungonline.service.PurchaseRequestService;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.NumberUtils;

@Controller
public class PurchaseRequestProductController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String PR_PRODUCT = "purchaseRequestProduct";
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	PurchaseRequestService purchaseRequestService;
	
	@Autowired
	PurchaseRequestProductService purchaseRequestProductService;
	
	@Autowired
	ProvinceService provinceService;
	
	@Autowired
	CountryService countryService;
	

	
	@RequestMapping(value="/user/purchaseRequestProduct/list/ajax/{purchaseRequestIdEncrypt}", method=RequestMethod.GET)
	public @ResponseBody String userPurchaseRequestProductList(DataTablesRequest datatableRequest, HttpSession session,@PathVariable String purchaseRequestIdEncrypt) throws JsonProcessingException, GodungIdException {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		logger.debug(Constants.LOG_INPUT, purchaseRequestIdEncrypt);
		User userSession = (User) session.getAttribute("user");
		
		List<PurchaseRequestProduct> prProductList = new ArrayList<>();
		List<PurchaseRequestProductDatatables> prProductDatatablesList = new ArrayList<>();
		PurchaseRequestProductDatatables prProductDatatables;
		
		if(StringUtils.isNotBlank(purchaseRequestIdEncrypt) && !StringUtils.equalsAnyIgnoreCase(purchaseRequestIdEncrypt, "null")) {
			PurchaseRequest purchaseRequest = purchaseRequestService.findByIdEncrypt(purchaseRequestIdEncrypt, userSession);
			prProductList = purchaseRequest.getPurchaseRequestProductList();
		}
	
		for (PurchaseRequestProduct prProductTemp : prProductList) {
			prProductTemp.encryptData();
			prProductDatatables = new PurchaseRequestProductDatatables();
			prProductDatatables.setPurchaseRequestProductIdEncrypt(prProductTemp.getPurchaseRequestProductIdEncrypt());
			prProductDatatables.setAmount(NumberUtils.bigDecimalToString(prProductTemp.getAmount()));
			prProductDatatables.setPrice(NumberUtils.bigDecimalToString(prProductTemp.getPrice()));
			prProductDatatables.setTotalPrice(NumberUtils.bigDecimalToString(prProductTemp.getAmount().multiply(prProductTemp.getPrice())));
			prProductDatatables.setDescription(prProductTemp.getDescription());
			prProductDatatables.setProductCode(prProductTemp.getProduct().getProductCode());
			prProductDatatables.setProductName(prProductTemp.getProduct().getProductName());
			prProductDatatables.setMeasureName(prProductTemp.getMeasure().getMeasureName());
			prProductDatatablesList.add(prProductDatatables);
		}
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(prProductDatatablesList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value="/user/purchaseRequestProduct/load/{purchaseRequestProductIdEncrypt}", method=RequestMethod.GET)
	public @ResponseBody JsonResponse load(@PathVariable String purchaseRequestProductIdEncrypt,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT,purchaseRequestProductIdEncrypt);
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			User userSession = (User) session.getAttribute("user");
			PurchaseRequestProduct prProduct = purchaseRequestProductService.findByIdEncrypt(purchaseRequestProductIdEncrypt, userSession);
			
			PurchaseRequestProductForm prProductForm = new PurchaseRequestProductForm();
			prProductForm.setPurchaseRequestProductIdEncrypt(prProduct.getPurchaseRequestProductIdEncrypt());
			prProductForm.setProductIdEncrypt(AESencrpUtils.encryptLong(prProduct.getProduct().getProductId()));
			prProductForm.setProductName(prProduct.getProduct().getProductName());
			prProductForm.setMeasureIdEncrypt(AESencrpUtils.encryptLong(prProduct.getMeasure().getMeasureId()));
			prProductForm.setAmount(NumberUtils.bigDecimalToString(prProduct.getAmount()));
			prProductForm.setPrice(NumberUtils.bigDecimalToString(prProduct.getPrice()));
			prProductForm.setTotalPrice(NumberUtils.bigDecimalToString(prProduct.getAmount().multiply(prProduct.getPrice())));
			prProductForm.setDescription(prProduct.getDescription());
			 
			jsonResponse.setStatus(Constants.STATUS_SUCCESS);
			jsonResponse.setResult(prProductForm);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/purchaseRequestProduct/manage/{purchaseRequestProductIdEncrypt}", method = RequestMethod.GET)
	public ModelAndView userPurchaseRequestProductLoad(Model model,HttpSession session, @PathVariable String purchaseRequestProductIdEncrypt) throws GodungIdException {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, purchaseRequestProductIdEncrypt);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findOneByMenuCode(MenuCode.PURCHASE_REQUEST.toString());
		User userSession = (User) session.getAttribute("user");
		
		PurchaseRequestProduct purchaseRequestProduct = purchaseRequestProductService.findByIdEncrypt(purchaseRequestProductIdEncrypt,userSession);
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject(PR_PRODUCT,purchaseRequestProduct);
		modelAndView.setViewName("user/purchaseRequestProduct_manage");
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/purchaseRequestProduct/save", method=RequestMethod.POST)
	public @ResponseBody JsonResponse save(@Valid PurchaseRequestProductForm prProductForm,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT,prProductForm);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute("user");
		
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		}else {
			try {
				purchaseRequestProductService.save(prProductForm.getPurchaseRequestIdEncrypt(), prProductForm, userSession);
				jsonResponse.setStatus(Constants.STATUS_SUCCESS);
				jsonResponse.setSaveSuccess(messageSource);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR,e);
				jsonResponse.setLoadError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/purchaseRequestProduct/calculateTotal/{purchaseRequestIdEncrypt}", method=RequestMethod.POST)
	public @ResponseBody JsonResponse calculateTotal(@PathVariable String purchaseRequestIdEncrypt,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, purchaseRequestIdEncrypt);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute("user");
		
		try {
			List<PurchaseRequestProduct> prProductList = new ArrayList<>();
			if(StringUtils.isNotBlank(purchaseRequestIdEncrypt) && !StringUtils.equalsAnyIgnoreCase(purchaseRequestIdEncrypt, "null")) {
				PurchaseRequest purchaseRequest = purchaseRequestService.findByIdEncrypt(purchaseRequestIdEncrypt, userSession);
				prProductList = purchaseRequest.getPurchaseRequestProductList();
			}
		
			BigDecimal totalAmount = new BigDecimal(NumberUtils.EMPLY_DATA);
			BigDecimal totalPrice = new BigDecimal(NumberUtils.EMPLY_DATA);
			for (PurchaseRequestProduct prProductTemp : prProductList) {
				totalAmount = totalAmount.add(prProductTemp.getAmount());
				totalPrice = totalPrice.add(prProductTemp.getAmount().multiply(prProductTemp.getPrice()));
			}
			
			PurchaseRequestForm purchaseRequestForm = new PurchaseRequestForm();
			purchaseRequestForm.setTotalAmount(NumberUtils.bigDecimalToString(totalAmount));
			purchaseRequestForm.setTotalPrice(NumberUtils.bigDecimalToString(totalPrice));
			
			jsonResponse.setStatus(Constants.STATUS_SUCCESS);
			jsonResponse.setResult(purchaseRequestForm);
		}catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/purchaseRequestProduct/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse delete(PurchaseRequestProductForm purchaserRequestProductForm,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, purchaserRequestProductForm);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			User userSession = (User) session.getAttribute("user");
			purchaseRequestProductService.delete(purchaserRequestProductForm.getPurchaseRequestIdEncrypt(), purchaserRequestProductForm.getPurchaseRequestProductIdEncrypt(), userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}

}