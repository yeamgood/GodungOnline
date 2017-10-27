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
import com.yeamgood.godungonline.datatables.RoleDatatables;
import com.yeamgood.godungonline.form.RoleForm;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.Role;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.RoleService;

@Controller
public class RoleController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String LOG_ROLE = "role:{}";
	

	@Autowired
	MessageSource messageSource;

	@Autowired
	MenuService menuService;

	@Autowired
	RoleService roleService;

	@RequestMapping(value = "/admin/role", method = RequestMethod.GET)
	public ModelAndView role(HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findOneByMenuCode(MenuCode.ADMIN_ROLES.toString());
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.setViewName("admin/role");
		logger.debug("O");
		return modelAndView;
	}

	@RequestMapping(value = "/admin/role/list", method = RequestMethod.GET)
	public @ResponseBody String roleList(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException  {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		List<Role> roleList = roleService.findAllOrderByRoleNameAsc();
		
		List<RoleDatatables> roleDatatablesList = new ArrayList<>();
		RoleDatatables roleDatatables;
		for (Role role : roleList) {
			roleDatatables = new RoleDatatables();
			roleDatatables.setDatatableByModel(role);
			roleDatatablesList.add(roleDatatables);
		}

		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(roleDatatablesList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value = "/admin/role/load", method = RequestMethod.GET)
	public @ResponseBody JsonResponse roleLoad(Role role, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_ROLE, role);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Role roleTemp = new Role();
			RoleForm roleForm = new RoleForm();
			if (!StringUtils.isBlank(role.getRoleIdEncrypt())) {
				roleTemp = roleService.findByIdEncrypt(role.getRoleIdEncrypt());
				roleForm.mapByModel(roleTemp);
			}
			jsonResponse.setObject(Constants.STATUS_SUCCESS, roleForm);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}

	@RequestMapping(value = "/admin/role/save", method = RequestMethod.POST)
	public @ResponseBody JsonResponse roleSave(@Valid Role role, BindingResult bindingResult,HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_ROLE, role);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		} else {
			try {
				roleService.save(role, userSession);
				jsonResponse.setSaveSuccess(messageSource);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR, e);
				jsonResponse.setSaveError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}

	@RequestMapping(value = "/admin/role/delete", method = RequestMethod.POST)
	public @ResponseBody JsonResponse roleDelete(Role role, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_ROLE, role);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			roleService.delete(role, userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}


}