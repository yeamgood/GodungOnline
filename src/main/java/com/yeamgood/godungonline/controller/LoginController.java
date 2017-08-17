package com.yeamgood.godungonline.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yeamgood.godungonline.model.Godung;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.UserService;

@Controller
@SessionAttributes("user")
public class LoginController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    MessageSource messageSource;
	
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
		model.addObject("user", user);
		model.setViewName("login");
		return model;
	}
	
	@RequestMapping("/authorizeRole")
    public ModelAndView defaultAfterLogin() {
		ModelAndView modelAndView = new ModelAndView();
		Set<String> roles = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		if(roles.contains("ADMIN")) {
			modelAndView.setViewName("redirect:/admin/home");
		}else if(roles.contains("USER")) {
			modelAndView.setViewName("redirect:/user/home");
		}else {
			modelAndView.setViewName("redirect:/login");
		}
		return modelAndView;
    }
	
	@RequestMapping(value="/admin/home", method = RequestMethod.GET)
	public ModelAndView adminHome(RedirectAttributes redirectAttributes){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		
		manageSelectGodung(user);
		
		modelAndView.addObject("user",user);
		modelAndView.addObject("userName", "Welcome " + user.getName());
		modelAndView.setViewName("admin/home");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/home", method = RequestMethod.GET)
	public ModelAndView userHome(RedirectAttributes redirectAttributes){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		
		manageSelectGodung(user);
		
		modelAndView.addObject("user",user);
		modelAndView.addObject("userName", "Welcome " + user.getName() );
		modelAndView.setViewName("user/home");
		return modelAndView;
	}
	
	private void manageSelectGodung(User user) {
		if(user.getGodungs().size() == 1) {
			user.setGodung(user.getGodungs().get(0));
		}else if(user.getGodungs().size() > 1) {
			//TODO Multi Godung
		}else {
			//TODO ERROR
		}
	}
	
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

}