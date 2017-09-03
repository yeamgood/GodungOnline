package com.yeamgood.godungonline.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.model.Warehouse;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.WarehouseService;
import com.yeamgood.godungonline.service.MenuService;

@Controller
public class WarehouseController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Long MENU_CATEGORY_ID = (long) 13;
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	WarehouseService warehouseService;
	
	@RequestMapping(value="/user/warehouse", method = RequestMethod.GET)
	public ModelAndView userWarehouse(HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(MENU_CATEGORY_ID);
		List<Warehouse> warehouseList = warehouseService.findAllByGodungGodungIdOrderByWarehouseNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("warehouseList", warehouseList);
		modelAndView.setViewName("user/warehouse");
		logger.debug("O");
		return modelAndView;
	}
	
	
	@RequestMapping(value="/user/warehouse/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userWarehouseListtest(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<Warehouse> warehouseList = warehouseService.findAllByGodungGodungIdOrderByWarehouseNameAsc(godungId);
		logger.debug("O:warehouseList" + warehouseList.size());
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(warehouseList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		return result;
	}
	
	@RequestMapping(value="/user/warehouse/save", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userWarehouseAdd(@Valid Warehouse warehouse,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug("I" + warehouse.toString());
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
				warehouseService.save(warehouse, userSession);
				
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
	
	@RequestMapping(value="/user/warehouse/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userWarehouseDelete(Warehouse warehouse,HttpSession session){
		logger.debug("I");
		logger.debug("I" + warehouse.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(warehouse.getWarehouseId() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			warehouseService.delete(warehouse, userSession);
			
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
	
	@RequestMapping(value="/user/warehouse/load", method=RequestMethod.GET)
	public @ResponseBody JsonResponse load(Warehouse warehouse,HttpSession session){
		logger.debug("I");
		logger.debug("I" + warehouse.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(warehouse.getWarehouseId() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			Warehouse warehouseTemp = warehouseService.findById(warehouse.getWarehouseId(), userSession);
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(warehouseTemp);
		} catch (Exception e) {
			logger.error("error:",e);
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/warehouse/load/add", method=RequestMethod.GET)
	public @ResponseBody JsonResponse loadAdd(HttpSession session){
		logger.debug("I");
		Pnotify pnotify;
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Warehouse warehouse = new Warehouse();
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(warehouse);
		} catch (Exception e) {
			logger.error("error:",e);
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
		}
		
		logger.debug("O");
		return jsonResponse;
	}

}