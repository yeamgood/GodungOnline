package com.yeamgood.godungonline.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yeamgood.godungonline.bean.Pnotify;
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
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult,RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user", messageSource.getMessage("validation.required.email.registered",null,LocaleContextHolder.getLocale()));
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			userService.saveUser(user);
			Locale userLocale = LocaleContextHolder.getLocale();
			Pnotify pnotify = new Pnotify();
			pnotify.setTitle(messageSource.getMessage("pnotify.title.success",null,userLocale));
			pnotify.setType(messageSource.getMessage("pnotify.type.success",null,userLocale));
			pnotify.setText(messageSource.getMessage("message.resetpassword.success",null,userLocale));
			redirectAttributes.addFlashAttribute(pnotify);
			modelAndView.setViewName("redirect:/login");
		}
		return modelAndView;
	}

}