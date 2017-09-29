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
import com.yeamgood.godungonline.datatables.UserDatatables;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.CommonService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.UserService;

@Controller
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String LOG_CATEGORY = "user:{}";
	

	@Autowired
	MessageSource messageSource;

	@Autowired
	MenuService menuService;

	@Autowired
	UserService userService;
	
	@Autowired
	CommonService commonService;

	@RequestMapping(value = "/admin/user", method = RequestMethod.GET)
	public ModelAndView user(HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(Constants.MENU_USER_ID);
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.setViewName("admin/user");
		logger.debug("O");
		return modelAndView;
	}

	@RequestMapping(value = "/admin/user/list", method = RequestMethod.GET)
	public @ResponseBody String userList(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException  {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		List<User> userList = userService.findAll();
		
		UserDatatables userDatatables;
		List<UserDatatables> userDatatablesList = new ArrayList<>();
		for (User obj : userList) {
			userDatatables = new UserDatatables();
			userDatatables.setDatatableByModel(obj);
			userDatatablesList.add(userDatatables);
		}

		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(userDatatablesList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}

	@RequestMapping(value = "/admin/user/load", method = RequestMethod.GET)
	public @ResponseBody JsonResponse userLoad(User user, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_CATEGORY, user);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			User userTemp = new User();
			if (!StringUtils.isBlank(user.getUserIdEncrypt())) {
				userTemp = userService.findByIdEncrypt(user.getUserIdEncrypt(), userSession);
			}
			jsonResponse.setObject(Constants.STATUS_SUCCESS, userTemp);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}

	@RequestMapping(value = "/admin/user/save", method = RequestMethod.POST)
	public @ResponseBody JsonResponse userSave(@Valid User user, BindingResult bindingResult, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_CATEGORY, user);
		JsonResponse jsonResponse = new JsonResponse();
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		} else {
			try {
				userService.saveUser(user);
				jsonResponse.setSaveSuccess(messageSource);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR, e);
				jsonResponse.setSaveError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}

	@RequestMapping(value = "/admin/user/delete", method = RequestMethod.POST)
	public @ResponseBody JsonResponse userDelete(User user, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_CATEGORY, user);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			//userService.delete(user, userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}

}