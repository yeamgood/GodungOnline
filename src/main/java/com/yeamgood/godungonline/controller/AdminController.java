package com.yeamgood.godungonline.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.Role;
import com.yeamgood.godungonline.repository.MenuRepository;
import com.yeamgood.godungonline.repository.RoleRepository;
import com.yeamgood.godungonline.repository.UserRepository;

@Controller
public class AdminController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MenuRepository menuRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
//	@RequestMapping(value="/admin/user", method = RequestMethod.GET)
//	public ModelAndView userHome(){
//		ModelAndView modelAndView = new ModelAndView();
//		List<User> userList = userRepository.findAll();
//		modelAndView.addObject("userList",userList);
//		modelAndView.setViewName("admin/user");
//		return modelAndView;
//	}
	
	@RequestMapping(value="/admin/role", method = RequestMethod.GET)
	public ModelAndView userRole(){
		ModelAndView modelAndView = new ModelAndView();
		List<Role> roleList = roleRepository.findAll();
		modelAndView.addObject("roleList",roleList);
		modelAndView.setViewName("admin/role");
		return modelAndView;
	}

	@RequestMapping(value="/admin/menu", method = RequestMethod.GET)
	public ModelAndView userMenu(){
		ModelAndView modelAndView = new ModelAndView();
		List<Menu> menuList = menuRepository.findAll();
		modelAndView.addObject("menuList",menuList);
		modelAndView.setViewName("admin/menu");
		return modelAndView;
	}

}