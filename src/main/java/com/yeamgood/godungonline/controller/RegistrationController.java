package com.yeamgood.godungonline.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.UserService;

@Controller
public class RegistrationController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
		logger.debug("I:");
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			logger.debug("userExists");
			bindingResult.rejectValue("email", "error.user", messageSource.getMessage("validation.required.email.registered",null,LocaleContextHolder.getLocale()));
		}
		if(!bindingResult.hasErrors() && !user.getPassword().equals(user.getConfirmPassword())) {
			String messageError = messageSource.getMessage("validation.required.password.compare",null,LocaleContextHolder.getLocale());
			bindingResult.rejectValue("password", "error.resetPasswordForm", messageError);
		}
		if (bindingResult.hasErrors()) {
			logger.debug("bindingResult error");
			modelAndView.setViewName("registration");
		} else {
			try {
				logger.debug("save");
				userService.saveUser(user);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"message.user.register.success"));
				modelAndView.setViewName("redirect:/login");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("error",e);
				modelAndView.addObject("pnotify",new Pnotify(messageSource,PnotifyType.ERROR,"message.error.system"));
				modelAndView.setViewName("registration");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}

}