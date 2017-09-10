package com.yeamgood.godungonline.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import com.yeamgood.godungonline.bean.CustomerType;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.model.Address;
import com.yeamgood.godungonline.model.Country;
import com.yeamgood.godungonline.model.Customer;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.Province;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.CountryService;
import com.yeamgood.godungonline.service.CustomerService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProvinceService;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Controller
public class CustomerController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Long MENU_ID = (long) 32;
	private final Long COUNTRY_THAILAND = (long) 217;
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	ProvinceService provinceService;
	
	@Autowired
	CountryService countryService;
	
	@RequestMapping(value="/user/customer", method = RequestMethod.GET)
	public ModelAndView userCustomer(HttpSession session) throws Exception{
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(MENU_ID);
		List<Customer> customerList = customerService.findAllByGodungGodungIdOrderByCustomerNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("customerList", customerList);
		modelAndView.setViewName("user/customer");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/customer/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userCustomerListtest(DataTablesRequest datatableRequest, HttpSession session) throws Exception{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<Customer> customerList = customerService.findAllByGodungGodungIdOrderByCustomerNameAsc(godungId);
		logger.debug("O:customerList" + customerList.size());
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(customerList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		return result;
	}
	
	@RequestMapping(value="/user/customer/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userCustomerDelete(Customer customer,HttpSession session){
		logger.debug("I");
		logger.debug("I" + customer.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(customer.getCustomerIdEncrypt() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		try {
			userSession = (User) session.getAttribute("user");
			customerService.delete(customer.getCustomerIdEncrypt(), userSession);
			
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
	
	@RequestMapping(value="/user/customer/manage/{idEncrypt}", method = RequestMethod.GET)
	public ModelAndView userCustomerLoad(Model model,HttpSession session, @PathVariable String idEncrypt) throws NumberFormatException, Exception{
		logger.debug("I:");
		logger.debug("I:idEncrypt" + idEncrypt);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		User userSession = (User) session.getAttribute("user");
		
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		Customer customer = customerService.findByIdEncrypt(idEncrypt,userSession);
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("customer",customer);
		modelAndView.addObject("provinceDropdown",provinceDropdown);
		modelAndView.addObject("countryDropdown",countryDropdown);
		
		if(StringUtils.equals(customer.getCustomerType(), CustomerType.PERSON.toString())) {
			modelAndView.setViewName("user/customer_person");
		}else {
			modelAndView.setViewName("user/customer_company");
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	// --------------------------------------------------------------------
	// --- PERSON ---------------------------------------------------------
	// --------------------------------------------------------------------
	@RequestMapping(value="/user/customer/manage/person", method = RequestMethod.GET)
	public ModelAndView userCustomerPerson(Model model,HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		
		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute("customer")) {
			logger.debug("New Object");
			Country country = new Country();
			country.setCountryId(COUNTRY_THAILAND);
			
			Address address = new Address();
			address.setCountry(country);
			
			Customer customer = new Customer();
			customer.setAddress(address);
			customer.setAddressSend(address);
			modelAndView.addObject("customer",customer);
	    }
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("provinceDropdown",provinceDropdown);
		modelAndView.setViewName("user/customer_person");
		modelAndView.addObject("countryDropdown",countryDropdown);
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value = "/user/customer/manage/person", method = RequestMethod.POST)
	public ModelAndView userCustomerPerson(@Valid Customer customer, BindingResult bindingResult,RedirectAttributes redirectAttributes , HttpSession session) {
		logger.debug("I:");
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		if (bindingResult.hasErrors()) {
			logger.debug("bindingResult error");
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.customer", bindingResult);
			redirectAttributes.addFlashAttribute("customer", customer);
			modelAndView.setViewName("redirect:/user/customer/manage/person");
		} else {
			try {
				logger.debug("save");
				customer.setCustomerType(CustomerType.PERSON.toString());
				userSession = (User) session.getAttribute("user");
				customerService.save(customer, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.save.success"));
				modelAndView.setViewName("redirect:/user/customer/manage/" + customer.getCustomerIdEncrypt());
			} catch (Exception e) {
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error"));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute("customer", customer);
				modelAndView.setViewName("redirect:/user/customer/manage/person");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/customer/manage/person/delete", method=RequestMethod.POST)
	public ModelAndView userCustomerIndividualDelete(Customer customer,HttpSession session, RedirectAttributes redirectAttributes) throws Exception{
		logger.debug("I");
		logger.debug("I" + customer.toString());
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		
		if(customer.getCustomerId() == null) {
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/customer");
		}
		try {
			userSession = (User) session.getAttribute("user");
			customerService.delete(customer.getCustomerIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.delete.success"));
			modelAndView.setViewName("redirect:/user/customer");
		} catch (Exception e) {
			logger.error("error:",e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/customer/manage/" + AESencrpUtils.encryptLong(customer.getCustomerId()));
		}
		logger.debug("O");
		return modelAndView;
	}
	
	// --------------------------------------------------------------------
	// --- COMPANY ---------------------------------------------------------
	// --------------------------------------------------------------------
	@RequestMapping(value="/user/customer/manage/company", method = RequestMethod.GET)
	public ModelAndView userCustomerCompany(Model model,HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();

		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute("customer")) {
			logger.debug("New Object");
			Country country = new Country();
			country.setCountryId(COUNTRY_THAILAND);
			
			Address address = new Address();
			address.setCountry(country);
			
			Customer customer = new Customer();
			customer.setAddress(address);
			customer.setAddressSend(address);
			modelAndView.addObject("customer",customer);
	    }
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("provinceDropdown",provinceDropdown);
		modelAndView.setViewName("user/customer_company");
		modelAndView.addObject("countryDropdown",countryDropdown);
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value = "/user/customer/manage/company", method = RequestMethod.POST)
	public ModelAndView userCustomerCompany(@Valid Customer customer, BindingResult bindingResult,RedirectAttributes redirectAttributes , HttpSession session) {
		logger.debug("I:");
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		if (bindingResult.hasErrors()) {
			logger.debug("bindingResult error");
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.customer", bindingResult);
			redirectAttributes.addFlashAttribute("customer", customer);
			modelAndView.setViewName("redirect:/user/customer/manage/company");
		} else {
			try {
				logger.debug("save");
				customer.setCustomerType(CustomerType.COMPANY.toString());
				userSession = (User) session.getAttribute("user");
				customerService.save(customer, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.save.success"));
				modelAndView.setViewName("redirect:/user/customer/manage/" + customer.getCustomerIdEncrypt());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error"));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute("customer", customer);
				modelAndView.setViewName("redirect:/user/customer/manage/company");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/customer/manage/company/delete", method=RequestMethod.POST)
	public ModelAndView userCustomerCompanyDelete(Customer customer,HttpSession session, RedirectAttributes redirectAttributes) throws Exception{
		logger.debug("I");
		logger.debug("I" + customer.toString());
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		
		if(customer.getCustomerId() == null) {
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/customer");
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			customerService.delete(customer.getCustomerIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.delete.success"));
			modelAndView.setViewName("redirect:/user/customer");
		} catch (Exception e) {
			logger.error("error:",e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/customer/manage/" + AESencrpUtils.encryptLong(customer.getCustomerId()));
		}
		logger.debug("O");
		return modelAndView;
	}
	
	
	
}