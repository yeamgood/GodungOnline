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
import com.yeamgood.godungonline.model.Measure;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.MeasureService;
import com.yeamgood.godungonline.service.MenuService;

@Controller
public class MeasureController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String LOG_MEASURE = "measure:{}";
	

	@Autowired
	MessageSource messageSource;

	@Autowired
	MenuService menuService;

	@Autowired
	MeasureService measureService;

	@RequestMapping(value = "/user/measure", method = RequestMethod.GET)
	public ModelAndView measure(HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findOneByMenuCode(MenuCode.MEASURE.toString());
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.setViewName("user/measure");
		logger.debug("O");
		return modelAndView;
	}

	@RequestMapping(value = "/user/measure/list", method = RequestMethod.GET)
	public @ResponseBody String measureList(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException  {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		Long godungId = userSession.getGodung().getGodungId();
		List<Measure> measureList = measureService.findAllByGodungGodungIdOrderByMeasureNameAsc(godungId);

		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(measureList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value = "/user/measure/load", method = RequestMethod.GET)
	public @ResponseBody JsonResponse measureLoad(Measure measure, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_MEASURE, measure);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			Measure measureTemp = new Measure();
			if (!StringUtils.isBlank(measure.getMeasureIdEncrypt())) {
				measureTemp = measureService.findByIdEncrypt(measure.getMeasureIdEncrypt(), userSession);
			}
			jsonResponse.setObject(Constants.STATUS_SUCCESS, measureTemp);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}

	@RequestMapping(value = "/user/measure/save", method = RequestMethod.POST)
	public @ResponseBody JsonResponse measureSave(@Valid Measure measure, BindingResult bindingResult,HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_MEASURE, measure);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		} else {
			try {
				measureService.save(measure, userSession);
				jsonResponse.setSaveSuccess(messageSource);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR, e);
				jsonResponse.setSaveError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}

	@RequestMapping(value = "/user/measure/delete", method = RequestMethod.POST)
	public @ResponseBody JsonResponse measureDelete(Measure measure, HttpSession session) {
		logger.debug("I");
		logger.debug(LOG_MEASURE, measure);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute(Constants.SESSION_USER);
		try {
			measureService.delete(measure, userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR, e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}


}