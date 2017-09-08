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

import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.model.Price;
import com.yeamgood.godungonline.service.BrandService;
import com.yeamgood.godungonline.service.MenuService;

@Controller
public class PriceController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	BrandService brandService;
	
	@RequestMapping(value="/user/price/select", method=RequestMethod.POST)
	public @ResponseBody JsonResponse select(@Valid Price price,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug("I" + price.toString());
		//logger.debug("I" + price.getMeasure().toString());
		Pnotify pnotify;
		JsonResponse jsonResponse = new JsonResponse();
		
		if (bindingResult.hasErrors()) {
			String errorMsg = "";
			List<FieldError> errors = bindingResult.getFieldErrors();
		    for (FieldError error : errors ) {
		    		errorMsg += error.getField() + " - " + error.getDefaultMessage();
		    }
		    logger.debug("errorMsg :" + errorMsg);
		    pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
		    pnotify.setText(errorMsg);
		    
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(errors);
		}else {
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(price);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	

}