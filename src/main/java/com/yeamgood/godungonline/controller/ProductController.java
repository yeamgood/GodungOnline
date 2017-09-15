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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.model.Address;
import com.yeamgood.godungonline.model.Brand;
import com.yeamgood.godungonline.model.Category;
import com.yeamgood.godungonline.model.Country;
import com.yeamgood.godungonline.model.Currency;
import com.yeamgood.godungonline.model.Measure;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.Province;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.model.Warehouse;
import com.yeamgood.godungonline.service.BrandService;
import com.yeamgood.godungonline.service.CategoryService;
import com.yeamgood.godungonline.service.CountryService;
import com.yeamgood.godungonline.service.CurrencyService;
import com.yeamgood.godungonline.service.LocationService;
import com.yeamgood.godungonline.service.MeasureService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProductService;
import com.yeamgood.godungonline.service.ProvinceService;
import com.yeamgood.godungonline.service.WarehouseService;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Controller
public class ProductController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Long MENU_ID = (long) 8;
	private final Long COUNTRY_THAILAND = (long) 217;
	
	@Autowired
    private MessageSource messageSource;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProvinceService provinceService;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private MeasureService measureService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private WarehouseService warehouseSerivce;
	
	@Autowired
	private LocationService locationService;
	
	@RequestMapping(value="/user/product", method = RequestMethod.GET)
	public ModelAndView userProduct(HttpSession session) throws Exception{
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(MENU_ID);
		List<Product> productList = productService.findAllByGodungGodungIdOrderByProductNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("productList", productList);
		modelAndView.setViewName("user/product");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/product/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userProductListtest(DataTablesRequest datatableRequest, HttpSession session) throws Exception{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<Product> productList = productService.findAllByGodungGodungIdOrderByProductNameAsc(godungId);
		logger.debug("O:productList" + productList.size());
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(productList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		return result;
	}
	
	@RequestMapping(value="/user/product/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userProductDelete(Product product,HttpSession session){
		logger.debug("I");
		logger.debug("I" + product.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(product.getProductIdEncrypt() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		try {
			userSession = (User) session.getAttribute("user");
			productService.delete(product.getProductIdEncrypt(), userSession);
			
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
	
	@RequestMapping(value="/user/product/manage/{idEncrypt}", method = RequestMethod.GET)
	public ModelAndView userProductLoad(Model model,HttpSession session, @PathVariable String idEncrypt) throws NumberFormatException, Exception{
		logger.debug("I:");
		logger.debug("I:idEncrypt" + idEncrypt);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		
		Product product = productService.findByIdEncrypt(idEncrypt,userSession);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		List<Brand> brandDropdown = brandService.findAllByGodungGodungIdOrderByBrandNameAsc(godungId);
		List<Measure> measureDropdown = measureService.findAllByGodungGodungIdOrderByMeasureNameAsc(godungId);
		List<Category> categoryDropdown = categoryService.findAllByGodungGodungIdOrderByCategoryNameAsc(godungId);
		List<Currency> currencyDropdown = currencyService.findAllByOrderByCurrencyNameAsc();
		List<Warehouse> warehouseDropdown = warehouseSerivce.findAllByGodungGodungIdOrderByWarehouseNameAsc(godungId);
		//ArrayList<Locaiton> locationDropdown = locationService.findByIdEncrypt(idEncrypt, userSession);
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("product",product);
		modelAndView.addObject("provinceDropdown",provinceDropdown);
		modelAndView.addObject("countryDropdown",countryDropdown);
		modelAndView.addObject("brandDropdown",brandDropdown);
		modelAndView.addObject("measureDropdown",measureDropdown);
		modelAndView.addObject("categoryDropdown",categoryDropdown);
		modelAndView.addObject("currencyDropdown",currencyDropdown);
		modelAndView.addObject("warehouseDropdown",warehouseDropdown);
		modelAndView.setViewName("user/product_manage");
		logger.debug("O:");
		return modelAndView;
	}
	
	// --------------------------------------------------------------------
	// --- Manage ---------------------------------------------------------
	// --------------------------------------------------------------------
	@RequestMapping(value="/user/product/manage", method = RequestMethod.GET)
	public ModelAndView userProductPerson(Model model,HttpSession session) throws Exception{
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		List<Brand> brandDropdown = brandService.findAllByGodungGodungIdOrderByBrandNameAsc(godungId);
		List<Measure> measureDropdown = measureService.findAllByGodungGodungIdOrderByMeasureNameAsc(godungId);
		List<Category> categoryDropdown = categoryService.findAllByGodungGodungIdOrderByCategoryNameAsc(godungId);
		List<Currency> currencyDropdown = currencyService.findAllByOrderByCurrencyNameAsc();
		List<Warehouse> warehouseDropdown = warehouseSerivce.findAllByGodungGodungIdOrderByWarehouseNameAsc(godungId);
		
		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute("product")) {
			logger.debug("New Object");
			Country country = new Country();
			country.setCountryId(COUNTRY_THAILAND);
			
			Address address = new Address();
			address.setCountry(country);
			
			Product product = new Product();
			modelAndView.addObject("product",product);
	    }
		
		modelAndView.addObject("menu", menu);
		modelAndView.addObject("countryDropdown",countryDropdown);
		modelAndView.addObject("provinceDropdown",provinceDropdown);
		modelAndView.addObject("brandDropdown",brandDropdown);
		modelAndView.addObject("measureDropdown",measureDropdown);
		modelAndView.addObject("categoryDropdown",categoryDropdown);
		modelAndView.addObject("currencyDropdown",currencyDropdown);
		modelAndView.addObject("warehouseDropdown",warehouseDropdown);
		modelAndView.setViewName("user/product_manage");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value = "/user/product/manage", method = RequestMethod.POST)
	public ModelAndView userProductPerson(@Valid Product product, BindingResult bindingResult,RedirectAttributes redirectAttributes , HttpSession session) {
		logger.debug("I:");
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		if (bindingResult.hasErrors()) {
			logger.debug("bindingResult error");
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.product", bindingResult);
			redirectAttributes.addFlashAttribute("product", product);
			modelAndView.setViewName("redirect:/user/product/manage");
		} else {
			try {
				logger.debug("save");
				userSession = (User) session.getAttribute("user");
				productService.save(product, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.save.success"));
				modelAndView.setViewName("redirect:/user/product/manage/" + product.getProductIdEncrypt());
			} catch (Exception e) {
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error"));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute("product", product);
				modelAndView.setViewName("redirect:/user/product/manage");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/product/manage/delete", method=RequestMethod.POST)
	public ModelAndView userProductIndividualDelete(Product product,HttpSession session, RedirectAttributes redirectAttributes) throws Exception{
		logger.debug("I");
		logger.debug("I" + product.toString());
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		
		if(product.getProductId() == null) {
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/product");
		}
		try {
			userSession = (User) session.getAttribute("user");
			productService.delete(product.getProductIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,"action.delete.success"));
			modelAndView.setViewName("redirect:/user/product");
		} catch (Exception e) {
			logger.error("error:",e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,"action.delete.error"));
			modelAndView.setViewName("redirect:/user/product/manage/" + AESencrpUtils.encryptLong(product.getProductId()));
		}
		logger.debug("O");
		return modelAndView;
	}
	
	
}