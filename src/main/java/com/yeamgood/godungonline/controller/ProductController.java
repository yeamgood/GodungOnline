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
import com.yeamgood.godungonline.model.Brand;
import com.yeamgood.godungonline.model.Category;
import com.yeamgood.godungonline.model.Measure;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.BrandService;
import com.yeamgood.godungonline.service.CategoryService;
import com.yeamgood.godungonline.service.MeasureService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProductService;

@Controller
public class ProductController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Long MENU_PRODUCT_ID = (long) 10;
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	MeasureService measureService;
	
	@Autowired
	CategoryService categoryService;
	
	@RequestMapping(value="/user/product", method = RequestMethod.GET)
	public ModelAndView userProduct(HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(MENU_PRODUCT_ID);
		
		Long godungId = userSession.getGodung().getGodungId();
		List<Product> productList = productService.findAllByGodungGodungIdOrderByProductNameAsc(godungId);
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("productList", productList);
		modelAndView.setViewName("user/product");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/product/create", method = RequestMethod.GET)
	public ModelAndView userProductCreate(HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(MENU_PRODUCT_ID);
		Product product = new Product();
		
		Long godungId = userSession.getGodung().getGodungId();
		List<Brand> brandList = brandService.findAllByGodungGodungIdOrderByBrandNameAsc(godungId);
		List<Measure> measureList = measureService.findAllByGodungGodungIdOrderByMeasureNameAsc(godungId);
		List<Category> categoryList = categoryService.findAllByGodungGodungIdOrderByCategoryCodeAsc(godungId);
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("product", product);
		modelAndView.addObject("brandList", brandList);
		modelAndView.addObject("measureList", measureList);
		modelAndView.addObject("categoryList", categoryList);
		modelAndView.setViewName("user/product_manage");
		logger.debug("O");
		return modelAndView;
	}
	
	
	@RequestMapping(value="/user/product/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userProductListtest(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<Product> productList = productService.findAllByGodungGodungIdOrderByProductNameAsc(godungId);
		logger.debug("O:productList" + productList.size());
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(productList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		return result;
	}
	
	@RequestMapping(value="/user/product/save", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userProductAdd(@Valid Product product,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug("I" + product.toString());
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
				productService.save(product, userSession);
				
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
	
	@RequestMapping(value="/user/product/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userProductDelete(Product product,HttpSession session){
		logger.debug("I");
		logger.debug("I" + product.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(product.getProductId() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			productService.delete(product, userSession);
			
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
	
	@RequestMapping(value="/user/product/load", method=RequestMethod.GET)
	public @ResponseBody JsonResponse load(Product product,HttpSession session){
		logger.debug("I");
		logger.debug("I" + product.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(product.getProductId() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			Product productTemp = productService.findById(product.getProductId(), userSession);
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(productTemp);
		} catch (Exception e) {
			logger.error("error:",e);
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/product/load/add", method=RequestMethod.GET)
	public @ResponseBody JsonResponse loadAdd(HttpSession session){
		logger.debug("I");
		Pnotify pnotify;
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Product product = new Product();
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(product);
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