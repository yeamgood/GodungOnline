package com.yeamgood.godungonline.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.UserRepository;

@Controller
public class ProfileController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
    MessageSource messageSource;
	
	@RequestMapping(value="/user/profile", method = RequestMethod.GET)
	public ModelAndView userProfile(User user){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("user",user);
		modelAndView.setViewName("user/profile");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/profile/save", method = RequestMethod.POST)
	public ModelAndView userProfileSave(User user,HttpSession session,RedirectAttributes redirectAttributes){
		ModelAndView modelAndView = new ModelAndView();
		try {
			User userSession = (User) session.getAttribute("user");
			User userTemp = userRepository.findOne(userSession.getId());
			userTemp.setName(user.getName());
			userRepository.save(userTemp);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.save.success"));
			userSession.setName(user.getName());
		} catch (Exception e) {
			logger.error("error",e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error"));
		}
		modelAndView.setViewName("redirect:/user/profile");
		return modelAndView;
	}
	

}