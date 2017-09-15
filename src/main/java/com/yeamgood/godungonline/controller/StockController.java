package com.yeamgood.godungonline.controller;

import java.math.BigDecimal;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.form.StockForm;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.Stock;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProductService;
import com.yeamgood.godungonline.service.StockService;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Controller
public class StockController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Long MENU_ID = (long) 13;
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	StockService stockService;
	
	@Autowired
	ProductService productService;
	
	@RequestMapping(value="/user/stock", method = RequestMethod.GET)
	public ModelAndView userStock(HttpSession session) throws Exception{
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		modelAndView.addObject("menu", menu);
		modelAndView.setViewName("user/stock");
		logger.debug("O");
		return modelAndView;
	}
	

	@RequestMapping(value="/user/stock/list/ajax/{idEncrypt}", method=RequestMethod.GET)
	public @ResponseBody String userStockList(DataTablesRequest datatableRequest, HttpSession session,@PathVariable String idEncrypt) throws Exception{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		logger.debug("idEncrypt" + idEncrypt);
		User userSession = (User) session.getAttribute("user");
		
		List<Stock> stockList = new ArrayList<Stock>();
		if(StringUtils.isNotBlank(idEncrypt) && !StringUtils.equalsAnyIgnoreCase(idEncrypt, "null")) {
			Product product = productService.findByIdEncrypt(idEncrypt, userSession);
			stockList = product.getStockList();
		}
		
		StockForm stockForm;
		List<StockForm> stockFormList = new ArrayList<StockForm>();
		
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(2);
		for (Stock stock : stockList) {
			stockForm = new StockForm();
			stockForm.setStockIdEncrypt(AESencrpUtils.encryptLong(stock.getStockId()));
			stockForm.setRemindNumber(df.format(stock.getRemindNumber()));
			if(stock.getWarehouse() != null) {
				stockForm.setWarehouseName(stock.getWarehouse().getWarehouseName());
			}
			if(stock.getLocation() != null) {
				stockForm.setLocationCode(stock.getLocation().getLocationCode());
			}
			stockForm.setRemainNumber(df.format(new BigDecimal(0)));// TODO get amount 
			stockFormList.add(stockForm);
		}
		
		logger.debug("O:stockList" + stockFormList.size());
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(stockFormList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		return result;
	}
	
	@RequestMapping(value="/user/stock/load/new", method=RequestMethod.GET)
	public @ResponseBody JsonResponse loadAdd(HttpSession session){
		logger.debug("I");
		Pnotify pnotify;
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Stock stock = new Stock();
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(stock);
		} catch (Exception e) {
			logger.error("error:",e);
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/stock/load", method=RequestMethod.GET)
	public @ResponseBody JsonResponse load(StockForm stockForm,HttpSession session){
		logger.debug("I");
		logger.debug("I" + stockForm.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(stockForm.getStockIdEncrypt() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			Stock stockTemp = stockService.findByIdEncrypt(stockForm.getStockIdEncrypt(), userSession);
			
			DecimalFormat df = new DecimalFormat();
			df.setMinimumFractionDigits(2);
			StockForm stockFormTemp = new StockForm();
			stockFormTemp.setStockIdEncrypt(stockTemp.getStockIdEncrypt());
			stockFormTemp.setWarehouseIdEncrypt(AESencrpUtils.encryptLong(stockTemp.getWarehouse().getWarehouseId()));
			if(stockTemp.getLocation() != null) {
				stockFormTemp.setLocationIdEncrypt(AESencrpUtils.encryptLong(stockTemp.getLocation().getLocationId()));
			}
			stockFormTemp.setRemindNumber(df.format(stockTemp.getRemindNumber()));
			
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(stockFormTemp);
		} catch (Exception e) {
			logger.error("error:",e);
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/stock/save", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userStockAdd(@Valid StockForm stockForm,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug("I" + stockForm.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if (bindingResult.hasErrors()) {
			String errorMsg = "";
			List<FieldError> errors = bindingResult.getFieldErrors();
		    for (FieldError error : errors ) {
		    		errorMsg += error.getField() + " - " + error.getDefaultMessage();
		    }
		    pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
		    pnotify.setText(errorMsg);
		    
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(errors);
		}else {
			try {
				userSession = (User) session.getAttribute("user");
				stockService.save(stockForm.getProductIdEncrypt(),stockForm, userSession);
				
				pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.save.success");
				jsonResponse.setStatus("SUCCESS");
				jsonResponse.setResult(pnotify);
				
			} catch (Exception e) {
				logger.error("error:",e);
				pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
				jsonResponse.setStatus("FAIL");
				jsonResponse.setResult(pnotify);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/stock/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userStockDelete(StockForm stockForm,HttpSession session){
		logger.debug("I");
		logger.debug("I" + stockForm.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(stockForm.getStockIdEncrypt() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			stockService.delete(stockForm.getProductIdEncrypt(),stockForm.getStockIdEncrypt(), userSession);
			
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.delete.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(pnotify);
			
		} catch (Exception e) {
			logger.error("error:",e);
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	
	

}