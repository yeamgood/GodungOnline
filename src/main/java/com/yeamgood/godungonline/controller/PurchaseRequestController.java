package com.yeamgood.godungonline.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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
import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.form.ApproverForm;
import com.yeamgood.godungonline.form.PurchaseRequestForm;
import com.yeamgood.godungonline.model.Approver;
import com.yeamgood.godungonline.model.ApproverRole;
import com.yeamgood.godungonline.model.Measure;
import com.yeamgood.godungonline.model.Menu;
import com.yeamgood.godungonline.model.PurchaseRequest;
import com.yeamgood.godungonline.model.PurchaseRequestProduct;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.ApproverRoleService;
import com.yeamgood.godungonline.service.CommonService;
import com.yeamgood.godungonline.service.CountryService;
import com.yeamgood.godungonline.service.MeasureService;
import com.yeamgood.godungonline.service.MenuService;
import com.yeamgood.godungonline.service.ProvinceService;
import com.yeamgood.godungonline.service.PurchaseRequestService;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.DateUtils;
import com.yeamgood.godungonline.utils.NumberUtils;

@Controller
public class PurchaseRequestController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String PURCHASE_REQUEST_FORM = "purchaseRequestForm";
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	PurchaseRequestService purchaseRequestService;
	
	@Autowired
	ProvinceService provinceService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	MeasureService measureService;
	
	@Autowired
	CommonService commonService;  
	
	@Autowired
	ApproverRoleService approverRoleService;
	
	@RequestMapping(value="/user/purchaseRequest", method = RequestMethod.GET)
	public ModelAndView userPurchaseRequest(HttpSession session) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		User userSession = (User) session.getAttribute("user");
		Menu menu = menuService.findById(Constants.MENU_PURCHASE_REQUEST);
		List<PurchaseRequest> purchaseRequestList = purchaseRequestService.findAllByGodungGodungIdOrderByPurchaseRequestNameAsc(userSession.getGodung().getGodungId());
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject("purchaseRequestList", purchaseRequestList);
		modelAndView.setViewName("user/purchaseRequest");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/purchaseRequest/list/ajax", method=RequestMethod.GET)
	public @ResponseBody String userPurchaseRequestList(DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT,datatableRequest);
		
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		List<PurchaseRequest> purchaseRequestList = purchaseRequestService.findAllByGodungGodungIdOrderByPurchaseRequestNameAsc(godungId);
		
		BigDecimal totalPrice;
		PurchaseRequestForm purchaseRequestForm;
		List<PurchaseRequestForm> purchaseRequestFormList = new ArrayList<>();
		for(PurchaseRequest purchaseRequest:purchaseRequestList) {
			purchaseRequestForm = new PurchaseRequestForm();
			purchaseRequestForm.setPurchaseRequestIdEncrypt(purchaseRequest.getPurchaseRequestIdEncrypt());
			purchaseRequestForm.setPurchaseRequestCode(purchaseRequest.getPurchaseRequestCode());
			purchaseRequestForm.setEmployeeName(purchaseRequest.getEmployee().getFullName());
			purchaseRequestForm.setRequestDate(DateUtils.dateToString(purchaseRequest.getRequestDate(), DateUtils.DDMMYYYY));
			purchaseRequestForm.setDemandDate(DateUtils.dateToString(purchaseRequest.getDemandDate(), DateUtils.DDMMYYYY));
			purchaseRequestForm.setDescription(purchaseRequest.getDescription());
			
			totalPrice = new BigDecimal(NumberUtils.EMPLY_DATA);
			logger.debug("STEP 1");
			if(purchaseRequest.getPurchaseRequestProductList() != null) {
				logger.debug("STEP 2");
				for (PurchaseRequestProduct prProduct : purchaseRequest.getPurchaseRequestProductList()) {
					logger.debug("STEP 3");
					logger.debug("STEP 3 prProduct.getAmount() {} ",prProduct.getAmount());
					logger.debug("STEP 3 prProduct.getPrice() {}", prProduct.getPrice());
					totalPrice = totalPrice.add(prProduct.getAmount().multiply(prProduct.getPrice()));
					logger.debug("STEP 3 totalPrice {}",totalPrice);
				}
			}
			logger.debug("STEP 4 totalPrice {} ",totalPrice);
			purchaseRequestForm.setTotalPrice(NumberUtils.bigDecimalToString(totalPrice));
			
			purchaseRequestFormList.add(purchaseRequestForm);
		}
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(purchaseRequestFormList));
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value="/user/purchaseRequest/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse userPurchaseRequestDelete(PurchaseRequest purchaseRequest,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, purchaseRequest);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			User userSession = (User) session.getAttribute("user");
			purchaseRequestService.delete(purchaseRequest.getPurchaseRequestIdEncrypt(), userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/purchaseRequest/manage/{purchaseRequestIdEncrypt}", method = RequestMethod.GET)
	public ModelAndView userPurchaseRequestLoad(Model model,HttpSession session, @PathVariable String purchaseRequestIdEncrypt) throws GodungIdException {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, purchaseRequestIdEncrypt);
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(Constants.MENU_PURCHASE_REQUEST);
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		
		PurchaseRequest purchaseRequest = purchaseRequestService.findByIdEncrypt(purchaseRequestIdEncrypt,userSession);
		PurchaseRequestForm purchaseRequestForm = new PurchaseRequestForm();
		purchaseRequestForm.setPurchaseRequestIdEncrypt(purchaseRequest.getPurchaseRequestIdEncrypt());
		purchaseRequestForm.setPurchaseRequestCode(purchaseRequest.getPurchaseRequestCode());
		purchaseRequestForm.setEmployeeIdEncrypt(AESencrpUtils.encryptLong(purchaseRequest.getEmployee().getEmployeeId()));
		purchaseRequestForm.setEmployeeName(purchaseRequest.getEmployee().getFullName());
		purchaseRequestForm.setRequestDate(DateUtils.dateToString(purchaseRequest.getRequestDate(), DateUtils.DDMMYYYY));
		purchaseRequestForm.setDemandDate(DateUtils.dateToString(purchaseRequest.getDemandDate(), DateUtils.DDMMYYYY));
		purchaseRequestForm.setReferenceNumber(purchaseRequest.getReferenceNumber());
		purchaseRequestForm.setDescription(purchaseRequest.getDescription());
		
		List<Measure> measureDropdown = measureService.findAllByGodungGodungIdOrderByMeasureNameAsc(godungId);
		List<ApproverRole> approveRoleDropdown = approverRoleService.findAll();
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject("measureDropdown",measureDropdown);
		modelAndView.addObject(PURCHASE_REQUEST_FORM,purchaseRequestForm);
		modelAndView.addObject("approveRoleDropdown",approveRoleDropdown);
		modelAndView.setViewName("user/purchaseRequest_manage");
		logger.debug("O:");
		return modelAndView;
	}
	
	// --------------------------------------------------------------------
	// --- Manage ---------------------------------------------------------
	// --------------------------------------------------------------------
	@RequestMapping(value="/user/purchaseRequest/manage", method = RequestMethod.GET)
	public ModelAndView purchaseRequestManage(Model model,HttpSession session){
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		Menu menu = menuService.findById(Constants.MENU_PURCHASE_REQUEST);
		User userSession = (User) session.getAttribute("user");
		Long godungId = userSession.getGodung().getGodungId();
		
		//CHECK ERROR BINDING AND INITIAL DATA
		if (!model.containsAttribute(PURCHASE_REQUEST_FORM)) {
			logger.debug("New Object");
			PurchaseRequestForm purchaseRequestForm = new PurchaseRequestForm();
			modelAndView.addObject(PURCHASE_REQUEST_FORM,purchaseRequestForm);
	    }
		
		List<Measure> measureDropdown = measureService.findAllByGodungGodungIdOrderByMeasureNameAsc(godungId);
		List<ApproverRole> approveRoleDropdown = approverRoleService.findAll();
		
		modelAndView.addObject(Constants.MENU, menu);
		modelAndView.addObject("measureDropdown",measureDropdown);
		modelAndView.addObject("approveRoleDropdown",approveRoleDropdown);
		modelAndView.setViewName("user/purchaseRequest_manage");
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value = "/user/purchaseRequest/manage", method = RequestMethod.POST)
	public ModelAndView purchaseRequestManageSave(@Valid PurchaseRequestForm purchaseRequestForm, BindingResult bindingResult,RedirectAttributes redirectAttributes , HttpSession session) {
		logger.debug("I:");
		ModelAndView modelAndView = new ModelAndView();
		User userSession;
		if (bindingResult.hasErrors()) {
			logger.debug("bindingResult error");
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.purchaseRequestForm", bindingResult);
			redirectAttributes.addFlashAttribute(PURCHASE_REQUEST_FORM, purchaseRequestForm);
			modelAndView.setViewName("redirect:/user/purchaseRequest/manage");
		} else {
			try {
				logger.debug("save");
				userSession = (User) session.getAttribute("user");
				purchaseRequestService.save(purchaseRequestForm, userSession);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_SAVE_SUCCESS));
				modelAndView.setViewName("redirect:/user/purchaseRequest/manage/" + purchaseRequestForm.getPurchaseRequestIdEncrypt());
			} catch (Exception e) {
				logger.error("error",e);
				redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_SAVE_ERROR));
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.profileForm", bindingResult);
				redirectAttributes.addFlashAttribute(PURCHASE_REQUEST_FORM, purchaseRequestForm);
				modelAndView.setViewName("redirect:/user/purchaseRequest/manage");
			}
		}
		logger.debug("O:");
		return modelAndView;
	}
	
	@RequestMapping(value="/user/purchaseRequest/manage/delete", method=RequestMethod.POST)
	public ModelAndView purchaseRequestManageDelete(PurchaseRequest purchaseRequest,HttpSession session, RedirectAttributes redirectAttributes) {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, purchaseRequest);
		ModelAndView modelAndView = new ModelAndView();
		try {
			User userSession = (User) session.getAttribute("user");
			purchaseRequestService.delete(purchaseRequest.getPurchaseRequestIdEncrypt(), userSession);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.SUCCESS,Constants.ACTION_DELETE_SUCCESS));
			modelAndView.setViewName("redirect:/user/purchaseRequest");
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			redirectAttributes.addFlashAttribute(new Pnotify(messageSource,PnotifyType.ERROR,Constants.ACTION_DELETE_ERROR));
			modelAndView.setViewName("redirect:/user/purchaseRequest/manage/" + AESencrpUtils.encryptLong(purchaseRequest.getPurchaseRequestId()));
		}
		logger.debug("O");
		return modelAndView;
	}
	
	
	// APPROVER
	@RequestMapping(value="/user/apaprover/save", method=RequestMethod.POST)
	public @ResponseBody JsonResponse approverSave(@Valid ApproverForm approverForm,BindingResult bindingResult,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, approverForm);
		JsonResponse jsonResponse = new JsonResponse();
		if (bindingResult.hasErrors()) {
			jsonResponse.setBinddingResultError(bindingResult);
		}else {
			try {
				User userSession = (User) session.getAttribute("user");
				purchaseRequestService.approverSave(approverForm.getPurchaseRequestIdEncrypt(), approverForm, userSession);
				jsonResponse.setSaveSuccess(messageSource);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR,e);
				jsonResponse.setSaveError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	
	@RequestMapping(value="/user/purchaseRequest/approver/list/ajax/{purchaseRequestIdEncrypt}", method=RequestMethod.GET)
	public @ResponseBody String approverList(@PathVariable String purchaseRequestIdEncrypt,DataTablesRequest datatableRequest, HttpSession session) throws JsonProcessingException, GodungIdException {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT,purchaseRequestIdEncrypt);
		logger.debug(Constants.LOG_INPUT,datatableRequest);
		
		User userSession = (User) session.getAttribute("user");
		
		List<Approver> approverList = new ArrayList<>();
		if(StringUtils.isNotBlank(purchaseRequestIdEncrypt) && !StringUtils.equalsAnyIgnoreCase(purchaseRequestIdEncrypt, "null")) {
			PurchaseRequest purchaseRequest = purchaseRequestService.findByIdEncrypt(purchaseRequestIdEncrypt, userSession);
			approverList = purchaseRequest.getApproverList();
		}
		
		Collections.sort(approverList, (o1, o2) -> o1.getSequence().compareTo(o2.getSequence()));
		
		ApproverForm approverForm;
		List<ApproverForm> approverFormList = new ArrayList<>();
		for (Approver approver : approverList) {
			approver.encryptData();
			approverForm = new ApproverForm();
			approverForm.setApproverIdEncrypt(approver.getApproverIdEncrypt());
			approverForm.setEmployeeName(approver.getEmployee().getFullName());
			approverForm.setApproverRoleName(messageSource.getMessage(approver.getApproverRole().getMessage(),null,LocaleContextHolder.getLocale()));
			approverForm.setRequestDate(DateUtils.dateToString(approver.getRequestDate(), DateUtils.DDMMYYYY_HHMMSS));
			approverForm.setApproverDate(DateUtils.dateToString(approver.getApproverDate(), DateUtils.DDMMYYYY_HHMMSS));
			approverFormList.add(approverForm);
		}
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(approverFormList));
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@RequestMapping(value="/user/purchaseRequest/approver/delete", method=RequestMethod.POST)
	public @ResponseBody JsonResponse approverDelete(ApproverForm approverForm,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, approverForm);
		JsonResponse jsonResponse = new JsonResponse();
		try {
			User userSession = (User) session.getAttribute("user");
			purchaseRequestService.approverDelete(approverForm.getPurchaseRequestIdEncrypt(), approverForm.getApproverIdEncrypt(), userSession);
			jsonResponse.setDeleteSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setDeleteError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
}