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
import com.yeamgood.godungonline.model.Godung;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.GodungService;
import com.yeamgood.godungonline.service.MenuService;

@Controller
public class GodungController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String LOG = "godung:{}";
	

	@Autowired
	MessageSource messageSource;

	@Autowired
	MenuService menuService;

	@Autowired
	GodungService godungService;

	@RequestMapping(value = "/admin/godung", method = RequestMethod.GET)
	public ModelAndView godung(HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findOneByMenuCode(MenuCode.ADMIN_GODUNG.toString());
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.setViewName("admin/godung");
		logger.debug("O");
		return modelAndView;
	}

	@RequestMapping(value = "/admin/godung/list", method = RequestMethod.GET)
	public @ResponseBody String godungList(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException  {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		List<Godung> godungList = godungService.findAllOrderByGodungNameAsc();

		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(godungList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value = "/admin/godung/load", method = RequestMethod.GET)
	public @ResponseBody JsonResponse godungLoad(Godung godung, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG, godung);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Godung godungTemp = new Godung();
			if (!StringUtils.isBlank(godung.getGodungIdEncrypt())) {
				godungTemp = godungService.findByIdEncrypt(godung.getGodungIdEncrypt());
			}
			jsonResponse.setObject(Constants.STATUS_SUCCESS, godungTemp);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}

	@RequestMapping(value = "/admin/godung/save", method = RequestMethod.POST)
	public @ResponseBody JsonResponse godungSave(@Valid Godung godung, BindingResult bindingResult,HttpSession session) {
		logger.debug("I");
		logger.debug(LOG, godung);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		} else {
			try {
				godungService.save(godung, userSession);
				jsonResponse.setSaveSuccess(messageSource);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR, e);
				jsonResponse.setSaveError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}

	@RequestMapping(value = "/admin/godung/delete", method = RequestMethod.POST)
	public @ResponseBody JsonResponse godungDelete(Godung godung, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG, godung);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			godungService.delete(godung);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}


}