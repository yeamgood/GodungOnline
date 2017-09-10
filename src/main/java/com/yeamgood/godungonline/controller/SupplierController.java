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
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
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
	private final Long MENU_ID = (long) 31;
	private final Long COUNTRY_THAILAND = (long) 217;
	
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
	public ModelAndView userSupplier(HttpSession session) throws Exception{
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(MENU_ID);
		List<Supplier> supplierList = supplierService.findAllByGodungGodungIdOrderBySupplierNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("supplierList", supplierList);
		modelAndView.setViewName("user/supplier");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/supplier/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userSupplierListtest(DataTablesRequest datatableRequest, HttpSession session) throws Exception{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<Supplier> supplierList = supplierService.findAllByGodungGodungIdOrderBySupplierNameAsc(godungId);
		logger.debug("O:supplierList" + supplierList.size());
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(supplierList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		return result;
	}
	
	@RequestMapping(value="/user/supplier/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userSupplierDelete(Supplier supplier,HttpSession session){
		logger.debug("I");
		logger.debug("I" + supplier.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(supplier.getSupplierIdEncrypt() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		try {
			userSession = (User) session.getAttribute("user");
			supplierService.delete(supplier.getSupplierIdEncrypt(), userSession);
			
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
	
	@RequestMapping(value="/user/supplier/manage/{idEncrypt}", method = RequestMethod.GET)
	public ModelAndView userSupplierLoad(Model model,HttpSession session, @PathVariable String idEncrypt) throws NumberFormatException, Exception{
		logger.debug("I:");
		logger.debug("I:idEncrypt" + idEncrypt);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		User userSession = (User) session.getAttribute("user");
		
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		Supplier supplier = supplierService.findByIdEncrypt(idEncrypt,userSession);
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("supplier",supplier);
		modelAndView.addObject("provinceDropdown",provinceDropdown);
		modelAndView.addObject("countryDropdown",countryDropdown);
		
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
		logger.debug("datatableRequest" + datatableRequest.toString());
		
		//TODO add function load list product of supplier
		List<Product> productList = new ArrayList<Product>();
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(productList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		logger.debug("O");
		return result;
	}
	
	// --------------------------------------------------------------------
	// --- PERSON ---------------------------------------------------------
	// --------------------------------------------------------------------
	@RequestMapping(value="/user/supplier/manage/person", method = RequestMethod.GET)
	public ModelAndView userSupplierPerson(Model model,HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		
		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute("supplier")) {
			logger.debug("New Object");
			Country country = new Country();
			country.setCountryId(COUNTRY_THAILAND);
			
			Address address = new Address();
			address.setCountry(country);
			
			Supplier supplier = new Supplier();
			supplier.setAddress(address);
			supplier.setAddressSend(address);
			modelAndView.addObject("supplier",supplier);
	    }
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("provinceDropdown",provinceDropdown);
		modelAndView.setViewName("user/supplier_person");
		modelAndView.addObject("countryDropdown",countryDropdown);
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
			redirectAttributes.addFlashAttribute("supplier", supplier);
			modelAndView.setViewName("redirect:/user/supplier/manage/person");
		} else {
			try {
				logger.debug("save");
				supplier.setSupplierType(SupplierType.PERSON.toString());
				userSession = (User) session.getAttribute("user");
				supplierService.save(supplier, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.save.success"));
				modelAndView.setViewName("redirect:/user/supplier/manage/" + supplier.getSupplierIdEncrypt());
			} catch (Exception e) {
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error"));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute("supplier", supplier);
				modelAndView.setViewName("redirect:/user/supplier/manage/person");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/supplier/manage/person/delete", method=RequestMethod.POST)
	public ModelAndView userSupplierIndividualDelete(Supplier supplier,HttpSession session, RedirectAttributes redirectAttributes) throws Exception{
		logger.debug("I");
		logger.debug("I" + supplier.toString());
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		
		if(supplier.getSupplierId() == null) {
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/supplier");
		}
		try {
			userSession = (User) session.getAttribute("user");
			supplierService.delete(supplier.getSupplierIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.delete.success"));
			modelAndView.setViewName("redirect:/user/supplier");
		} catch (Exception e) {
			logger.error("error:",e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/supplier/manage/" + AESencrpUtils.encryptLong(supplier.getSupplierId()));
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
		Menu menu = menuService.findById(MENU_ID);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		
		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute("supplier")) {
			logger.debug("New Object");
			Country country = new Country();
			country.setCountryId(COUNTRY_THAILAND);
			
			Address address = new Address();
			address.setCountry(country);
			
			Supplier supplier = new Supplier();
			supplier.setAddress(address);
			supplier.setAddressSend(address);
			modelAndView.addObject("supplier",supplier);
	    }
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("provinceDropdown",provinceDropdown);
		modelAndView.setViewName("user/supplier_company");
		modelAndView.addObject("countryDropdown",countryDropdown);
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
			redirectAttributes.addFlashAttribute("supplier", supplier);
			modelAndView.setViewName("redirect:/user/supplier/manage/company");
		} else {
			try {
				logger.debug("save");
				supplier.setSupplierType(SupplierType.COMPANY.toString());
				userSession = (User) session.getAttribute("user");
				supplierService.save(supplier, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.save.success"));
				modelAndView.setViewName("redirect:/user/supplier/manage/" + supplier.getSupplierIdEncrypt());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error"));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute("supplier", supplier);
				modelAndView.setViewName("redirect:/user/supplier/manage/company");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/supplier/manage/company/delete", method=RequestMethod.POST)
	public ModelAndView userSupplierCompanyDelete(Supplier supplier,HttpSession session, RedirectAttributes redirectAttributes) throws Exception{
		logger.debug("I");
		logger.debug("I" + supplier.toString());
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		
		if(supplier.getSupplierId() == null) {
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/supplier");
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			supplierService.delete(supplier.getSupplierIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.delete.success"));
			modelAndView.setViewName("redirect:/user/supplier");
		} catch (Exception e) {
			logger.error("error:",e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/supplier/manage/" + AESencrpUtils.encryptLong(supplier.getSupplierId()));
		}
		logger.debug("O");
		return modelAndView;
	}
	
	
	
}