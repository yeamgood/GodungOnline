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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.yeamgood.godungonline.model.MenuPrivilege;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.MenuPrivilegeService;
import com.yeamgood.godungonline.service.MenuService;

@Controller
public class MenuPrivilegeController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String LOG_MENU_PRIVILEGE = "menuPrivilege:{}";
	

	@Autowired
	MessageSource messageSource;

	@Autowired
	MenuService menuService;

	@Autowired
	MenuPrivilegeService menuPrivilegeService;

	@RequestMapping(value = "/admin/menuPrivilege", method = RequestMethod.GET)
	public ModelAndView menuPrivilege(HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findOneByMenuCode(MenuCode.BRAND.toString());
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.setViewName("admin/menuPrivilege");
		logger.debug("O");
		return modelAndView;
	}

	@RequestMapping(value = "/admin/menuPrivilege/list/{menuIdEncrypt}", method = RequestMethod.GET)
	public @ResponseBody String menuPrivilegeList(@PathVariable String menuIdEncrypt,DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException  {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		List<MenuPrivilege> menuPrivilegeList = menuPrivilegeService.findAllByMenuIdOrderByMenuPrivilegeNameAsc(menuIdEncrypt);

		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(menuPrivilegeList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value = "/admin/menuPrivilege/load", method = RequestMethod.GET)
	public @ResponseBody JsonResponse menuPrivilegeLoad(MenuPrivilege menuPrivilege, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_MENU_PRIVILEGE, menuPrivilege);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			MenuPrivilege menuPrivilegeTemp = new MenuPrivilege();
			if (!StringUtils.isBlank(menuPrivilege.getMenuPrivilegeIdEncrypt())) {
				menuPrivilegeTemp = menuPrivilegeService.findByIdEncrypt(menuPrivilege.getMenuPrivilegeIdEncrypt(), userSession);
			}
			jsonResponse.setObject(Constants.STATUS_SUCCESS, menuPrivilegeTemp);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}

	@RequestMapping(value = "/admin/menuPrivilege/save", method = RequestMethod.POST)
	public @ResponseBody JsonResponse menuPrivilegeSave(@Valid MenuPrivilege menuPrivilege, BindingResult bindingResult,HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_MENU_PRIVILEGE, menuPrivilege);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		if (bindingResult.hasErrors()) {
			logger.debug("bindingResult error");
			jsonResponse.setBinddingResultError(bindingResult);
		} else {
			try {
				menuPrivilegeService.save(menuPrivilege, userSession);
				jsonResponse.setSaveSuccess(messageSource);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR, e);
				jsonResponse.setSaveError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}

	@RequestMapping(value = "/admin/menuPrivilege/delete", method = RequestMethod.POST)
	public @ResponseBody JsonResponse menuPrivilegeDelete(MenuPrivilege menuPrivilege, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_MENU_PRIVILEGE, menuPrivilege);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			menuPrivilegeService.delete(menuPrivilege, userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}


}