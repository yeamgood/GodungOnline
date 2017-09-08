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
import com.yeamgood.godungonline.model.Employee;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.EmployeeService;
import com.yeamgood.godungonline.service.MenuService;

@Controller
public class EmployeeController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Long MENU_ID = (long) 16;
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	EmployeeService employeeService;
	
	@RequestMapping(value="/user/employee", method = RequestMethod.GET)
	public ModelAndView userEmployee(HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(MENU_ID);
		List<Employee> employeeList = employeeService.findAllByGodungGodungIdOrderByEmployeeNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("employeeList", employeeList);
		modelAndView.setViewName("user/employee");
		logger.debug("O");
		return modelAndView;
	}
	
	
	@RequestMapping(value="/user/employee/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userEmployeeListtest(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<Employee> employeeList = employeeService.findAllByGodungGodungIdOrderByEmployeeNameAsc(godungId);
		logger.debug("O:employeeList" + employeeList.size());
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(employeeList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		return result;
	}
	
	@RequestMapping(value="/user/employee/save", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userEmployeeAdd(@Valid Employee employee,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug("I" + employee.toString());
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
				employeeService.save(employee, userSession);
				
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
	
	@RequestMapping(value="/user/employee/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userEmployeeDelete(Employee employee,HttpSession session){
		logger.debug("I");
		logger.debug("I" + employee.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(employee.getEmployeeId() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			employeeService.delete(employee, userSession);
			
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
	
	@RequestMapping(value="/user/employee/load", method=RequestMethod.GET)
	public @ResponseBody JsonResponse load(Employee employee,HttpSession session){
		logger.debug("I");
		logger.debug("I" + employee.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(employee.getEmployeeId() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			Employee employeeTemp = employeeService.findById(employee.getEmployeeId(), userSession);
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(employeeTemp);
		} catch (Exception e) {
			logger.error("error:",e);
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/employee/load/add", method=RequestMethod.GET)
	public @ResponseBody JsonResponse loadAdd(HttpSession session){
		logger.debug("I");
		Pnotify pnotify;
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Employee employee = new Employee();
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(employee);
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