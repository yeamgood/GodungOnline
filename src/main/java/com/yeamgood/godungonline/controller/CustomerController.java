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
import com.yeamgood.godungonline.bean.CustomerType;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.exception.GodungIdException;
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
	
	private static final String LOG_CUSTOMER = "customer:{}";
	private static final String LOG_IDENCRYPT = "idEncrypt:{}";
	
	private static final String CUSTOMER = "customer";
	private static final String REDIRECT_CUSTOMER = "redirect:/user/customer";
	private static final String REDIRECT_CUSTOMER_MANAGE = "redirect:/user/customer/manage/";
	
	@RequestMapping(value="/user/customer", method = RequestMethod.GET)
	public ModelAndView userCustomer(HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(Constants.MENU_CUSTOMER_ID);
		List<Customer> customerList = customerService.findAllByGodungGodungIdOrderByCustomerNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject("customerList", customerList);
		modelAndView.setViewName("user/customer");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/customer/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userCustomerListtest(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<Customer> customerList = customerService.findAllByGodungGodungIdOrderByCustomerNameAsc(godungId);
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(customerList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value="/user/customer/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userCustomerDelete(Customer customer,HttpSession session){
		logger.debug("I");
		logger.debug(LOG_CUSTOMER, customer);
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			userSession = (User) session.getAttribute("user");
			customerService.delete(customer.getCustomerIdEncrypt(), userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setDeleteError(messageSource);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/customer/manage/{idEncrypt}", method = RequestMethod.GET)
	public ModelAndView userCustomerLoad(Model model,HttpSession session, @PathVariable String idEncrypt) throws GodungIdException {
		logger.debug("I:");
		logger.debug(LOG_IDENCRYPT,idEncrypt);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(Constants.MENU_CUSTOMER_ID);
		User userSession = (User) session.getAttribute("user");
		
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		Customer customer = customerService.findByIdEncrypt(idEncrypt,userSession);
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject(CUSTOMER,customer);
		modelAndView.addObject(Constants.PROVINCE_DROPDOWN,provinceDropdown);
		modelAndView.addObject(Constants.COUNTRY_DROPDOWN,countryDropdown);
		
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
		Menu menu = menuService.findById(Constants.MENU_CUSTOMER_ID);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		
		if (!model.containsAttribute(CUSTOMER)) {
			logger.debug("New Object");
			Country country = new Country();
			country.setCountryId(Constants.COUNTRY_THAILAND);
			
			Address address = new Address();
			address.setCountry(country);
			
			Customer customer = new Customer();
			customer.setAddress(address);
			customer.setAddressSend(address);
			modelAndView.addObject(CUSTOMER,customer);
	    }
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject(Constants.PROVINCE_DROPDOWN,provinceDropdown);
		modelAndView.setViewName("user/customer_person");
		modelAndView.addObject(Constants.COUNTRY_DROPDOWN,countryDropdown);
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
			redirectAttributes.addFlashAttribute(CUSTOMER, customer);
			modelAndView.setViewName("redirect:/user/customer/manage/person");
		} else {
			try {
				logger.debug("save");
				customer.setCustomerType(CustomerType.PERSON.toString());
				userSession = (User) session.getAttribute("user");
				customerService.save(customer, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_SAVE_SUCCESS));
				modelAndView.setViewName(REDIRECT_CUSTOMER_MANAGE + customer.getCustomerIdEncrypt());
			} catch (Exception e) {
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_SAVE_ERROR));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute(CUSTOMER, customer);
				modelAndView.setViewName("redirect:/user/customer/manage/person");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/customer/manage/person/delete", method=RequestMethod.POST)
	public ModelAndView userCustomerIndividualDelete(Customer customer,HttpSession session, RedirectAttributes redirectAttributes) {
		logger.debug("I");
		logger.debug(LOG_CUSTOMER, customer);
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		
		if(customer.getCustomerId() == null) {
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_DELETE_ERROR));
			modelAndView.setViewName(REDIRECT_CUSTOMER);
		}
		try {
			userSession = (User) session.getAttribute("user");
			customerService.delete(customer.getCustomerIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_DELETE_SUCCESS));
			modelAndView.setViewName(REDIRECT_CUSTOMER);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_DELETE_ERROR));
			modelAndView.setViewName(REDIRECT_CUSTOMER_MANAGE + AESencrpUtils.encryptLong(customer.getCustomerId()));
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
		Menu menu = menuService.findById(Constants.MENU_CUSTOMER_ID);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();

		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute(CUSTOMER)) {
			logger.debug("New Object");
			Country country = new Country();
			country.setCountryId(Constants.COUNTRY_THAILAND);
			
			Address address = new Address();
			address.setCountry(country);
			
			Customer customer = new Customer();
			customer.setAddress(address);
			customer.setAddressSend(address);
			modelAndView.addObject(CUSTOMER,customer);
	    }
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject(Constants.PROVINCE_DROPDOWN,provinceDropdown);
		modelAndView.setViewName("user/customer_company");
		modelAndView.addObject(Constants.COUNTRY_DROPDOWN,countryDropdown);
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
			redirectAttributes.addFlashAttribute(CUSTOMER, customer);
			modelAndView.setViewName("redirect:/user/customer/manage/company");
		} else {
			try {
				logger.debug("save");
				customer.setCustomerType(CustomerType.COMPANY.toString());
				userSession = (User) session.getAttribute("user");
				customerService.save(customer, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_SAVE_SUCCESS));
				modelAndView.setViewName(REDIRECT_CUSTOMER_MANAGE + customer.getCustomerIdEncrypt());
			} catch (Exception e) {
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_SAVE_ERROR));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute(CUSTOMER, customer);
				modelAndView.setViewName("redirect:/user/customer/manage/company");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/customer/manage/company/delete", method=RequestMethod.POST)
	public ModelAndView userCustomerCompanyDelete(Customer customer,HttpSession session, RedirectAttributes redirectAttributes) {
		logger.debug("I");
		logger.debug(LOG_CUSTOMER, customer);
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		
		if(customer.getCustomerId() == null) {
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_DELETE_ERROR));
			modelAndView.setViewName(REDIRECT_CUSTOMER);
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			customerService.delete(customer.getCustomerIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_DELETE_SUCCESS));
			modelAndView.setViewName(REDIRECT_CUSTOMER);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_DELETE_ERROR));
			modelAndView.setViewName(REDIRECT_CUSTOMER_MANAGE + AESencrpUtils.encryptLong(customer.getCustomerId()));
		}
		logger.debug("O");
		return modelAndView;
	}
	
	
	
}