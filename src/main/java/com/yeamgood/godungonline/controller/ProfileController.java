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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.form.ProfileForm;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.CommonService;
import com.yeamgood.godungonline.service.ProfileService;

@Controller
public class ProfileController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ProfileService profileService;
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	CommonService commonService;
	
	@RequestMapping(value="/user/profile", method = RequestMethod.GET)
	public ModelAndView userProfile(HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		ProfileForm profileForm = new ProfileForm();
		profileService.loadProfile(profileForm, userSession.getId(),userSession.getGodung().getId());
		modelAndView.addObject("profileForm",profileForm);
		modelAndView.setViewName("user/profile");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/profile/save", method = RequestMethod.POST)
	public ModelAndView userProfileSave(@Valid ProfileForm profileForm,BindingResult bindingResult,HttpSession session,RedirectAttributes redirectAttributes){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		if (bindingResult.hasErrors()) {
			logger.debug("bindingResultError");
			User userSession = (User) session.getAttribute("user");
			ProfileForm profileFormLoad = new ProfileForm();
			profileService.loadProfile(profileFormLoad, userSession.getId(),userSession.getGodung().getId());
			
			profileForm.setEmail(profileFormLoad.getEmail());
			profileForm.setLanguageList(profileFormLoad.getLanguageList());
			modelAndView.setViewName("user/profile");
		} else {
			try {
				User userSession = (User) session.getAttribute("user");
				profileService.updateProfile(profileForm, userSession.getId(),userSession.getGodung().getId());
				userSession.setName(profileForm.getName());
				userSession.getGodung().setName(profileForm.getGodungName());
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.save.success"));
			} catch (Exception e) {
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error"));
			}
			modelAndView.setViewName("redirect:/user/profile");
		}
		logger.debug("O");
		return modelAndView;
	}

}