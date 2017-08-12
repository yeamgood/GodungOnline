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
public class ForgotPasswordController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    MessageSource messageSource;

	@RequestMapping(value={"/forgot_password"}, method = RequestMethod.GET)
	public ModelAndView forgotPassword(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("user", new User());
		modelAndView.setViewName("forgot_password");
		return modelAndView;
	}
	
	@RequestMapping(value={"/forgot_password"}, method = RequestMethod.POST)
	public ModelAndView forgotPasswordSendEmail(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (!bindingResult.hasFieldErrors("email") && userExists == null) {
			bindingResult.rejectValue("email", "error.user", messageSource.getMessage("validation.required.email.not.registered",null,LocaleContextHolder.getLocale()));
		}
		if (bindingResult.hasFieldErrors("email")) {
			modelAndView.setViewName("forgot_password");
		} else {
			//TODO funciont send email
			modelAndView.addObject("successMessage", messageSource.getMessage("message.user.register.success",null,LocaleContextHolder.getLocale()));
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("login");
		}
		return modelAndView;
	}
	
}