package com.yeamgood.godungonline.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.form.ApproverForm;

@Controller
public class ApproverController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    MessageSource messageSource;
	
	@RequestMapping(value="/user/approver/valid", method=RequestMethod.POST)
	public @ResponseBody JsonResponse validForm(@Valid ApproverForm approverForm,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT,approverForm);
		JsonResponse jsonResponse = new JsonResponse();
		
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		}else {
			try {
				jsonResponse.setStatus(Constants.STATUS_SUCCESS);
				jsonResponse.setResult(approverForm);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR,e);
				jsonResponse.setLoadError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}
	
}