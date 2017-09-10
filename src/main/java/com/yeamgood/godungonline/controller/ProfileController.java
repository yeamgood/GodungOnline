package com.yeamgood.godungonline.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yeamgood.godungonline.bean.CommonType;
import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.form.ProfileForm;
import com.yeamgood.godungonline.model.Common;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.CommonService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProfileService;

@Controller
public class ProfileController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Long MENU_PROFILE_ID = (long) 51;
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	ProfileService profileService;
	
	@Autowired
	CommonService commonService;
	
	@RequestMapping(value="/user/profile", method = RequestMethod.GET)
	public ModelAndView userProfile(Model model,HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		
		//INITIAL DATA
		Menu menu = menuService.findById(MENU_PROFILE_ID);
		modelAndView.addObject("menu",menu);
		List<Common> languageList = commonService.findByType(CommonType.LANGUAGE.toString());
		modelAndView.addObject("languageList",languageList);
		
		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute("profileForm")) {
			ProfileForm profileForm = new ProfileForm();
			User userSession = (User) session.getAttribute("user");
			profileService.loadProfile(profileForm, userSession.getId(),userSession.getGodung().getGodungId());
			modelAndView.addObject("profileForm",profileForm);
	    }
		modelAndView.setViewName("user/profile");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/profile", method = RequestMethod.POST)
	public ModelAndView userProfileSave(@Valid ProfileForm profileForm,BindingResult bindingResult,HttpSession session,RedirectAttributes redirectAttributes){
		logger.debug("I");
		String language = "";
		ModelAndView modelAndView = new ModelAndView();
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
			redirectAttributes.addFlashAttribute("profileForm", profileForm);
		} else {
			try {
				User userSession = (User) session.getAttribute("user");
				profileService.updateProfile(profileForm, userSession.getId(),userSession.getGodung().getGodungId());
				userSession.setName(profileForm.getName());
				userSession.getGodung().setGodungName(profileForm.getGodungName());
				userSession.setLanguage(profileForm.getLanguage());
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.save.success"));
				language = "?lang=" + profileForm.getLanguage();
			} catch (Exception e) {
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error"));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute("profileForm", profileForm);
			}
		}
		modelAndView.setViewName("redirect:/user/profile" + language);
		logger.debug("O");
		return modelAndView;
	}

}