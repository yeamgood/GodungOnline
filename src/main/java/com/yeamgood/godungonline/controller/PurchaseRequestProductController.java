package com.yeamgood.godungonline.controller;

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
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.exception.GodungIdException;
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
		List<PurchaseRequestProductForm> prProductFormList = new ArrayList<>();
		PurchaseRequestProductForm prProductForm;
		
		if(StringUtils.isNotBlank(purchaseRequestIdEncrypt) && !StringUtils.equalsAnyIgnoreCase(purchaseRequestIdEncrypt, "null")) {
			PurchaseRequest purchaseRequest = purchaseRequestService.findByIdEncrypt(purchaseRequestIdEncrypt, userSession);
			prProductList = purchaseRequest.getPurchaseRequestProductList();
		}
	
		for (PurchaseRequestProduct prProductTemp : prProductList) {
			prProductTemp.encryptData();
			prProductForm = new PurchaseRequestProductForm();
			prProductForm.setAmount(NumberUtils.bigDecimalToString(prProductTemp.getAmount()));
			prProductForm.setPrice(NumberUtils.bigDecimalToString(prProductTemp.getPrice()));
			prProductForm.setTotalPrice(NumberUtils.bigDecimalToString(prProductTemp.getAmount().multiply(prProductTemp.getPrice())));
			prProductForm.setDescription(prProductTemp.getDescription());
			prProductForm.setProductIdEncrypt(prProductTemp.getProduct().getProductIdEncrypt());
			prProductForm.setProductCode(prProductTemp.getProduct().getProductCode());
			prProductForm.setProductName(prProductTemp.getProduct().getProductName());
			prProductForm.setMeasureIdEncrypt(prProductTemp.getMeasure().getMeasureIdEncrypt());
			prProductForm.setMeasureName(prProductTemp.getMeasure().getMeasureName());
			prProductFormList.add(prProductForm);
		}
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(prProductFormList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value="/user/purchaseRequestProduct/manage/{purchaseRequestProductIdEncrypt}", method = RequestMethod.GET)
	public ModelAndView userPurchaseRequestProductLoad(Model model,HttpSession session, @PathVariable String purchaseRequestProductIdEncrypt) throws GodungIdException {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, purchaseRequestProductIdEncrypt);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(Constants.MENU_ROLE_GODUNG_ID);
		User userSession = (User) session.getAttribute("user");
		
		PurchaseRequestProduct purchaseRequestProduct = purchaseRequestProductService.findByIdEncrypt(purchaseRequestProductIdEncrypt,userSession);
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject(PR_PRODUCT,purchaseRequestProduct);
		modelAndView.setViewName("user/purchaseRequestProduct_manage");
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/purchaseRequestProduct/valid", method=RequestMethod.POST)
	public @ResponseBody JsonResponse load(@Valid PurchaseRequestProductForm prProductForm,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT,prProductForm);
		JsonResponse jsonResponse = new JsonResponse();
		
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		}else {
			try {
				jsonResponse.setStatus(Constants.STATUS_SUCCESS);
				jsonResponse.setResult(prProductForm);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR,e);
				jsonResponse.setLoadError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	
	
}