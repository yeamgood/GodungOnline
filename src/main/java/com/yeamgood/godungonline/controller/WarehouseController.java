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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.model.Location;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.model.Warehouse;
import com.yeamgood.godungonline.service.CountryService;
import com.yeamgood.godungonline.service.LocationService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProvinceService;
import com.yeamgood.godungonline.service.WarehouseService;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Controller
public class WarehouseController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Long MENU_ID = (long) 9;
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	WarehouseService warehouseService;
	
	@Autowired
	ProvinceService provinceService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	LocationService locationService;
	
	@RequestMapping(value="/user/warehouse", method = RequestMethod.GET)
	public ModelAndView userWarehouse(HttpSession session) throws Exception{
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(MENU_ID);
		List<Warehouse> warehouseList = warehouseService.findAllByGodungGodungIdOrderByWarehouseNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("warehouseList", warehouseList);
		modelAndView.setViewName("user/warehouse");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/warehouse/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userWarehouseListtest(DataTablesRequest datatableRequest, HttpSession session) throws Exception{
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
	
	@RequestMapping(value="/user/warehouse/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userWarehouseDelete(Warehouse warehouse,HttpSession session){
		logger.debug("I");
		logger.debug("I" + warehouse.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(warehouse.getWarehouseIdEncrypt() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		try {
			userSession = (User) session.getAttribute("user");
			warehouseService.delete(warehouse.getWarehouseIdEncrypt(), userSession);
			
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
	
	
	// --------------------------------------------------------------------
	// --- Manage ---------------------------------------------------------
	// --------------------------------------------------------------------
	@RequestMapping(value="/user/warehouse/manage", method = RequestMethod.GET)
	public ModelAndView userWarehousePerson(Model model,HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		
		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute("warehouse")) {
			logger.debug("New Object");
			Warehouse warehouse = new Warehouse();
			warehouse.setLocationList(new ArrayList<Location>());
			modelAndView.addObject("warehouse",warehouse);
	    }
		
		modelAndView.addObject("menu", menu);
		modelAndView.setViewName("user/warehouse_manage");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/warehouse/manage/{idEncrypt}", method = RequestMethod.GET)
	public ModelAndView userWarehouseLoad(Model model,HttpSession session, @PathVariable String idEncrypt) throws NumberFormatException, Exception{
		logger.debug("I:");
		logger.debug("I:idEncrypt" + idEncrypt);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		User userSession = (User) session.getAttribute("user");
		
		Warehouse warehouse = warehouseService.findByIdEncrypt(idEncrypt,userSession);
		logger.debug("warehouse yeam:" + warehouse.toString());
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("warehouse",warehouse);
		modelAndView.setViewName("user/warehouse_manage");
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/warehouse/location/list/ajax/{idEncrypt}", method=RequestMethod.GET)
	public @ResponseBody String locationListAjax(DataTablesRequest datatableRequest, HttpSession session,@PathVariable String idEncrypt) throws Exception{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		logger.debug("idEncrypt" + idEncrypt);
		User userSession = (User) session.getAttribute("user");
		
		List<Location> locationList = new ArrayList<Location>();
		if(StringUtils.isNotBlank(idEncrypt) && !StringUtils.equalsAnyIgnoreCase(idEncrypt, "null")) {
			Warehouse warehouse = warehouseService.findByIdEncrypt(idEncrypt, userSession);
			locationList = warehouse.getLocationList();
		}
		
		for (Location locationTemp : locationList) {
			locationTemp.setLocationIdEncrypt(AESencrpUtils.encryptLong(locationTemp.getLocationId()));
		}
		
		logger.debug("O:locationList" + locationList.size());
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(locationList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		return result;
	}
	
	@RequestMapping(value = "/user/warehouse/manage", method = RequestMethod.POST)
	public ModelAndView userWarehousePerson(@Valid Warehouse warehouse, BindingResult bindingResult,RedirectAttributes redirectAttributes , HttpSession session) {
		logger.debug("I:");
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		if (bindingResult.hasErrors()) {
			logger.debug("bindingResult error");
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.warehouse", bindingResult);
			redirectAttributes.addFlashAttribute("warehouse", warehouse);
			modelAndView.setViewName("redirect:/user/warehouse/manage");
		} else {
			try {
				logger.debug("save");
				userSession = (User) session.getAttribute("user");
				warehouseService.save(warehouse, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.save.success"));
				modelAndView.setViewName("redirect:/user/warehouse/manage/" + warehouse.getWarehouseIdEncrypt());
			} catch (Exception e) {
				logger.error("error1",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error"));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute("warehouse", warehouse);
				modelAndView.setViewName("redirect:/user/warehouse/manage");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/warehouse/manage/delete", method=RequestMethod.POST)
	public ModelAndView userWarehouseIndividualDelete(Warehouse warehouse,HttpSession session, RedirectAttributes redirectAttributes) throws Exception{
		logger.debug("I");
		logger.debug("I" + warehouse.toString());
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		
		if(warehouse.getWarehouseId() == null) {
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/warehouse");
		}
		try {
			userSession = (User) session.getAttribute("user");
			warehouseService.delete(warehouse.getWarehouseIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.delete.success"));
			modelAndView.setViewName("redirect:/user/warehouse");
		} catch (Exception e) {
			logger.error("error:",e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/warehouse/manage/" + AESencrpUtils.encryptLong(warehouse.getWarehouseId()));
		}
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/warehouse/location/one", method=RequestMethod.POST)
	public @ResponseBody JsonResponse locationAddOne(@Valid Location location,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug("I:location" + location.toString());
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
				List<Location> locationList = new ArrayList<Location>();
				locationList.add(location);
				warehouseService.saveLocation(location.getWarehouseIdEncrypt(), locationList, userSession);
				
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
	
	@RequestMapping(value="/user/warehouse/location/one/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse locationOneDelete(Location location,HttpSession session){
		logger.debug("I");
		logger.debug("I" + location.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(location.getWarehouseIdEncrypt() == null || location.getLocationIdEncrypt() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			warehouseService.deleteLocation(location.getWarehouseIdEncrypt(), location.getLocationIdEncrypt(), userSession);
			
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