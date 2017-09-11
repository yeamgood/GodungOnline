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
import com.yeamgood.godungonline.model.Employee;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.Province;
import com.yeamgood.godungonline.model.Rolegodung;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.CountryService;
import com.yeamgood.godungonline.service.EmployeeService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProvinceService;
import com.yeamgood.godungonline.service.RolegodungService;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Controller
public class EmployeeController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Long MENU_ID = (long) 33;
	private final Long COUNTRY_THAILAND = (long) 217;
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	ProvinceService provinceService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	RolegodungService rolegodungService;
	
	@RequestMapping(value="/user/employee", method = RequestMethod.GET)
	public ModelAndView userEmployee(HttpSession session) throws Exception{
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(MENU_ID);
		List<Employee> employeeList = employeeService.findAllByGodungGodungIdOrderByEmployeeNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("employeeList", employeeList);
		modelAndView.setViewName("user/employee");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/employee/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userEmployeeListtest(DataTablesRequest datatableRequest, HttpSession session) throws Exception{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<Employee> employeeList = employeeService.findAllByGodungGodungIdOrderByEmployeeNameAsc(godungId);
		logger.debug("O:employeeList" + employeeList.size());
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(employeeList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		return result;
	}
	
	@RequestMapping(value="/user/employee/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userEmployeeDelete(Employee employee,HttpSession session){
		logger.debug("I");
		logger.debug("I" + employee.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(employee.getEmployeeIdEncrypt() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		try {
			userSession = (User) session.getAttribute("user");
			employeeService.delete(employee.getEmployeeIdEncrypt(), userSession);
			
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
	
	@RequestMapping(value="/user/employee/manage/{idEncrypt}", method = RequestMethod.GET)
	public ModelAndView userEmployeeLoad(Model model,HttpSession session, @PathVariable String idEncrypt) throws NumberFormatException, Exception{
		logger.debug("I:");
		logger.debug("I:idEncrypt" + idEncrypt);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		User userSession = (User) session.getAttribute("user");
		
		Employee employee = employeeService.findByIdEncrypt(idEncrypt,userSession);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		List<Rolegodung> rolegodungDropdown = rolegodungService.findAllByGodungGodungIdOrderByRolegodungNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("employee",employee);
		modelAndView.addObject("provinceDropdown",provinceDropdown);
		modelAndView.addObject("countryDropdown",countryDropdown);
		modelAndView.addObject("rolegodungDropdown",rolegodungDropdown);
		modelAndView.setViewName("user/employee_manage");
		logger.debug("O:");
		return modelAndView;
	}
	
	// --------------------------------------------------------------------
	// --- PERSON ---------------------------------------------------------
	// --------------------------------------------------------------------
	@RequestMapping(value="/user/employee/manage", method = RequestMethod.GET)
	public ModelAndView userEmployeePerson(Model model,HttpSession session) throws Exception{
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		User userSession = (User) session.getAttribute("user");
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		List<Rolegodung> rolegodungDropdown = rolegodungService.findAllByGodungGodungIdOrderByRolegodungNameAsc(userSession.getGodung().getGodungId());
		
		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute("employee")) {
			logger.debug("New Object");
			Country country = new Country();
			country.setCountryId(COUNTRY_THAILAND);
			
			Address address = new Address();
			address.setCountry(country);
			
			Employee employee = new Employee();
			employee.setAddress(address);
			modelAndView.addObject("employee",employee);
	    }
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("countryDropdown",countryDropdown);
		modelAndView.addObject("provinceDropdown",provinceDropdown);
		modelAndView.addObject("rolegodungDropdown",rolegodungDropdown);
		modelAndView.setViewName("user/employee_manage");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value = "/user/employee/manage", method = RequestMethod.POST)
	public ModelAndView userEmployeePerson(@Valid Employee employee, BindingResult bindingResult,RedirectAttributes redirectAttributes , HttpSession session) {
		logger.debug("I:");
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		if (bindingResult.hasErrors()) {
			logger.debug("bindingResult error");
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.employee", bindingResult);
			redirectAttributes.addFlashAttribute("employee", employee);
			modelAndView.setViewName("redirect:/user/employee/manage");
		} else {
			try {
				logger.debug("save");
				userSession = (User) session.getAttribute("user");
				employeeService.save(employee, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.save.success"));
				modelAndView.setViewName("redirect:/user/employee/manage/" + employee.getEmployeeIdEncrypt());
			} catch (Exception e) {
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error"));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute("employee", employee);
				modelAndView.setViewName("redirect:/user/employee/manage");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/employee/manage/delete", method=RequestMethod.POST)
	public ModelAndView userEmployeeIndividualDelete(Employee employee,HttpSession session, RedirectAttributes redirectAttributes) throws Exception{
		logger.debug("I");
		logger.debug("I" + employee.toString());
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		
		if(employee.getEmployeeId() == null) {
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/employee");
		}
		try {
			userSession = (User) session.getAttribute("user");
			employeeService.delete(employee.getEmployeeIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.delete.success"));
			modelAndView.setViewName("redirect:/user/employee");
		} catch (Exception e) {
			logger.error("error:",e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/employee/manage/" + AESencrpUtils.encryptLong(employee.getEmployeeId()));
		}
		logger.debug("O");
		return modelAndView;
	}
	
	
	
}