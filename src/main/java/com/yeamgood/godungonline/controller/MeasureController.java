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
import com.yeamgood.godungonline.model.Measure;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.MeasureService;
import com.yeamgood.godungonline.service.MenuService;

@Controller
public class MeasureController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Long MENU_CATEGORY_ID = (long) 13;
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	MeasureService measureService;
	
	@RequestMapping(value="/user/measure", method = RequestMethod.GET)
	public ModelAndView userMeasure(HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(MENU_CATEGORY_ID);
		List<Measure> measureList = measureService.findAllByGodungGodungIdOrderByMeasureNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("measureList", measureList);
		modelAndView.setViewName("user/measure");
		logger.debug("O");
		return modelAndView;
	}
	
	
	@RequestMapping(value="/user/measure/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userMeasureListtest(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<Measure> measureList = measureService.findAllByGodungGodungIdOrderByMeasureNameAsc(godungId);
		logger.debug("O:measureList" + measureList.size());
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(measureList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		return result;
	}
	
	@RequestMapping(value="/user/measure/save", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userMeasureAdd(@Valid Measure measure,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug("I" + measure.toString());
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
				measureService.save(measure, userSession);
				
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
	
	@RequestMapping(value="/user/measure/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userMeasureDelete(Measure measure,HttpSession session){
		logger.debug("I");
		logger.debug("I" + measure.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(measure.getMeasureId() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			measureService.delete(measure, userSession);
			
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
	
	@RequestMapping(value="/user/measure/load", method=RequestMethod.GET)
	public @ResponseBody JsonResponse load(Measure measure,HttpSession session){
		logger.debug("I");
		logger.debug("I" + measure.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(measure.getMeasureId() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			Measure measureTemp = measureService.findById(measure.getMeasureId(), userSession);
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(measureTemp);
		} catch (Exception e) {
			logger.error("error:",e);
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/measure/load/add", method=RequestMethod.GET)
	public @ResponseBody JsonResponse loadAdd(HttpSession session){
		logger.debug("I");
		Pnotify pnotify;
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Measure measure = new Measure();
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(measure);
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