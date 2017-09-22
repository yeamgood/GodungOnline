package com.yeamgood.godungonline.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.UserService;

@Controller
@SessionAttributes("user")
public class LoginController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			                  @RequestParam(value = "logout", required = false) String logout, 
			                  User user, BindingResult bindingResult,HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView(); 
		if (error != null) {
			bindingResult.rejectValue("email", "error.user", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
		}
		if (logout != null) {
			modelAndView.addObject("pnotify",new Pnotify(messageSource,PnotifyType.SUCCESS,"message.success.logout"));
		}
		Set<String> roles = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		if(roles.contains("ADMIN")) {
			modelAndView.setViewName("redirect:/admin/home");
		}else if(roles.contains("USER")) {
			modelAndView.setViewName("redirect:/user/home");
		}else {
			modelAndView.setViewName("login");
		}
		
		modelAndView.addObject("user", user);
		return modelAndView;
	}

	@RequestMapping("/loginAuthorizeRole")
    public ModelAndView defaultAfterLogin() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());
		logger.info("[USER][Loggin] email:{} role:{}",auth.getName(),roles);
		User user = userService.findUserByEmail(auth.getName());
		if(roles.contains("ADMIN")) {
			modelAndView.setViewName("redirect:/admin/home?lang="+user.getLanguage());
		}else if(roles.contains("USER")) {
			modelAndView.setViewName("redirect:/user/home?lang="+user.getLanguage());
		}else {
			modelAndView.setViewName("redirect:/login");
		}
		return modelAndView;
    }
	
	@RequestMapping(value="/admin/home", method = RequestMethod.GET)
	public ModelAndView adminHome(RedirectAttributes redirectAttributes){
		return homePage();
	}

	@RequestMapping(value="/user/home", method = RequestMethod.GET)
	public ModelAndView userHome(RedirectAttributes redirectAttributes){
		return homePage();
	}
	
	public ModelAndView homePage() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		manageSelectGodungAndRole(user);
		manageMenu(user);
		Menu menu = menuService.findById(Constants.MENU_HOME_ID);
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject("user",user);
		modelAndView.setViewName("home");
		return modelAndView;
	}
	
	@RequestMapping(value="/logoutSuccess", method = RequestMethod.GET)
	public ModelAndView logoutPage (HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView(); 
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
		redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"app.action.logout"));
	    modelAndView.setViewName("redirect:/login");
	    return modelAndView;
	}
	
	private void manageMenu(User user) {
		List<Menu> subMenuList;
		List<Menu> menuList = new ArrayList<>();
		for (Menu menu : user.getRole().getMenuList()) {
			if(menu.getParentId() == null) {
				menuList.add(menu);
			}
		}
		for(Menu menu : menuList) {
			subMenuList = new ArrayList<>();
			for (Menu subMenu : user.getRole().getMenuList()) {
				if(subMenu.getParentId() != null && subMenu.getParentId().equals(menu.getId())) {
					subMenuList.add(subMenu);
				}
			}
			menu.setMenuList(subMenuList);
		}
		user.getRole().setMenuList(menuList);
		
	}
	
	private void manageSelectGodungAndRole(User user) {
		if(user.getGodungUserRoleList().size() == 1) {
			user.setGodung(user.getGodungUserRoleList().get(0).getGodung());
			user.setRole(user.getGodungUserRoleList().get(0).getRole());
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