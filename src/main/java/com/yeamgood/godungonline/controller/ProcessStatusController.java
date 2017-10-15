package com.yeamgood.godungonline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import com.yeamgood.godungonline.service.ProcessStatusService;

@Controller
public class ProcessStatusController {
	
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	ProcessStatusService processStatusService;
	
	
}