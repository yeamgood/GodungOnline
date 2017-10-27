package com.yeamgood.godungonline.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.bean.MenuCode;
import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Country;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.Province;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.CountryService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProvinceService;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Controller
public class MenuController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String MENU = "menuBean";
	
	private static final String LOG_MENU = "menu:{}";
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	ProvinceService provinceService;
	
	@Autowired
	CountryService countryService;
	
	@RequestMapping(value="/admin/menu", method = RequestMethod.GET)
	public ModelAndView userMenu(HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findOneByMenuCode(MenuCode.ADMIN_MENU.toString());
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.setViewName("admin/menu");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/admin/menu/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userMenuList(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT,datatableRequest);
		List<Menu> menuList = menuService.findAllOrderBySequenceAsc();
		List<Menu> menuListSort = new ArrayList<>();
		
		for (Menu menuMain : menuList) {
			if(StringUtils.isBlank(menuMain.getParentCode())) {
				menuListSort.add(menuMain);
				for (Menu menuSub : menuList) {
					if(StringUtils.isNotBlank(menuSub.getParentCode()) && StringUtils.equals(menuMain.getMenuCode(), menuSub.getParentCode())){
						menuListSort.add(menuSub);
					}
				}
			}
		}
		
		for (Menu menuTemp : menuListSort) {
			menuTemp.setMenuName(getMessageSource(menuTemp.getMessageCode()));
		}
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(menuListSort));
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	public String getMessageSource(String messageCode) {
		String message = "";
		try {
			message = messageSource.getMessage(messageCode,null,LocaleContextHolder.getLocale());
		} catch (Exception e) {
			message = "";
		}
		return message;
	}
	
	@RequestMapping(value="/admin/menu/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userMenuDelete(Menu menu,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, menu);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			User userSession = (User) session.getAttribute("user");
			menuService.delete(menu.getMenuIdEncrypt(), userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/admin/menu/manage/{menuIdEncrypt}", method = RequestMethod.GET)
	public ModelAndView userMenuLoad(Model model,HttpSession session, @PathVariable String menuIdEncrypt) throws GodungIdException {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, menuIdEncrypt);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findOneByMenuCode(MenuCode.ADMIN_MENU.toString());
		User userSession = (User) session.getAttribute("user");
		
		Menu menuTemp = menuService.findByIdEncrypt(menuIdEncrypt, userSession);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject(MENU,menuTemp);
		modelAndView.addObject(Constants.PROVINCE_DROPDOWN,provinceDropdown);
		modelAndView.addObject(Constants.COUNTRY_DROPDOWN,countryDropdown);
		modelAndView.setViewName("admin/menu_manage");
		logger.debug("O:");
		return modelAndView;
	}
	
	// --------------------------------------------------------------------
	// --- Manage ---------------------------------------------------------
	// --------------------------------------------------------------------
	@RequestMapping(value="/admin/menu/manage", method = RequestMethod.GET)
	public ModelAndView userMenuManage(Model model,HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findOneByMenuCode(MenuCode.ADMIN_MENU.toString());
		
		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute(MENU)) {
			logger.debug("New Object");
			Menu menuTemp = new Menu();
			modelAndView.addObject(MENU,menuTemp);
	    }
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.setViewName("admin/menu_manage");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value = "/admin/menu/manage", method = RequestMethod.POST)
	public ModelAndView userMenuManage(@Valid Menu menu, BindingResult bindingResult,RedirectAttributes redirectAttributes , HttpSession session) {
		logger.debug("I:");
		logger.debug(LOG_MENU, menu);
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		if (bindingResult.hasErrors()) {
			logger.debug("bindingResult error");
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.menuBean", bindingResult);
			redirectAttributes.addFlashAttribute(MENU, menu);
			modelAndView.setViewName("redirect:/admin/menu/manage");
		} else {
			try {
				logger.debug("save");
				userSession = (User) session.getAttribute("user");
				menuService.save(menu, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_SAVE_SUCCESS));
				modelAndView.setViewName("redirect:/admin/menu/manage/" + menu.getMenuIdEncrypt());
			} catch (Exception e) {
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_SAVE_ERROR));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.menuBean", bindingResult);
				redirectAttributes.addFlashAttribute(MENU, menu);
				modelAndView.setViewName("redirect:/admin/menu/manage");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/admin/menu/manage/delete", method=RequestMethod.POST)
	public ModelAndView userMenuIndividualDelete(Menu menu,HttpSession session, RedirectAttributes redirectAttributes) {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, menu);
		ModelAndView modelAndView = new ModelAndView();
		try {
			User userSession = (User) session.getAttribute("user");
			menuService.delete(menu.getMenuIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_DELETE_SUCCESS));
			modelAndView.setViewName("redirect:/admin/menu");
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_DELETE_ERROR));
			modelAndView.setViewName("redirect:/admin/menu/manage/" + AESencrpUtils.encryptLong(menu.getMenuId()));
		}
		logger.debug("O");
		return modelAndView;
	}
	
	
	
}