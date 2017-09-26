package com.yeamgood.godungonline.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.form.SaleForm;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.Sale;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProductService;
import com.yeamgood.godungonline.service.SaleService;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.DateUtils;
import com.yeamgood.godungonline.utils.NumberUtils;

@Controller
public class SaleController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	SaleService saleService;
	
	@Autowired
	ProductService productService;
	
	
	@RequestMapping(value="/user/sale/list/ajax/{productIdEncrypt}", method=RequestMethod.GET)
	public @ResponseBody String userSaleList(DataTablesRequest datatableRequest, HttpSession session,@PathVariable String productIdEncrypt) throws GodungIdException, JsonProcessingException {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		logger.debug(Constants.LOG_INPUT, productIdEncrypt);
		User userSession = (User) session.getAttribute("user");
		
		List<Sale> saleList = new ArrayList<>();
		List<SaleForm> saleFormList = new ArrayList<>();
		SaleForm saleForm;
		
		if(StringUtils.isNotBlank(productIdEncrypt) && !StringUtils.equalsAnyIgnoreCase(productIdEncrypt, "null")) {
			Product product = productService.findByIdEncrypt(productIdEncrypt, userSession);
			saleList = product.getSaleList();
		}
	
		Collections.sort(saleList, (o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()));
		
		for (Sale sale : saleList) {
			sale.encryptData();
			saleForm = new SaleForm();
			saleForm.setStartDateText(DateUtils.dateToString(sale.getStartDate(), DateUtils.DDMMYYYY));
			saleForm.setEndDateText(DateUtils.dateToString(sale.getEndDate(), DateUtils.DDMMYYYY));
			saleForm.setPrice(NumberUtils.bigDecimalToString(sale.getPrice()));
			saleForm.setCurrencyName(sale.getCurrency().getCurrencyName());
			saleForm.setMeasureName(sale.getMeasure().getMeasureName());
			saleForm.setSaleIdEncrypt(sale.getSaleIdEncrypt());
			saleFormList.add(saleForm);
		}
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(saleFormList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value="/user/sale/load/new", method=RequestMethod.GET)
	public @ResponseBody JsonResponse loadAdd(HttpSession session){
		logger.debug("I");
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Sale sale = new Sale();
			jsonResponse.setStatus(Constants.STATUS_SUCCESS);
			jsonResponse.setResult(sale);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/sale/load", method=RequestMethod.GET)
	public @ResponseBody JsonResponse load(Sale sale,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT,sale);
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			User userSession = (User) session.getAttribute("user");
			Sale saleTemp = saleService.findByIdEncrypt(sale.getSaleIdEncrypt(), userSession);
			
			SaleForm saleForm = new SaleForm();
			saleForm.setStartDateText(DateUtils.dateToString(saleTemp.getStartDate(), DateUtils.DDMMYYYY));
			saleForm.setEndDateText(DateUtils.dateToString(saleTemp.getEndDate(), DateUtils.DDMMYYYY));
			saleForm.setPrice(NumberUtils.bigDecimalToString(saleTemp.getPrice()));
			saleForm.setCurrencyId(saleTemp.getCurrency().getCurrencyId());
			saleForm.setCurrencyName(saleTemp.getCurrency().getCurrencyName());
			saleForm.setMeasureIdEncrypt(AESencrpUtils.encryptLong(saleTemp.getMeasure().getMeasureId()));
			saleForm.setMeasureName(saleTemp.getMeasure().getMeasureName());
			saleForm.setSaleIdEncrypt(saleTemp.getSaleIdEncrypt());
			 
			jsonResponse.setStatus(Constants.STATUS_SUCCESS);
			jsonResponse.setResult(saleForm);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/sale/save", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userSaleAdd(@Valid SaleForm saleForm,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, saleForm);
		JsonResponse jsonResponse = new JsonResponse();
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		}else {
			try {
				User userSession = (User) session.getAttribute("user");
				saleService.save(saleForm.getProductIdEncrypt(),saleForm, userSession);
				jsonResponse.setSaveSuccess(messageSource);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR,e);
				jsonResponse.setSaveError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/sale/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userSaleDelete(SaleForm saleForm,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, saleForm);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			User userSession = (User) session.getAttribute("user");
			saleService.delete(saleForm.getProductIdEncrypt(),saleForm.getSaleIdEncrypt(), userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	
	

}