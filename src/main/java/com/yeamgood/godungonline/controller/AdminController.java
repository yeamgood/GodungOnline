package com.yeamgood.godungonline.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.UserRepository;

@Controller
public class AdminController {
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(value="/admin/user", method = RequestMethod.GET)
	public ModelAndView userHome(){
		ModelAndView modelAndView = new ModelAndView();
		
		List<User> userList = userRepository.findAll();
		
		modelAndView.addObject("userList",userList);
		modelAndView.setViewName("admin/user");
		return modelAndView;
	}

}