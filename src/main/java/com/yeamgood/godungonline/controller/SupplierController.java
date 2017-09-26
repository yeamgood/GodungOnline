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
import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.bean.SupplierType;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Address;
import com.yeamgood.godungonline.model.Country;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.Province;
import com.yeamgood.godungonline.model.Supplier;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.CountryService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProvinceService;
import com.yeamgood.godungonline.service.SupplierService;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Controller
public class SupplierController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String SUPPLIER = "supplier";
	
	private static final String REDIRECT_SUPPLIER = "redirect:/user/supplier";
	private static final String REDIRECT_SUPPLIER_MANAGE = "redirect:/user/supplier/manage/";
	private static final String REDIRECT_SUPPLIER_MANAGE_COMPANY = "redirect:/user/supplier/manage/company";
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	SupplierService supplierService;
	
	@Autowired
	ProvinceService provinceService;
	
	@Autowired
	CountryService countryService;
	
	@RequestMapping(value="/user/supplier", method = RequestMethod.GET)
	public ModelAndView userSupplier(HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(Constants.MENU_SUPPLIER_ID);
		List<Supplier> supplierList = supplierService.findAllByGodungGodungIdOrderBySupplierNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject("supplierList", supplierList);
		modelAndView.setViewName("user/supplier");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/supplier/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userSupplierList(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<Supplier> supplierList = supplierService.findAllByGodungGodungIdOrderBySupplierNameAsc(godungId);
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(supplierList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value="/user/supplier/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userSupplierDelete(Supplier supplier,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, supplier);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			User userSession = (User) session.getAttribute("user");
			supplierService.delete(supplier.getSupplierIdEncrypt(), userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/supplier/manage/{idEncrypt}", method = RequestMethod.GET)
	public ModelAndView userSupplierLoad(Model model,HttpSession session, @PathVariable String idEncrypt) throws GodungIdException {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, idEncrypt);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(Constants.MENU_SUPPLIER_ID);
		User userSession = (User) session.getAttribute("user");
		
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		Supplier supplier = supplierService.findByIdEncrypt(idEncrypt,userSession);
		
		modelAndView.addObject(SUPPLIER,supplier);
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject(Constants.PROVINCE_DROPDOWN,provinceDropdown);
		modelAndView.addObject(Constants.COUNTRY_DROPDOWN,countryDropdown);
		
		if(StringUtils.equals(supplier.getSupplierType(), SupplierType.PERSON.toString())) {
			modelAndView.setViewName("user/supplier_person");
		}else {
			modelAndView.setViewName("user/supplier_company");
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/supplier/manage/product/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userSupplierProductList(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException{
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		List<Product> productList = new ArrayList<>();
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(productList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	// --------------------------------------------------------------------
	// --- PERSON ---------------------------------------------------------
	// --------------------------------------------------------------------
	@RequestMapping(value="/user/supplier/manage/person", method = RequestMethod.GET)
	public ModelAndView userSupplierPerson(Model model,HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(Constants.MENU_SUPPLIER_ID);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		
		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute(SUPPLIER)) {
			logger.debug("New Object");
			Country country = new Country();
			country.setCountryId(Constants.COUNTRY_THAILAND);
			
			Address address = new Address();
			address.setCountry(country);
			
			Supplier supplier = new Supplier();
			supplier.setAddress(address);
			supplier.setAddressSend(address);
			modelAndView.addObject(SUPPLIER,supplier);
	    }
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject(Constants.PROVINCE_DROPDOWN,provinceDropdown);
		modelAndView.setViewName("user/supplier_person");
		modelAndView.addObject(Constants.COUNTRY_DROPDOWN,countryDropdown);
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value = "/user/supplier/manage/person", method = RequestMethod.POST)
	public ModelAndView userSupplierPerson(@Valid Supplier supplier, BindingResult bindingResult,RedirectAttributes redirectAttributes , HttpSession session) {
		logger.debug("I:");
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		if (bindingResult.hasErrors()) {
			logger.debug("bindingResult error");
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.supplier", bindingResult);
			redirectAttributes.addFlashAttribute(SUPPLIER, supplier);
			modelAndView.setViewName("redirect:/user/supplier/manage/person");
		} else {
			try {
				logger.debug("save");
				supplier.setSupplierType(SupplierType.PERSON.toString());
				userSession = (User) session.getAttribute("user");
				supplierService.save(supplier, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_SAVE_SUCCESS));
				modelAndView.setViewName(REDIRECT_SUPPLIER_MANAGE + supplier.getSupplierIdEncrypt());
			} catch (Exception e) {
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_SAVE_ERROR));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute(SUPPLIER, supplier);
				modelAndView.setViewName("redirect:/user/supplier/manage/person");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/supplier/manage/person/delete", method=RequestMethod.POST)
	public ModelAndView userSupplierIndividualDelete(Supplier supplier,HttpSession session, RedirectAttributes redirectAttributes) {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, supplier);
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		
		if(supplier.getSupplierId() == null) {
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_DELETE_ERROR));
			modelAndView.setViewName(REDIRECT_SUPPLIER);
		}
		try {
			userSession = (User) session.getAttribute("user");
			supplierService.delete(supplier.getSupplierIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_DELETE_SUCCESS));
			modelAndView.setViewName(REDIRECT_SUPPLIER);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_DELETE_ERROR));
			modelAndView.setViewName(REDIRECT_SUPPLIER_MANAGE + AESencrpUtils.encryptLong(supplier.getSupplierId()));
		}
		logger.debug("O");
		return modelAndView;
	}
	
	// --------------------------------------------------------------------
	// --- COMPANY ---------------------------------------------------------
	// --------------------------------------------------------------------
	@RequestMapping(value="/user/supplier/manage/company", method = RequestMethod.GET)
	public ModelAndView userSupplierCompany(Model model,HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(Constants.MENU_SUPPLIER_ID);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		
		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute(SUPPLIER)) {
			logger.debug("New Object");
			Country country = new Country();
			country.setCountryId(Constants.COUNTRY_THAILAND);
			
			Address address = new Address();
			address.setCountry(country);
			
			Supplier supplier = new Supplier();
			supplier.setAddress(address);
			supplier.setAddressSend(address);
			modelAndView.addObject(SUPPLIER,supplier);
	    }
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject(Constants.PROVINCE_DROPDOWN,provinceDropdown);
		modelAndView.setViewName("user/supplier_company");
		modelAndView.addObject(Constants.COUNTRY_DROPDOWN,countryDropdown);
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value = "/user/supplier/manage/company", method = RequestMethod.POST)
	public ModelAndView userSupplierCompany(@Valid Supplier supplier, BindingResult bindingResult,RedirectAttributes redirectAttributes , HttpSession session) {
		logger.debug("I:");
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		if (bindingResult.hasErrors()) {
			logger.debug("bindingResult error");
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.supplier", bindingResult);
			redirectAttributes.addFlashAttribute(SUPPLIER, supplier);
			modelAndView.setViewName(REDIRECT_SUPPLIER_MANAGE_COMPANY);
		} else {
			try {
				logger.debug("save");
				supplier.setSupplierType(SupplierType.COMPANY.toString());
				userSession = (User) session.getAttribute("user");
				supplierService.save(supplier, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_SAVE_SUCCESS));
				modelAndView.setViewName(REDIRECT_SUPPLIER_MANAGE + supplier.getSupplierIdEncrypt());
			} catch (Exception e) {
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_SAVE_ERROR));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute(SUPPLIER, supplier);
				modelAndView.setViewName(REDIRECT_SUPPLIER_MANAGE_COMPANY);
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/supplier/manage/company/delete", method=RequestMethod.POST)
	public ModelAndView userSupplierCompanyDelete(Supplier supplier,HttpSession session, RedirectAttributes redirectAttributes) {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, supplier);
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		
		if(supplier.getSupplierId() == null) {
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_DELETE_ERROR));
			modelAndView.setViewName(REDIRECT_SUPPLIER);
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			supplierService.delete(supplier.getSupplierIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_DELETE_SUCCESS));
			modelAndView.setViewName(REDIRECT_SUPPLIER);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_DELETE_ERROR));
			modelAndView.setViewName(REDIRECT_SUPPLIER_MANAGE + AESencrpUtils.encryptLong(supplier.getSupplierId()));
		}
		logger.debug("O");
		return modelAndView;
	}
	
	
	
}