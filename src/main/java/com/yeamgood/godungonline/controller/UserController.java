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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeamgood.godungonline.bean.CommonType;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.bean.MenuCode;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.datatables.UserDatatables;
import com.yeamgood.godungonline.form.UserForm;
import com.yeamgood.godungonline.form.UserPasswordForm;
import com.yeamgood.godungonline.model.Common;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.RoleLogin;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.CommonService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.RoleLoginService;
import com.yeamgood.godungonline.service.UserService;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Controller
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String LOG_USER = "user:{}";
	

	@Autowired
	MessageSource messageSource;

	@Autowired
	MenuService menuService;

	@Autowired
	UserService userService;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	RoleLoginService roleLoginService;

	@RequestMapping(value = "/admin/user", method = RequestMethod.GET)
	public ModelAndView user(HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findOneByMenuCode(MenuCode.ADMIN_HOME.toString());
		
		List<Common> languageList = commonService.findByType(CommonType.LANGUAGE.toString());
		modelAndView.addObject("languageList",languageList);
		
		List<RoleLogin> roleLoginList = roleLoginService.findAll();
		modelAndView.addObject("roleLoginList",roleLoginList);
		
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
		List<Common> languageList = commonService.findByType(CommonType.LANGUAGE.toString());
		
		UserDatatables userDatatables;
		List<UserDatatables> userDatatablesList = new ArrayList<>();
		for (User obj : userList) {
			
			for (Common language : languageList) {
				if(StringUtils.equals(language.getKey(), obj.getLanguage())) {
					obj.setLanguage(language.getValue());
					break;
				}
			}
			
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
		logger.debug(LOG_USER, user);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			logger.debug(user.getUserIdEncrypt());
			UserForm userForm = new UserForm();
			if (!StringUtils.isBlank(user.getUserIdEncrypt())) {
				User userTemp = userService.findByIdEncrypt(user.getUserIdEncrypt(), userSession);
				userForm.mapObjectToForm(userTemp);
			}
			jsonResponse.setObject(Constants.STATUS_SUCCESS, userForm);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}

	@RequestMapping(value = "/admin/user/save", method = RequestMethod.POST)
	public @ResponseBody JsonResponse userSave(@Valid UserForm userForm, BindingResult bindingResult, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_USER, userForm);
		JsonResponse jsonResponse = new JsonResponse();
		
		User userExists = userService.findUserByEmail(userForm.getEmail());
		if (userExists != null && userExists.getUserId().longValue() != AESencrpUtils.decryptLong(userForm.getUserIdEncrypt()).longValue()) {
			logger.debug("userExists");
			bindingResult.rejectValue("email", "error.user", messageSource.getMessage("validation.required.email.registered",null,LocaleContextHolder.getLocale()));
		}
		
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		} else {
			try {
				userService.updateUser(userForm);
				jsonResponse.setSaveSuccess(messageSource);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR, e);
				jsonResponse.setSaveError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value = "/admin/user/changepassword", method = RequestMethod.POST)
	public @ResponseBody JsonResponse userChangePassword(@Valid UserPasswordForm userPasswordForm, BindingResult bindingResult, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_USER, userPasswordForm);
		JsonResponse jsonResponse = new JsonResponse();
		
		if(!bindingResult.hasErrors() && !userPasswordForm.getPassword().equals(userPasswordForm.getConfirmPassword())) {
			String messageError = messageSource.getMessage("validation.required.password.compare",null,LocaleContextHolder.getLocale());
			bindingResult.rejectValue("password", "error.resetPasswordForm", messageError);
		}
		
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		} else {
			try {
				userService.changeUserPassword(userPasswordForm);
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
		logger.debug(LOG_USER, user);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			userService.delete(user);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}

}