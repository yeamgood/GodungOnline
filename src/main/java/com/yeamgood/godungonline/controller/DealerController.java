package com.yeamgood.godungonline.controller;

import java.text.DecimalFormat;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.datatables.DealerDatatables;
import com.yeamgood.godungonline.form.DealerForm;
import com.yeamgood.godungonline.model.Dealer;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.Product;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.DealerService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProductService;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.DateUtils;

@Controller
public class DealerController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Long MENU_ID = (long) 13;
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	DealerService dealerService;
	
	@Autowired
	ProductService productService;
	
	@RequestMapping(value="/user/dealer", method = RequestMethod.GET)
	public ModelAndView userDealer(HttpSession session) throws Exception{
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(MENU_ID);
		modelAndView.addObject("menu", menu);
		modelAndView.setViewName("user/dealer");
		logger.debug("O");
		return modelAndView;
	}
	

	@RequestMapping(value="/user/dealer/list/ajax/{idEncrypt}", method=RequestMethod.GET)
	public @ResponseBody String userDealerList(DataTablesRequest datatableRequest, HttpSession session,@PathVariable String idEncrypt) throws Exception{
		logger.debug("I");
		logger.debug("datatableRequest" + datatableRequest.toString());
		logger.debug("idEncrypt" + idEncrypt);
		User userSession = (User) session.getAttribute("user");
		
		List<Dealer> dealerList = new ArrayList<Dealer>();
		if(StringUtils.isNotBlank(idEncrypt) && !StringUtils.equalsAnyIgnoreCase(idEncrypt, "null")) {
			Product product = productService.findByIdEncrypt(idEncrypt, userSession);
			dealerList = product.getDealerList();
		}
		
		DealerDatatables dealerDatatables;
		List<DealerDatatables> dealerDatatablesList = new ArrayList<DealerDatatables>();
		
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(2);
		for (Dealer dealer : dealerList) {
			dealerDatatables = new DealerDatatables();
			dealerDatatables.setDealerIdEncrypt(AESencrpUtils.encryptLong(dealer.getDealerId()));
			dealerDatatables.setSupplierName(dealer.getSupplier().getFullname());
			dealerDatatables.setPrice(df.format(dealer.getPrice()));
			dealerDatatables.setStartDate(DateUtils.dateToString(dealer.getStartDate(), DateUtils.ddMMyyyy));
			dealerDatatables.setEndDate(DateUtils.dateToString(dealer.getEndDate(), DateUtils.ddMMyyyy));
			dealerDatatables.setMeasureName(dealer.getMeasure().getMeasureName());
			dealerDatatables.setCurrencyName(dealer.getCurrency().getCurrencyName());
			dealerDatatablesList.add(dealerDatatables);
		}
		
		logger.debug("O:dealerList" + dealerDatatablesList.size());
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(dealerDatatablesList);
		String result = new ObjectMapper().writeValueAsString(dataTableObject);
		return result;
	}
	
	@RequestMapping(value="/user/dealer/load/new", method=RequestMethod.GET)
	public @ResponseBody JsonResponse loadAdd(HttpSession session){
		logger.debug("I");
		Pnotify pnotify;
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Dealer dealer = new Dealer();
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(dealer);
		} catch (Exception e) {
			logger.error("error:",e);
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/dealer/load", method=RequestMethod.GET)
	public @ResponseBody JsonResponse load(DealerForm dealerForm,HttpSession session){
		logger.debug("I");
		logger.debug("I" + dealerForm.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(dealerForm.getDealerIdEncrypt() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			Dealer dealer = dealerService.findByIdEncrypt(dealerForm.getDealerIdEncrypt(), userSession);
			
			DecimalFormat df = new DecimalFormat();
			df.setMinimumFractionDigits(2);
			DealerForm dealerFormTemp = new DealerForm();
			dealerFormTemp.setDealerIdEncrypt(dealer.getDealerIdEncrypt());
			dealerFormTemp.setSupplierIdEncrypt(AESencrpUtils.encryptLong(dealer.getSupplier().getSupplierId()));
			dealerFormTemp.setPrice(df.format(dealer.getPrice()));
			dealerFormTemp.setStartDate(DateUtils.dateToString(dealer.getStartDate(), DateUtils.ddMMyyyy));
			dealerFormTemp.setEndDate(DateUtils.dateToString(dealer.getEndDate(), DateUtils.ddMMyyyy));
			dealerFormTemp.setMeasureIdEncrypt(AESencrpUtils.encryptLong(dealer.getMeasure().getMeasureId()));
			dealerFormTemp.setCurrencyId(dealer.getCurrency().getCurrencyId());
			
			
			pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.load.success");
			jsonResponse.setStatus("SUCCESS");
			jsonResponse.setResult(dealerFormTemp);
		} catch (Exception e) {
			logger.error("error:",e);
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.load.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
		}
		
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/dealer/save", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userDealerAdd(@Valid DealerForm dealerForm,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug("I" + dealerForm.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if (bindingResult.hasErrors()) {
			String errorMsg = "";
			List<FieldError> errors = bindingResult.getFieldErrors();
		    for (FieldError error : errors ) {
		    		errorMsg += error.getField() + " - " + error.getDefaultMessage();
		    }
		    pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
		    pnotify.setText(errorMsg);
		    
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(errors);
		}else {
			try {
				userSession = (User) session.getAttribute("user");
				dealerService.save(dealerForm.getProductIdEncrypt(),dealerForm, userSession);
				
				pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"action.save.success");
				jsonResponse.setStatus("SUCCESS");
				jsonResponse.setResult(pnotify);
				
			} catch (Exception e) {
				logger.error("error:",e);
				pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
				jsonResponse.setStatus("FAIL");
				jsonResponse.setResult(pnotify);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/dealer/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userDealerDelete(DealerForm dealerForm,HttpSession session){
		logger.debug("I");
		logger.debug("I" + dealerForm.toString());
		Pnotify pnotify;
		User userSession;
		JsonResponse jsonResponse = new JsonResponse();
		
		if(dealerForm.getDealerIdEncrypt() == null) {
			pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"action.save.error");
			jsonResponse.setStatus("FAIL");
			jsonResponse.setResult(pnotify);
			return jsonResponse;
		}
		
		try {
			userSession = (User) session.getAttribute("user");
			dealerService.delete(dealerForm.getProductIdEncrypt(),dealerForm.getDealerIdEncrypt(), userSession);
			
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
	
	
	

}