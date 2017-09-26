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
import com.yeamgood.godungonline.form.StockForm;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.Stock;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProductService;
import com.yeamgood.godungonline.service.StockService;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.NumberUtils;

@Controller
public class StockController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	StockService stockService;
	
	@Autowired
	ProductService productService;

	@RequestMapping(value="/user/stock/list/ajax/{idEncrypt}", method=RequestMethod.GET)
	public @ResponseBody String userStockList(DataTablesRequest datatableRequest, HttpSession session,@PathVariable String idEncrypt) throws GodungIdException, JsonProcessingException {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		logger.debug(Constants.LOG_INPUT, idEncrypt);
		User userSession = (User) session.getAttribute("user");
		
		List<Stock> stockList = new ArrayList<>();
		if(StringUtils.isNotBlank(idEncrypt) && !StringUtils.equalsAnyIgnoreCase(idEncrypt, "null")) {
			Product product = productService.findByIdEncrypt(idEncrypt, userSession);
			stockList = product.getStockList();
		}
		
		StockForm stockForm;
		List<StockForm> stockFormList = new ArrayList<>();
		
		for (Stock stock : stockList) {
			stockForm = new StockForm();
			stockForm.setStockIdEncrypt(AESencrpUtils.encryptLong(stock.getStockId()));
			stockForm.setRemindNumber(NumberUtils.bigDecimalToString(stock.getRemindNumber()));
			if(stock.getWarehouse() != null) {
				stockForm.setWarehouseName(stock.getWarehouse().getWarehouseName());
			}
			if(stock.getLocation() != null) {
				stockForm.setLocationCode(stock.getLocation().getLocationCode());
			}
			stockForm.setRemainNumber(NumberUtils.EMPLY_DATA);
			stockFormList.add(stockForm);
		}
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(stockFormList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value="/user/stock/load/new", method=RequestMethod.GET)
	public @ResponseBody JsonResponse loadAdd(HttpSession session){
		logger.debug("I");
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Stock stock = new Stock();
			jsonResponse.setStatus(Constants.STATUS_SUCCESS);
			jsonResponse.setResult(stock);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setLoadError(messageSource);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/stock/load", method=RequestMethod.GET)
	public @ResponseBody JsonResponse load(StockForm stockForm,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, stockForm);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			User userSession = (User) session.getAttribute("user");
			Stock stockTemp = stockService.findByIdEncrypt(stockForm.getStockIdEncrypt(), userSession);
			
			StockForm stockFormTemp = new StockForm();
			stockFormTemp.setStockIdEncrypt(stockTemp.getStockIdEncrypt());
			stockFormTemp.setWarehouseIdEncrypt(AESencrpUtils.encryptLong(stockTemp.getWarehouse().getWarehouseId()));
			if(stockTemp.getLocation() != null) {
				stockFormTemp.setLocationIdEncrypt(AESencrpUtils.encryptLong(stockTemp.getLocation().getLocationId()));
			}
			stockFormTemp.setRemindNumber(NumberUtils.bigDecimalToString(stockTemp.getRemindNumber()));
			
			jsonResponse.setStatus(Constants.STATUS_SUCCESS);
			jsonResponse.setResult(stockFormTemp);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/stock/save", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userStockAdd(@Valid StockForm stockForm,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, stockForm);
		JsonResponse jsonResponse = new JsonResponse();
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		}else {
			try {
				User userSession = (User) session.getAttribute("user");
				stockService.save(stockForm.getProductIdEncrypt(),stockForm, userSession);
				jsonResponse.setSaveSuccess(messageSource);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR,e);
				jsonResponse.setSaveError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/stock/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userStockDelete(StockForm stockForm,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, stockForm);
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			User userSession = (User) session.getAttribute("user");
			stockService.delete(stockForm.getProductIdEncrypt(),stockForm.getStockIdEncrypt(), userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setDeleteError(messageSource);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	
	

}