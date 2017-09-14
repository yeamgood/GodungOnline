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
import com.yeamgood.godungonline.form.PriceForm;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.Price;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.PriceService;
import com.yeamgood.godungonline.service.ProductService;

@Controller
public class PriceController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Long MENU_ID = (long) 13;
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	PriceService priceService;
	
	@Autowired
	ProductService productService;
	
	@RequestMapping(value="/user/price", method = RequestMethod.GET)
	public ModelAndView userPrice(HttpSession session) throws Exception{
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		//User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(MENU_ID);
		//List<Price> priceList = priceService.findAllByGodungGodungIdOrderByPriceNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject("menu", menu);
		//modelAndView.addObject("priceList", priceList);
		modelAndView.setViewName("user/price");
		logger.debug("O");
		return modelAndView;
	}
	
//	@RequestMapping(value="/user/price/list/ajax", method=RequestMethod.GET)
//	public @ResponseBody String userPriceListtest(DataTablesRequest datatableRequest, HttpSession session) throws Exception{
//		logger.debug("I");
//		logger.debug("datatableRequest" + datatableRequest.toString());
//		
//		User userSession = (User) session.getAttribute("user");
//		Long godungId = userSession.getGodung().getGodungId();
//		List<Price> priceList = priceService.findAllByGodungGodungIdOrderByPriceNameAsc(godungId);
//		logger.debug("O:priceList" + priceList.size());
//		
//		DataTableObject dataTableObject = new DataTableObject();
//		dataTableObject.setAaData(priceList);
//		String result = new ObjectMapper().writeValueAsString(dataTableObject);
//		return result;
//	}
	
	@RequestMapping(value="/user/price/list/ajax/{idEncrypt}", method=RequestMethod.GET)
	public @ResponseBody String userPriceList(DataTablesRequest datatableRequest, HttpSession session,@PathVariable String idEncrypt) throws Exception{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		logger.debug("idEncrypt" + idEncrypt);
		User userSession = (User) session.getAttribute("user");
		
		List<Price> priceList = new ArrayList<Price>();
		if(StringUtils.isNotBlank(idEncrypt) && !StringUtils.equalsAnyIgnoreCase(idEncrypt, "null")) {
			Product product = productService.findByIdEncrypt(idEncrypt, userSession);
			priceList = product.getPriceList();
		}
		for (Price price : priceList) {
			price.encryptData(price);
		}
		
		logger.debug("O:priceList" + priceList.size());
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(priceList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		return result;
	}
	
	@RequestMapping(value="/user/price/load/new", method=RequestMethod.GET)
	public @ResponseBody JsonResponse loadAdd(HttpSession session){
		logger.debug("I");
		Pnotify pnotify;
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Price price = new Price();
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(price);
		} catch (Exception e) {
			logger.error("error:",e);
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/price/load", method=RequestMethod.GET)
	public @ResponseBody JsonResponse load(Price price,HttpSession session){
		logger.debug("I");
		logger.debug("I" + price.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(price.getPriceIdEncrypt() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			Price priceTemp = priceService.findByIdEncrypt(price.getPriceIdEncrypt(), userSession);
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(priceTemp);
		} catch (Exception e) {
			logger.error("error:",e);
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/price/save", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userPriceAdd(@Valid PriceForm priceForm,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug("I" + priceForm.toString());
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
				priceService.save(priceForm.getProductIdEncrypt(),priceForm, userSession);
				
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
	
	@RequestMapping(value="/user/price/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userPriceDelete(Price price,HttpSession session){
		logger.debug("I");
		logger.debug("I" + price.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(price.getPriceIdEncrypt() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			priceService.delete(price.getPriceIdEncrypt(), userSession);
			
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