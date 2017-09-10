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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.model.Address;
import com.yeamgood.godungonline.model.Country;
import com.yeamgood.godungonline.model.Rolegodung;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.Province;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.CountryService;
import com.yeamgood.godungonline.service.RolegodungService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProvinceService;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Controller
public class RolegodungController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Long MENU_ID = (long) 52;
	private final Long COUNTRY_THAILAND = (long) 217;
	
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
	public ModelAndView userRolegodung(HttpSession session) throws Exception{
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(MENU_ID);
		List<Rolegodung> rolegodungList = rolegodungService.findAllByGodungGodungIdOrderByRolegodungNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("rolegodungList", rolegodungList);
		modelAndView.setViewName("user/rolegodung");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/rolegodung/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userRolegodungListtest(DataTablesRequest datatableRequest, HttpSession session) throws Exception{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<Rolegodung> rolegodungList = rolegodungService.findAllByGodungGodungIdOrderByRolegodungNameAsc(godungId);
		logger.debug("O:rolegodungList" + rolegodungList.size());
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(rolegodungList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		return result;
	}
	
	@RequestMapping(value="/user/rolegodung/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userRolegodungDelete(Rolegodung rolegodung,HttpSession session){
		logger.debug("I");
		logger.debug("I" + rolegodung.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(rolegodung.getRolegodungIdEncrypt() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		try {
			userSession = (User) session.getAttribute("user");
			rolegodungService.delete(rolegodung.getRolegodungIdEncrypt(), userSession);
			
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.delete.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(pnotify);
		} catch (Exception e) {
			logger.error("error:",e);
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/rolegodung/manage/{idEncrypt}", method = RequestMethod.GET)
	public ModelAndView userRolegodungLoad(Model model,HttpSession session, @PathVariable String idEncrypt) throws NumberFormatException, Exception{
		logger.debug("I:");
		logger.debug("I:idEncrypt" + idEncrypt);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		User userSession = (User) session.getAttribute("user");
		
		Rolegodung rolegodung = rolegodungService.findByIdEncrypt(idEncrypt,userSession);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("rolegodung",rolegodung);
		modelAndView.addObject("provinceDropdown",provinceDropdown);
		modelAndView.addObject("countryDropdown",countryDropdown);
		modelAndView.setViewName("user/rolegodung_manage");
		logger.debug("O:");
		return modelAndView;
	}
	
	// --------------------------------------------------------------------
	// --- PERSON ---------------------------------------------------------
	// --------------------------------------------------------------------
	@RequestMapping(value="/user/rolegodung/manage", method = RequestMethod.GET)
	public ModelAndView userRolegodungPerson(Model model,HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		
		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute("rolegodung")) {
			logger.debug("New Object");
			Country country = new Country();
			country.setCountryId(COUNTRY_THAILAND);
			
			Address address = new Address();
			address.setCountry(country);
			
			Rolegodung rolegodung = new Rolegodung();
			rolegodung.setAddress(address);
			modelAndView.addObject("rolegodung",rolegodung);
	    }
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("countryDropdown",countryDropdown);
		modelAndView.addObject("provinceDropdown",provinceDropdown);
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
			redirectAttributes.addFlashAttribute("rolegodung", rolegodung);
			modelAndView.setViewName("redirect:/user/rolegodung/manage");
		} else {
			try {
				logger.debug("save");
				userSession = (User) session.getAttribute("user");
				rolegodungService.save(rolegodung, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.save.success"));
				modelAndView.setViewName("redirect:/user/rolegodung/manage/" + rolegodung.getRolegodungIdEncrypt());
			} catch (Exception e) {
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error"));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute("rolegodung", rolegodung);
				modelAndView.setViewName("redirect:/user/rolegodung/manage");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/rolegodung/manage/delete", method=RequestMethod.POST)
	public ModelAndView userRolegodungIndividualDelete(Rolegodung rolegodung,HttpSession session, RedirectAttributes redirectAttributes) throws Exception{
		logger.debug("I");
		logger.debug("I" + rolegodung.toString());
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		
		if(rolegodung.getRolegodungId() == null) {
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/rolegodung");
		}
		try {
			userSession = (User) session.getAttribute("user");
			rolegodungService.delete(rolegodung.getRolegodungIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.delete.success"));
			modelAndView.setViewName("redirect:/user/rolegodung");
		} catch (Exception e) {
			logger.error("error:",e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/rolegodung/manage/" + AESencrpUtils.encryptLong(rolegodung.getRolegodungId()));
		}
		logger.debug("O");
		return modelAndView;
	}
	
	
	
}