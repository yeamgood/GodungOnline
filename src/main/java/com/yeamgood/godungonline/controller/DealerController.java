package com.yeamgood.godungonline.controller;

import java.text.DecimalFormat;
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
import com.yeamgood.godungonline.datatables.DealerDatatables;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.form.DealerForm;
import com.yeamgood.godungonline.model.Dealer;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.DealerService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProductService;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.DateUtils;

@Controller
public class DealerController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	DealerService dealerService;
	
	@Autowired
	ProductService productService;
	
	private static final String LOG_DEALER = "dealer:{}";
	private static final String LOG_PRODUCT_IDENCRYPT = "productIdEncrypt:{}";
	

	@RequestMapping(value="/user/dealer/list/ajax/{productIdEncrypt}", method=RequestMethod.GET)
	public @ResponseBody String userDealerList(DataTablesRequest datatableRequest, HttpSession session,@PathVariable String productIdEncrypt) throws GodungIdException, JsonProcessingException {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		logger.debug(LOG_PRODUCT_IDENCRYPT, productIdEncrypt);
		User userSession = (User) session.getAttribute("user");
		
		List<Dealer> dealerList = new ArrayList<>();
		if(StringUtils.isNotBlank(productIdEncrypt) && !StringUtils.equalsAnyIgnoreCase(productIdEncrypt, "null")) {
			Product product = productService.findByIdEncrypt(productIdEncrypt, userSession);
			dealerList = product.getDealerList();
		}
		
		DealerDatatables dealerDatatables;
		List<DealerDatatables> dealerDatatablesList = new ArrayList<>();
		
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(2);
		for (Dealer dealer : dealerList) {
			dealerDatatables = new DealerDatatables();
			dealerDatatables.setDealerIdEncrypt(AESencrpUtils.encryptLong(dealer.getDealerId()));
			dealerDatatables.setSupplierName(dealer.getSupplier().getFullname());
			dealerDatatables.setPrice(df.format(dealer.getPrice()));
			dealerDatatables.setStartDate(DateUtils.dateToString(dealer.getStartDate(), DateUtils.DDMMYYYY));
			dealerDatatables.setEndDate(DateUtils.dateToString(dealer.getEndDate(), DateUtils.DDMMYYYY));
			dealerDatatables.setMeasureName(dealer.getMeasure().getMeasureName());
			dealerDatatables.setCurrencyName(dealer.getCurrency().getCurrencyName());
			dealerDatatablesList.add(dealerDatatables);
		}
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(dealerDatatablesList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value="/user/dealer/load/new", method=RequestMethod.GET)
	public @ResponseBody JsonResponse loadAdd(HttpSession session){
		logger.debug("I");
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Dealer dealer = new Dealer();
			jsonResponse.setStatus(Constants.STATUS_SUCCESS);
			jsonResponse.setResult(dealer);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/dealer/load", method=RequestMethod.GET)
	public @ResponseBody JsonResponse load(DealerForm dealerForm,HttpSession session){
		logger.debug("I");
		logger.debug(LOG_DEALER,dealerForm);
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
	
		try {
			userSession = (User) session.getAttribute("user");
			Dealer dealer = dealerService.findByIdEncrypt(dealerForm.getDealerIdEncrypt(), userSession);
			
			DecimalFormat df = new DecimalFormat();
			df.setMinimumFractionDigits(2);
			DealerForm dealerFormTemp = new DealerForm();
			dealerFormTemp.setDealerIdEncrypt(dealer.getDealerIdEncrypt());
			dealerFormTemp.setSupplierIdEncrypt(AESencrpUtils.encryptLong(dealer.getSupplier().getSupplierId()));
			dealerFormTemp.setPrice(df.format(dealer.getPrice()));
			dealerFormTemp.setStartDate(DateUtils.dateToString(dealer.getStartDate(), DateUtils.DDMMYYYY));
			dealerFormTemp.setEndDate(DateUtils.dateToString(dealer.getEndDate(), DateUtils.DDMMYYYY));
			dealerFormTemp.setMeasureIdEncrypt(AESencrpUtils.encryptLong(dealer.getMeasure().getMeasureId()));
			dealerFormTemp.setCurrencyId(dealer.getCurrency().getCurrencyId());
			
			jsonResponse.setLoadSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/dealer/save", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userDealerAdd(@Valid DealerForm dealerForm,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug(LOG_DEALER,dealerForm);
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		}else {
			try {
				userSession = (User) session.getAttribute("user");
				dealerService.save(dealerForm.getProductIdEncrypt(),dealerForm, userSession);
				jsonResponse.setSaveSuccess(messageSource);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR,e);
				jsonResponse.setSaveError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/dealer/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userDealerDelete(DealerForm dealerForm,HttpSession session){
		logger.debug("I");
		logger.debug(LOG_DEALER,dealerForm);
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		try {
			userSession = (User) session.getAttribute("user");
			dealerService.delete(dealerForm.getProductIdEncrypt(),dealerForm.getDealerIdEncrypt(), userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	
	

}