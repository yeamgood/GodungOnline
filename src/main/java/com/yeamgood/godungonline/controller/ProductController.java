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
import com.yeamgood.godungonline.model.Brand;
import com.yeamgood.godungonline.model.Category;
import com.yeamgood.godungonline.model.Country;
import com.yeamgood.godungonline.model.Currency;
import com.yeamgood.godungonline.model.Measure;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.Province;
import com.yeamgood.godungonline.model.Supplier;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.model.Warehouse;
import com.yeamgood.godungonline.service.BrandService;
import com.yeamgood.godungonline.service.CategoryService;
import com.yeamgood.godungonline.service.CountryService;
import com.yeamgood.godungonline.service.CurrencyService;
import com.yeamgood.godungonline.service.MeasureService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProductService;
import com.yeamgood.godungonline.service.ProvinceService;
import com.yeamgood.godungonline.service.SupplierService;
import com.yeamgood.godungonline.service.WarehouseService;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Controller
public class ProductController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String PRODUCT = "product";
	
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
	private SupplierService supplierService;
	
	@RequestMapping(value="/user/product", method = RequestMethod.GET)
	public ModelAndView userProduct(HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(Constants.MENU_PRODUCT_ID);
		List<Product> productList = productService.findAllByGodungGodungIdOrderByProductNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject("productList", productList);
		modelAndView.setViewName("user/product");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/product/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userProductList(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, datatableRequest);
		
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<Product> productList = productService.findAllByGodungGodungIdOrderByProductNameAsc(godungId);
		
		for (Product product : productList) {
			product.getBrand().setBrandId(null);
			product.getMeasure().setMeasureId(null);
			product.getCategory().setCategoryId(null);
		}
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(productList));
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value="/user/product/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userProductDelete(Product product,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, product);
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		try {
			userSession = (User) session.getAttribute("user");
			productService.delete(product.getProductIdEncrypt(), userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/product/manage/{productIdEncrypt}", method = RequestMethod.GET)
	public ModelAndView userProductLoad(Model model,HttpSession session, @PathVariable String productIdEncrypt) throws GodungIdException {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, productIdEncrypt);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(Constants.MENU_PRODUCT_ID);
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		
		Product product = productService.findByIdEncrypt(productIdEncrypt,userSession);
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		List<Brand> brandDropdown = brandService.findAllByGodungGodungIdOrderByBrandNameAsc(godungId);
		List<Measure> measureDropdown = measureService.findAllByGodungGodungIdOrderByMeasureNameAsc(godungId);
		List<Category> categoryDropdown = categoryService.findAllByGodungGodungIdOrderByCategoryNameAsc(godungId);
		List<Currency> currencyDropdown = currencyService.findAllByOrderByCurrencyNameAsc();
		List<Warehouse> warehouseDropdown = warehouseSerivce.findAllByGodungGodungIdOrderByWarehouseNameAsc(godungId);
		List<Supplier> supplierDropdown = supplierService.findAllByGodungGodungIdOrderBySupplierNameAsc(godungId);
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject(PRODUCT,product);
		modelAndView.addObject(Constants.PROVINCE_DROPDOWN,provinceDropdown);
		modelAndView.addObject(Constants.COUNTRY_DROPDOWN,countryDropdown);
		modelAndView.addObject("brandDropdown",brandDropdown);
		modelAndView.addObject("measureDropdown",measureDropdown);
		modelAndView.addObject("categoryDropdown",categoryDropdown);
		modelAndView.addObject("currencyDropdown",currencyDropdown);
		modelAndView.addObject("warehouseDropdown",warehouseDropdown);
		modelAndView.addObject("supplierDropdown",supplierDropdown);
		modelAndView.setViewName("user/product_manage");
		logger.debug("O:");
		return modelAndView;
	}
	
	// --------------------------------------------------------------------
	// --- Manage ---------------------------------------------------------
	// --------------------------------------------------------------------
	@RequestMapping(value="/user/product/manage", method = RequestMethod.GET)
	public ModelAndView userProductPerson(Model model,HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(Constants.MENU_PRODUCT_ID);
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		
		List<Province> provinceDropdown = provinceService.findAllByOrderByProvinceNameAsc();
		List<Country> countryDropdown = countryService.findAllByOrderByCountryNameAsc();
		List<Brand> brandDropdown = brandService.findAllByGodungGodungIdOrderByBrandNameAsc(godungId);
		List<Measure> measureDropdown = measureService.findAllByGodungGodungIdOrderByMeasureNameAsc(godungId);
		List<Category> categoryDropdown = categoryService.findAllByGodungGodungIdOrderByCategoryNameAsc(godungId);
		List<Currency> currencyDropdown = currencyService.findAllByOrderByCurrencyNameAsc();
		List<Warehouse> warehouseDropdown = warehouseSerivce.findAllByGodungGodungIdOrderByWarehouseNameAsc(godungId);
		List<Supplier> supplierDropdown = supplierService.findAllByGodungGodungIdOrderBySupplierNameAsc(godungId);
		
		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute(PRODUCT)) {
			logger.debug("New Object");
			Country country = new Country();
			country.setCountryId(Constants.COUNTRY_THAILAND);
			
			Address address = new Address();
			address.setCountry(country);
			
			Product product = new Product();
			modelAndView.addObject(PRODUCT,product);
	    }
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject(Constants.COUNTRY_DROPDOWN,countryDropdown);
		modelAndView.addObject(Constants.PROVINCE_DROPDOWN,provinceDropdown);
		modelAndView.addObject("brandDropdown",brandDropdown);
		modelAndView.addObject("measureDropdown",measureDropdown);
		modelAndView.addObject("categoryDropdown",categoryDropdown);
		modelAndView.addObject("currencyDropdown",currencyDropdown);
		modelAndView.addObject("warehouseDropdown",warehouseDropdown);
		modelAndView.addObject("supplierDropdown",supplierDropdown);
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
			redirectAttributes.addFlashAttribute(PRODUCT, product);
			modelAndView.setViewName("redirect:/user/product/manage");
		} else {
			try {
				logger.debug("save");
				userSession = (User) session.getAttribute("user");
				productService.save(product, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_SAVE_SUCCESS));
				modelAndView.setViewName("redirect:/user/product/manage/" + product.getProductIdEncrypt());
			} catch (Exception e) {
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_SAVE_ERROR));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute(PRODUCT, product);
				modelAndView.setViewName("redirect:/user/product/manage");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/product/manage/delete", method=RequestMethod.POST)
	public ModelAndView userProductIndividualDelete(Product product,HttpSession session, RedirectAttributes redirectAttributes) {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, product);
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		
		if(product.getProductId() == null) {
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_DELETE_ERROR));
			modelAndView.setViewName("redirect:/user/product");
		}
		try {
			userSession = (User) session.getAttribute("user");
			productService.delete(product.getProductIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_DELETE_SUCCESS));
			modelAndView.setViewName("redirect:/user/product");
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_DELETE_ERROR));
			modelAndView.setViewName("redirect:/user/product/manage/" + AESencrpUtils.encryptLong(product.getProductId()));
		}
		logger.debug("O");
		return modelAndView;
	}
	
	
}