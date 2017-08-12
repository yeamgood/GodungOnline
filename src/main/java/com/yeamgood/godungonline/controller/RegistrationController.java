package com.yeamgood.godungonline.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.UserService;

@Controller
public class RegistrationController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    MessageSource messageSource;
	
	@RequestMapping(value="/registration", method = RequestMethod.GET)
	public ModelAndView registration(){
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("registration");
		return modelAndView;
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user", messageSource.getMessage("validation.required.email.registered",null,LocaleContextHolder.getLocale()));
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", messageSource.getMessage("message.user.register.success",null,LocaleContextHolder.getLocale()));
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("registration");
		}
		return modelAndView;
	}

}