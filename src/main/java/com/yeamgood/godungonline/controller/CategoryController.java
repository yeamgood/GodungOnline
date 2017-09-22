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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.model.Category;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.CategoryService;
import com.yeamgood.godungonline.service.MenuService;

@Controller
public class CategoryController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String LOG_CATEGORY = "category:{}";
	

	@Autowired
	MessageSource messageSource;

	@Autowired
	MenuService menuService;

	@Autowired
	CategoryService categoryService;

	@RequestMapping(value = "/user/category", method = RequestMethod.GET)
	public ModelAndView category(HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(Constants.MENU_CATEGORY_ID);
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.setViewName("user/category");
		logger.debug("O");
		return modelAndView;
	}

	@RequestMapping(value = "/user/category/list", method = RequestMethod.GET)
	public @ResponseBody String categoryList(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException  {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		Long godungId = userSession.getGodung().getGodungId();
		List<Category> categoryList = categoryService.findAllByGodungGodungIdOrderByCategoryNameAsc(godungId);

		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(categoryList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}

	@RequestMapping(value = "/user/category/load", method = RequestMethod.GET)
	public @ResponseBody JsonResponse categoryLoad(Category category, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_CATEGORY, category);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			Category categoryTemp = new Category();
			if (!StringUtils.isBlank(category.getCategoryIdEncrypt())) {
				categoryTemp = categoryService.findByIdEncrypt(category.getCategoryIdEncrypt(), userSession);
			}
			jsonResponse.setObject(Constants.STATUS_SUCCESS, categoryTemp);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}

	@RequestMapping(value = "/user/category/save", method = RequestMethod.POST)
	public @ResponseBody JsonResponse categorySave(@Valid Category category, BindingResult bindingResult, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_CATEGORY, category);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		} else {
			try {
				categoryService.save(category, userSession);
				jsonResponse.setSaveSuccess(messageSource);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR, e);
				jsonResponse.setSaveError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}

	@RequestMapping(value = "/user/category/delete", method = RequestMethod.POST)
	public @ResponseBody JsonResponse categoryDelete(Category category, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_CATEGORY, category);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			categoryService.delete(category, userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}

}