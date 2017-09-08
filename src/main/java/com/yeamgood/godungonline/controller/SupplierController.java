package com.yeamgood.godungonline.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.Province;
import com.yeamgood.godungonline.model.Supplier;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProvinceService;
import com.yeamgood.godungonline.service.SupplierService;

@Controller
public class SupplierController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Long MENU_ID = (long) 14;
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	SupplierService supplierService;
	
	@Autowired
	ProvinceService provinceService;
	
	@RequestMapping(value="/user/supplier", method = RequestMethod.GET)
	public ModelAndView userSupplier(HttpSession session){
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
	public @ResponseBody String userSupplierListtest(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException{
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
	
//	@RequestMapping(value="/user/supplier/save", method=RequestMethod.POST)
//	public @ResponseBody JsonResponse userSupplierAdd(@Valid Supplier supplier,BindingResult bindingResult,HttpSession session){
//		logger.debug("I");
//		logger.debug("I" + supplier.toString());
//		Pnotify pnotify;
//		User userSession;
//		JsonResponse jsonResponse = new JsonResponse();
//		
//		if (bindingResult.hasErrors()) {
//			String errorMsg = "";
//			List<FieldError> errors = bindingResult.getFieldErrors();
//		    for (FieldError error : errors ) {
//		    		errorMsg += error.getField() + " - " + error.getDefaultMessage();
//		    }
//		    pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
//		    pnotify.setText(errorMsg);
//		    
//			jsonResponse.setStatus("FAIL");
//			jsonResponse.setResult(errors);
//		}else {
//			try {
//				userSession = (User) session.getAttribute("user");
//				supplierService.save(supplier, userSession);
//				
//				pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.save.success");
//				jsonResponse.setStatus("SUCCESS");
//				jsonResponse.setResult(pnotify);
//				
//			} catch (Exception e) {
//				logger.error("error:",e);
//				pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
//				jsonResponse.setStatus("FAIL");
//				jsonResponse.setResult(pnotify);
//			}
//		}
//		logger.debug("O");
//		return jsonResponse;
//	}
	
	@RequestMapping(value="/user/supplier/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userSupplierDelete(Supplier supplier,HttpSession session){
		logger.debug("I");
		logger.debug("I" + supplier.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(supplier.getSupplierId() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			supplierService.delete(supplier, userSession);
			
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
	
//	@RequestMapping(value="/user/supplier/load", method=RequestMethod.GET)
//	public @ResponseBody JsonResponse load(Supplier supplier,HttpSession session){
//		logger.debug("I");
//		logger.debug("I" + supplier.toString());
//		Pnotify pnotify;
//		User userSession;
//		JsonResponse jsonResponse = new JsonResponse();
//		
//		if(supplier.getSupplierId() == null) {
//			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
//			jsonResponse.setStatus("FAIL");
//			jsonResponse.setResult(pnotify);
//			return jsonResponse;
//		}
//		
//		try {
//			userSession = (User) session.getAttribute("user");
//			Supplier supplierTemp = supplierService.findById(supplier.getSupplierId(), userSession);
//			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
//			jsonResponse.setStatus("SUCCESS");
//			jsonResponse.setResult(supplierTemp);
//		} catch (Exception e) {
//			logger.error("error:",e);
//			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
//			jsonResponse.setStatus("FAIL");
//			jsonResponse.setResult(pnotify);
//		}
//		
//		logger.debug("O");
//		return jsonResponse;
//	}
	
//	@RequestMapping(value="/user/supplier/load/add", method=RequestMethod.GET)
//	public @ResponseBody JsonResponse loadAdd(HttpSession session){
//		logger.debug("I");
//		Pnotify pnotify;
//		JsonResponse jsonResponse = new JsonResponse();
//		try {
//			Supplier supplier = new Supplier();
//			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
//			jsonResponse.setStatus("SUCCESS");
//			jsonResponse.setResult(supplier);
//		} catch (Exception e) {
//			logger.error("error:",e);
//			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
//			jsonResponse.setStatus("FAIL");
//			jsonResponse.setResult(pnotify);
//		}
//		
//		logger.debug("O");
//		return jsonResponse;
//	}
	
	@RequestMapping(value="/user/supplier/individual", method = RequestMethod.GET)
	public ModelAndView userSupplierIndividual(Model model,HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();

		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute("supplier")) {
			logger.debug("New Object");
			modelAndView.addObject("supplier",new Supplier());
	    }
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("provinceDropdown",provinceDropdown);
		modelAndView.setViewName("user/supplier_individual");
		logger.debug("O");
		return modelAndView;
	}
	
	
	@RequestMapping(value="/user/supplier/individual/{id}", method = RequestMethod.GET)
	public ModelAndView userSupplierIndividual(Model model,HttpSession session, @PathVariable Long id){
		logger.debug("I:");
		logger.debug("I:id" + id);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		Supplier supplier = supplierService.findById(id);
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("supplier",supplier);
		modelAndView.addObject("provinceDropdown",provinceDropdown);
		modelAndView.setViewName("user/supplier_individual");
		logger.debug("O:");
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/user/supplier/individual", method = RequestMethod.POST)
	public ModelAndView userSupplierIndividual(@Valid Supplier supplier, BindingResult bindingResult,RedirectAttributes redirectAttributes , HttpSession session) {
		logger.debug("I:");
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		String id = "";
		if (bindingResult.hasErrors()) {
			logger.debug("bindingResult error");
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.supplier", bindingResult);
			redirectAttributes.addFlashAttribute("supplier", supplier);
		} else {
			try {
				logger.debug("save");
				userSession = (User) session.getAttribute("user");
				supplierService.save(supplier, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.save.success"));
				//redirectAttributes.addFlashAttribute("supplier", supplier);
				id = "/" + supplier.getSupplierId();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error"));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute("supplier", supplier);
			}
		}
		modelAndView.setViewName("redirect:/user/supplier/individual" + id);
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/supplier/individual/delete", method=RequestMethod.POST)
	public ModelAndView userSupplierIndividualDelete(Supplier supplier,HttpSession session, RedirectAttributes redirectAttributes){
		logger.debug("I");
		logger.debug("I" + supplier.toString());
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		
		if(supplier.getSupplierId() == null) {
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/supplier/individual/" + supplier.getSupplierId().toString());
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			supplierService.delete(supplier, userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.delete.success"));
			modelAndView.setViewName("redirect:/user/supplier");
		} catch (Exception e) {
			logger.error("error:",e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/supplier/individual/" + supplier.getSupplierId().toString());
		}
		logger.debug("O");
		return modelAndView;
	}
	
	
	@RequestMapping(value="/user/supplier/product/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userSupplierProductList(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		
		//User userSession = (User) session.getAttribute("user");
		//Long godungId = userSession.getGodung().getGodungId();
		//List<Product> productList = productService.findAllByGodungGodungIdOrderByProductNameAsc(godungId);
		//logger.debug("O:productList" + productList.size());
		List<Product> productList = new ArrayList<Product>();
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(productList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		logger.debug("O");
		return result;
	}
}