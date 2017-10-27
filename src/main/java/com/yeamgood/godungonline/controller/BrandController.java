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
import com.yeamgood.godungonline.bean.MenuCode;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.model.Brand;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.BrandService;
import com.yeamgood.godungonline.service.MenuService;

@Controller
public class BrandController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String LOG_BRAND = "brand:{}";
	

	@Autowired
	MessageSource messageSource;

	@Autowired
	MenuService menuService;

	@Autowired
	BrandService brandService;

	@RequestMapping(value = "/user/brand", method = RequestMethod.GET)
	public ModelAndView brand(HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findOneByMenuCode(MenuCode.BRAND.toString());
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.setViewName("user/brand");
		logger.debug("O");
		return modelAndView;
	}

	@RequestMapping(value = "/user/brand/list", method = RequestMethod.GET)
	public @ResponseBody String brandList(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException  {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		Long godungId = userSession.getGodung().getGodungId();
		List<Brand> brandList = brandService.findAllByGodungGodungIdOrderByBrandNameAsc(godungId);

		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(brandList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value = "/user/brand/load", method = RequestMethod.GET)
	public @ResponseBody JsonResponse brandLoad(Brand brand, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_BRAND, brand);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			Brand brandTemp = new Brand();
			if (!StringUtils.isBlank(brand.getBrandIdEncrypt())) {
				brandTemp = brandService.findByIdEncrypt(brand.getBrandIdEncrypt(), userSession);
			}
			jsonResponse.setObject(Constants.STATUS_SUCCESS, brandTemp);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}

	@RequestMapping(value = "/user/brand/save", method = RequestMethod.POST)
	public @ResponseBody JsonResponse brandSave(@Valid Brand brand, BindingResult bindingResult,HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_BRAND, brand);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		} else {
			try {
				brandService.save(brand, userSession);
				jsonResponse.setSaveSuccess(messageSource);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR, e);
				jsonResponse.setSaveError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}

	@RequestMapping(value = "/user/brand/delete", method = RequestMethod.POST)
	public @ResponseBody JsonResponse brandDelete(Brand brand, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_BRAND, brand);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			brandService.delete(brand, userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}


}