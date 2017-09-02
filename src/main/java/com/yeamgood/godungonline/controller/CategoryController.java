package com.yeamgood.godungonline.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.yeamgood.godungonline.form.CategoryForm;
import com.yeamgood.godungonline.model.Category;
import com.yeamgood.godungonline.model.CategoryBranch;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.CategoryBranchService;
import com.yeamgood.godungonline.service.CategoryService;
import com.yeamgood.godungonline.service.MenuService;

@Controller
public class CategoryController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Long MENU_CATEGORY_ID = (long) 13;
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	CategoryBranchService categoryBranchService;
	
	@RequestMapping(value="/user/category", method = RequestMethod.GET)
	public ModelAndView userCategory(HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_CATEGORY_ID);
		modelAndView.addObject("menu", menu);
		modelAndView.setViewName("user/category");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/category/list/server", method=RequestMethod.GET)
	public @ResponseBody String userCategoryList(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();

		int start = datatableRequest.getiDisplayStart();
		int length = datatableRequest.getiDisplayLength();
		if(length ==0) {
			length = 10;
		}
		int page = start/length;
		String sSearch = datatableRequest.getsSearch();
		logger.debug("start:" + start + " length:" + length + " page:" + page);
		Pageable pageable = new PageRequest( page, length,datatableRequest.getDirection(), datatableRequest.getNamecolumn());
		long count = categoryService.count(godungId);
		List<Category> categoryList = categoryService.findByGodungGodungIdAndCategoryNameIgnoreCaseContaining(godungId,sSearch,pageable);
		logger.debug("O:categoryList" + categoryList.size());
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setiTotalRecords((int)count);
		dataTableObject.setiTotalDisplayRecords((int)count);
		dataTableObject.setAaData(categoryList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		return result;
	}
	
	
	@RequestMapping(value="/user/category/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userCategoryListtest(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<Category> categoryList = categoryService.findAllByGodungGodungIdOrderByCategoryCodeAsc(godungId);
		logger.debug("O:categoryList" + categoryList.size());
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(categoryList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		return result;
	}
	
	@RequestMapping(value="/user/category/save", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userCategoryAdd(@Valid Category category,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug("I" + category.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if (bindingResult.hasErrors()) {
			String errorMsg = "";
			List<FieldError> errors = bindingResult.getFieldErrors();
		    for (FieldError error : errors ) {
		    		//errorMsg += error.getObjectName() + " - " + error.getCode() + " - " + error.getField() + " - " + error.getArguments() + " - " +error.getDefaultMessage();
		    		errorMsg += error.getField() + " - " + error.getDefaultMessage();
		    }
		    pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
		    pnotify.setText(errorMsg);
		    
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(errors);
			//jsonResponse.setResult(pnotify);
		}else {
			try {
				userSession = (User) session.getAttribute("user");
				categoryService.save(category, userSession);
				
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
	
	@RequestMapping(value="/user/category/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userCategoryDelete(Category category,HttpSession session){
		logger.debug("I");
		logger.debug("I" + category.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(category.getCategoryId() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			categoryService.delete(category, userSession);
			
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
	
	@RequestMapping(value="/user/category/load", method=RequestMethod.GET)
	public @ResponseBody JsonResponse load(Category category,HttpSession session){
		logger.debug("I");
		logger.debug("I" + category.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(category.getCategoryId() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			Category categoryTemp = categoryService.findById(category.getCategoryId(), userSession);
			List<Category> categorySelectList = categoryService.findAllByGodungGodungIdOrderByCategoryCodeAsc(userSession.getGodung().getGodungId());
			List<CategoryBranch> categoryBranchList = categoryBranchService.findAllByCategoryId(category.getCategoryId());
			
			//select child category sub
			List<Category> categoryBranchShowList = new ArrayList<Category>();
			Category categoryBranchChild;
			for (CategoryBranch categoryBranch : categoryBranchList) {
				categoryBranchChild = new Category();
				categoryBranchChild = categoryService.findById(categoryBranch.getCategoryRefId());
				categoryBranchShowList.add(categoryBranchChild);
			}
			logger.debug("categoryBranchShowList size : " + categoryBranchShowList.size());
			
			CategoryForm categoryForm = new CategoryForm();
			categoryForm.mapObjectToForm(categoryTemp);
			categoryForm.setCategoryDropdownList(categorySelectList);
			categoryForm.setCatogoryBranchShowList(categoryBranchShowList);
			
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(categoryForm);
		} catch (Exception e) {
			logger.error("error:",e);
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/category/load/add", method=RequestMethod.GET)
	public @ResponseBody JsonResponse loadNew(HttpSession session){
		logger.debug("I");
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			userSession = (User) session.getAttribute("user");
			CategoryForm categoryForm = new CategoryForm();
			List<Category> categoryDropdownList = categoryService.findAllByGodungGodungIdOrderByCategoryCodeAsc(userSession.getGodung().getGodungId());
			categoryForm.setCategoryDropdownList(categoryDropdownList);
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(categoryForm);
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