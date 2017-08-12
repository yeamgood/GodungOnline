package com.yeamgood.godungonline.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.UserService;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    MessageSource messageSource;
	
//	@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
//	public ModelAndView login(){
//		ModelAndView modelAndView = new ModelAndView();
//		User user = new User();
//		modelAndView.addObject("user", user);
//		modelAndView.setViewName("login");
//		return modelAndView;
//	}
	
	@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			                  @RequestParam(value = "logout", required = false) String logout, 
			                  User user, BindingResult bindingResult,HttpServletRequest request) {
		ModelAndView model = new ModelAndView(); 
		if (error != null) {
			bindingResult.rejectValue("email", "error.user", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		//User user = new User();
		model.addObject("user", user);
		model.setViewName("login");
		return model;
	}
	
	//CUSTOMIZE THE ERROR MESSAGE
	private String getErrorMessage(HttpServletRequest request, String key){
		Exception exception = (Exception) request.getSession().getAttribute(key);
		String error = "";
		if (exception instanceof BadCredentialsException) {
			error = "Invalid username and password!";
		}else if(exception instanceof LockedException) {
			error = exception.getMessage();
		}else{
			error = "Invalid username and password!";
		}
		return error;
	}
	
	@RequestMapping(value="/admin/home", method = RequestMethod.GET)
	public ModelAndView home(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
		modelAndView.setViewName("home");
		return modelAndView;
	}

}