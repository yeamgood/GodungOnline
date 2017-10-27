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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.bean.MenuCode;
import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.exception.GodungIdException;
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
	
	private static final String WAREHOUSE = "warehouse";
	
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
	public ModelAndView userWarehouse(HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findOneByMenuCode(MenuCode.WEREHOUSE.toString());
		List<Warehouse> warehouseList = warehouseService.findAllByGodungGodungIdOrderByWarehouseNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject("warehouseList", warehouseList);
		modelAndView.setViewName("user/warehouse");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/warehouse/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userWarehouseList(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<Warehouse> warehouseList = warehouseService.findAllByGodungGodungIdOrderByWarehouseNameAsc(godungId);
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(warehouseList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value="/user/warehouse/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userWarehouseDelete(Warehouse warehouse,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, warehouse);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			User userSession = (User) session.getAttribute("user");
			warehouseService.delete(warehouse.getWarehouseIdEncrypt(), userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setDeleteError(messageSource);
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
		Menu menu = menuService.findOneByMenuCode(MenuCode.WEREHOUSE.toString());
		
		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute(WAREHOUSE)) {
			logger.debug("New Object");
			Warehouse warehouse = new Warehouse();
			warehouse.setLocationList(new ArrayList<>());
			modelAndView.addObject(WAREHOUSE,warehouse);
	    }
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.setViewName("user/warehouse_manage");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/warehouse/manage/{idEncrypt}", method = RequestMethod.GET)
	public ModelAndView userWarehouseLoad(Model model,HttpSession session, @PathVariable String idEncrypt) throws GodungIdException {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, idEncrypt);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findOneByMenuCode(MenuCode.WEREHOUSE.toString());
		User userSession = (User) session.getAttribute("user");
		
		Warehouse warehouse = warehouseService.findByIdEncrypt(idEncrypt,userSession);
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject(WAREHOUSE,warehouse);
		modelAndView.setViewName("user/warehouse_manage");
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/warehouse/location/list/ajax/{idEncrypt}", method=RequestMethod.GET)
	public @ResponseBody String locationListAjax(DataTablesRequest datatableRequest, HttpSession session,@PathVariable String idEncrypt) throws JsonProcessingException, GodungIdException {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		logger.debug(Constants.LOG_INPUT, idEncrypt);
		User userSession = (User) session.getAttribute("user");
		
		List<Location> locationList = new ArrayList<>();
		if(StringUtils.isNotBlank(idEncrypt) && !StringUtils.equalsAnyIgnoreCase(idEncrypt, "null")) {
			Warehouse warehouse = warehouseService.findByIdEncrypt(idEncrypt, userSession);
			locationList = warehouse.getLocationList();
		}
		
		for (Location location : locationList) {
			location.setLocationIdEncrypt(AESencrpUtils.encryptLong(location.getLocationId()));
			location.encryptData();
		}
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(locationList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value = "/user/warehouse/manage", method = RequestMethod.POST)
	public ModelAndView userWarehousePerson(@Valid Warehouse warehouse, BindingResult bindingResult,RedirectAttributes redirectAttributes , HttpSession session) {
		logger.debug("I:");
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		if (bindingResult.hasErrors()) {
			logger.debug("bindingResult error");
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.warehouse", bindingResult);
			redirectAttributes.addFlashAttribute(WAREHOUSE, warehouse);
			modelAndView.setViewName("redirect:/user/warehouse/manage");
		} else {
			try {
				logger.debug("save");
				userSession = (User) session.getAttribute("user");
				warehouseService.save(warehouse, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_SAVE_SUCCESS));
				modelAndView.setViewName("redirect:/user/warehouse/manage/" + warehouse.getWarehouseIdEncrypt());
			} catch (Exception e) {
				logger.error("error1",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_SAVE_ERROR));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute(WAREHOUSE, warehouse);
				modelAndView.setViewName("redirect:/user/warehouse/manage");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/warehouse/manage/delete", method=RequestMethod.POST)
	public ModelAndView userWarehouseIndividualDelete(Warehouse warehouse,HttpSession session, RedirectAttributes redirectAttributes) {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, warehouse);
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		
		if(warehouse.getWarehouseId() == null) {
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_DELETE_ERROR));
			modelAndView.setViewName("redirect:/user/warehouse");
		}
		try {
			userSession = (User) session.getAttribute("user");
			warehouseService.delete(warehouse.getWarehouseIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_DELETE_SUCCESS));
			modelAndView.setViewName("redirect:/user/warehouse");
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_DELETE_ERROR));
			modelAndView.setViewName("redirect:/user/warehouse/manage/" + AESencrpUtils.encryptLong(warehouse.getWarehouseId()));
		}
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/warehouse/location/one", method=RequestMethod.POST)
	public @ResponseBody JsonResponse locationAddOne(@Valid Location location,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, location);
		JsonResponse jsonResponse = new JsonResponse();
		
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		}else {
			try {
				User userSession = (User) session.getAttribute("user");
				List<Location> locationList = new ArrayList<>();
				locationList.add(location);
				warehouseService.saveLocation(location.getWarehouseIdEncrypt(), locationList, userSession);
				jsonResponse.setSaveSuccess(messageSource);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR,e);
				jsonResponse.setSaveError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/warehouse/location/one/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse locationOneDelete(Location location,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, location);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			User userSession = (User) session.getAttribute("user");
			warehouseService.deleteLocation(location.getWarehouseIdEncrypt(), location.getLocationIdEncrypt(), userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setDeleteError(messageSource);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/warehouse/location/one/load", method=RequestMethod.GET)
	public @ResponseBody JsonResponse locationOneLoad(Location location,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, location);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			User userSession = (User) session.getAttribute("user");
			Location locationLoad = locationService.findByIdEncrypt(location.getLocationIdEncrypt(), userSession);
			jsonResponse.setStatus(Constants.STATUS_SUCCESS);
			jsonResponse.setResult(locationLoad);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
}