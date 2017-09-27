package com.yeamgood.godungonline.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.form.ApproverForm;
import com.yeamgood.godungonline.model.Approver;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.ApproverService;

@Controller
public class ApproverController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	ApproverService approverService;
	
	@RequestMapping(value="/user/approver/load/{approverIdEncrypt}", method=RequestMethod.GET)
	public @ResponseBody JsonResponse load(@PathVariable String approverIdEncrypt,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT,approverIdEncrypt);
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			User userSession = (User) session.getAttribute("user");
			Approver approver = approverService.findByIdEncrypt(approverIdEncrypt, userSession);
			
			ApproverForm approverForm = new ApproverForm();
			approverForm.setApproverIdEncrypt(approver.getApproverIdEncrypt());
			 
			jsonResponse.setStatus(Constants.STATUS_SUCCESS);
			jsonResponse.setResult(approverForm);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setLoadError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
}