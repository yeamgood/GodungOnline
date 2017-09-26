package com.yeamgood.godungonline.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Address;
import com.yeamgood.godungonline.model.Country;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.Province;
import com.yeamgood.godungonline.model.Rolegodung;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.CountryService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProvinceService;
import com.yeamgood.godungonline.service.RolegodungService;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Controller
public class RolegodungController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String ROLEGODUNG = "rolegodung";
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	RolegodungService rolegodungService;
	
	@Autowired
	ProvinceService provinceService;
	
	@Autowired
	CountryService countryService;
	
	@RequestMapping(value="/user/rolegodung", method = RequestMethod.GET)
	public ModelAndView userRolegodung(HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(Constants.MENU_ROLE_GODUNG_ID);
		List<Rolegodung> rolegodungList = rolegodungService.findAllByGodungGodungIdOrderByRolegodungNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject("rolegodungList", rolegodungList);
		modelAndView.setViewName("user/rolegodung");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/rolegodung/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userRolegodungList(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT,datatableRequest);
		
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<Rolegodung> rolegodungList = rolegodungService.findAllByGodungGodungIdOrderByRolegodungNameAsc(godungId);
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(rolegodungList));
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value="/user/rolegodung/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userRolegodungDelete(Rolegodung rolegodung,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, rolegodung);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			User userSession = (User) session.getAttribute("user");
			rolegodungService.delete(rolegodung.getRolegodungIdEncrypt(), userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/rolegodung/manage/{rolegodungIdEncrypt}", method = RequestMethod.GET)
	public ModelAndView userRolegodungLoad(Model model,HttpSession session, @PathVariable String rolegodungIdEncrypt) throws GodungIdException {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, rolegodungIdEncrypt);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(Constants.MENU_ROLE_GODUNG_ID);
		User userSession = (User) session.getAttribute("user");
		
		Rolegodung rolegodung = rolegodungService.findByIdEncrypt(rolegodungIdEncrypt,userSession);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject(ROLEGODUNG,rolegodung);
		modelAndView.addObject(Constants.PROVINCE_DROPDOWN,provinceDropdown);
		modelAndView.addObject(Constants.COUNTRY_DROPDOWN,countryDropdown);
		modelAndView.setViewName("user/rolegodung_manage");
		logger.debug("O:");
		return modelAndView;
	}
	
	// --------------------------------------------------------------------
	// --- Manage ---------------------------------------------------------
	// --------------------------------------------------------------------
	@RequestMapping(value="/user/rolegodung/manage", method = RequestMethod.GET)
	public ModelAndView userRolegodungPerson(Model model,HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(Constants.MENU_ROLE_GODUNG_ID);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		
		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute(ROLEGODUNG)) {
			logger.debug("New Object");
			Country country = new Country();
			country.setCountryId(Constants.COUNTRY_THAILAND);
			
			Address address = new Address();
			address.setCountry(country);
			
			Rolegodung rolegodung = new Rolegodung();
			modelAndView.addObject(ROLEGODUNG,rolegodung);
	    }
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject(Constants.COUNTRY_DROPDOWN,countryDropdown);
		modelAndView.addObject(Constants.PROVINCE_DROPDOWN,provinceDropdown);
		modelAndView.setViewName("user/rolegodung_manage");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value = "/user/rolegodung/manage", method = RequestMethod.POST)
	public ModelAndView userRolegodungPerson(@Valid Rolegodung rolegodung, BindingResult bindingResult,RedirectAttributes redirectAttributes , HttpSession session) {
		logger.debug("I:");
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		if (bindingResult.hasErrors()) {
			logger.debug("bindingResult error");
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.rolegodung", bindingResult);
			redirectAttributes.addFlashAttribute(ROLEGODUNG, rolegodung);
			modelAndView.setViewName("redirect:/user/rolegodung/manage");
		} else {
			try {
				logger.debug("save");
				userSession = (User) session.getAttribute("user");
				rolegodungService.save(rolegodung, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_SAVE_SUCCESS));
				modelAndView.setViewName("redirect:/user/rolegodung/manage/" + rolegodung.getRolegodungIdEncrypt());
			} catch (Exception e) {
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_SAVE_ERROR));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute(ROLEGODUNG, rolegodung);
				modelAndView.setViewName("redirect:/user/rolegodung/manage");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/rolegodung/manage/delete", method=RequestMethod.POST)
	public ModelAndView userRolegodungIndividualDelete(Rolegodung rolegodung,HttpSession session, RedirectAttributes redirectAttributes) {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, rolegodung);
		ModelAndView modelAndView = new ModelAndView();
		try {
			User userSession = (User) session.getAttribute("user");
			rolegodungService.delete(rolegodung.getRolegodungIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_DELETE_SUCCESS));
			modelAndView.setViewName("redirect:/user/rolegodung");
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_DELETE_ERROR));
			modelAndView.setViewName("redirect:/user/rolegodung/manage/" + AESencrpUtils.encryptLong(rolegodung.getRolegodungId()));
		}
		logger.debug("O");
		return modelAndView;
	}
	
	
	
}