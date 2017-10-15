package com.yeamgood.godungonline.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.form.ApproverForm;
import com.yeamgood.godungonline.form.PurchaseRequestForm;
import com.yeamgood.godungonline.model.Approver;
import com.yeamgood.godungonline.model.ApproverRole;
import com.yeamgood.godungonline.model.Document;
import com.yeamgood.godungonline.model.Employee;
import com.yeamgood.godungonline.model.History;
import com.yeamgood.godungonline.model.PurchaseRequest;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.ApproverRepository;
import com.yeamgood.godungonline.repository.ApproverRoleRepository;
import com.yeamgood.godungonline.repository.EmployeeRepository;
import com.yeamgood.godungonline.repository.PurchaseRequestRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.DateUtils;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("purchaseRequestService")
public class PurchaseRequestServiceImpl implements PurchaseRequestService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PurchaseRequestRepository purchaseRequestRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private GodungService godungService;
	
	@Autowired
	private ApproverRepository approverRepository;
	
	@Autowired
	private ApproverRoleRepository approverRoleRepository;
	
	@Autowired
	private HistoryService historyService;
	
	@Override
	public PurchaseRequest findByIdEncrypt(String idEncrypt,User userSession) throws GodungIdException  {
		logger.debug("I:");
		logger.debug("O:");
		PurchaseRequest purchaseRequest = purchaseRequestRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		purchaseRequest.encryptData();
		godungService.checkGodungId(purchaseRequest.getGodung().getGodungId(), userSession);
		return purchaseRequest;
	}

	@Override
	public List<PurchaseRequest> findAllByGodungGodungIdOrderByPurchaseRequestNameAsc(Long godungId)  {
		logger.debug(Constants.LOG_INPUT, godungId);
		logger.debug("O:");
		List<PurchaseRequest> purchaseRequestList = purchaseRequestRepository.findAllByGodungGodungIdOrderByPurchaseRequestCodeAsc(godungId);
		for (PurchaseRequest purchaseRequest : purchaseRequestList) {
			purchaseRequest.setPurchaseRequestIdEncrypt(AESencrpUtils.encryptLong(purchaseRequest.getPurchaseRequestId()));
			purchaseRequest.encryptData();
		}
		return purchaseRequestList;
	}

	@Override
	public long count(Long godungId) {
		logger.debug(Constants.LOG_INPUT, godungId);
		logger.debug("O:");
		return purchaseRequestRepository.countByGodungGodungId(godungId);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(PurchaseRequestForm purchaseRequestForm,User userSession) throws ParseException  {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, purchaseRequestForm);
		
		PurchaseRequest purchaseRequestTemp = new PurchaseRequest();
		if(StringUtils.isBlank(purchaseRequestForm.getPurchaseRequestIdEncrypt())) {
			PurchaseRequest maxPurchaseRequest = purchaseRequestRepository.findTopByGodungGodungIdOrderByPurchaseRequestCodeDesc(userSession.getGodung().getGodungId());
			if(maxPurchaseRequest == null) {
				logger.debug("I:Null Max Data");
				maxPurchaseRequest = new PurchaseRequest();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_PURCHASE_REQUEST , maxPurchaseRequest.getPurchaseRequestCode());
			purchaseRequestTemp.setPurchaseRequestCode(generateCode);
			purchaseRequestTemp.setGodung(userSession.getGodung());
			purchaseRequestTemp.setCreate(userSession);
		}else {
			Long id = AESencrpUtils.decryptLong(purchaseRequestForm.getPurchaseRequestIdEncrypt());
			purchaseRequestTemp = purchaseRequestRepository.findOne(id);
			purchaseRequestTemp.setUpdate(userSession);
		}
		
		Employee employee = null;
		if(!StringUtils.isBlank(purchaseRequestForm.getEmployeeIdEncrypt())) {
			employee = employeeRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestForm.getEmployeeIdEncrypt()));
		}
		
		purchaseRequestTemp.setEmployee(employee);
		purchaseRequestTemp.setRequestDate(DateUtils.stringToDate(purchaseRequestForm.getRequestDate(), DateUtils.DDMMYYYY));
		purchaseRequestTemp.setDemandDate(DateUtils.stringToDate(purchaseRequestForm.getDemandDate(), DateUtils.DDMMYYYY));
		purchaseRequestTemp.setReferenceNumber(purchaseRequestForm.getReferenceNumber());
		purchaseRequestTemp.setDescription(purchaseRequestForm.getDescription());
		
		purchaseRequestRepository.save(purchaseRequestTemp);
		purchaseRequestForm.setPurchaseRequestIdEncrypt(AESencrpUtils.encryptLong(purchaseRequestTemp.getPurchaseRequestId()));
		
		History history = new History();
		history.setHistoryType("PR");
		history.setHistoryCode(purchaseRequestTemp.getPurchaseRequestCode());
		history.setEmployeeName(userSession.getName());
		history.setAction("SAVE");
		history.setDescription("Save Purchase Request");
		history.setGodung(userSession.getGodung());
		history.setCreate(userSession);
		historyService.save(history, userSession);
		
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String idEncrypt, User userSession) throws GodungIdException{
		logger.debug("I:");
		PurchaseRequest purchaseRequestTemp = purchaseRequestRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		godungService.checkGodungId(purchaseRequestTemp.getGodung().getGodungId(), userSession);
		purchaseRequestRepository.delete(purchaseRequestTemp.getPurchaseRequestId());
		logger.debug("O:");
	}
	
	@Override
	@Transactional(rollbackFor={Exception.class})
	public void approverSave(String purchaseRequestIdEncrypt,ApproverForm approverForm,User userSession) throws GodungIdException  {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, purchaseRequestIdEncrypt);
		
		PurchaseRequest purchaseRequest = purchaseRequestRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestIdEncrypt));
		godungService.checkGodungId(purchaseRequest.getGodung().getGodungId(), userSession);
		
		if(StringUtils.isBlank(approverForm.getApproverIdEncrypt())) {
			Employee employee = employeeRepository.findOne(AESencrpUtils.decryptLong(approverForm.getEmployeeIdEncrypt()));
			ApproverRole approverRole = approverRoleRepository.findOneByApproverRoleCode(approverForm.getApproverRoleCode());
			
			Long maxSequence = Long.valueOf("0");
			for (Approver approver : purchaseRequest.getApproverList()) {
				if(maxSequence < approver.getSequence()) {
					maxSequence = approver.getSequence();
				}
			}
			
			Approver approver = new Approver();
			approver.setEmployee(employee);
			approver.setApproverRole(approverRole);
			approver.setRequestDate(new Date());
			approver.setCreate(userSession);
			approver.setSequence(maxSequence+1);
			
			purchaseRequest.getApproverList().add(approver);
			purchaseRequestRepository.save(purchaseRequest);
		}else {
			Approver approver = approverRepository.findOne(AESencrpUtils.decryptLong(approverForm.getApproverIdEncrypt()));
			Employee employee = employeeRepository.findOne(AESencrpUtils.decryptLong(approverForm.getEmployeeIdEncrypt()));
			ApproverRole approverRole = approverRoleRepository.findOneByApproverRoleCode(approverForm.getApproverRoleCode());
			
			approver.setEmployee(employee);
			approver.setApproverRole(approverRole);
			approver.setRequestDate(new Date());
			approver.setUpdate(userSession);
			approverRepository.save(approver);
		}
		
		logger.debug("O:");
	}

	@Override
	public void approverDelete(String purchaseRequestIdEncrypt, String approverIdEncrypt, User userSession) throws GodungIdException {
		logger.debug("I:");
		PurchaseRequest purchaseRequest = purchaseRequestRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestIdEncrypt));
		godungService.checkGodungId(purchaseRequest.getGodung().getGodungId(), userSession);
		Long approverId = AESencrpUtils.decryptLong(approverIdEncrypt);
		for (Approver approver : purchaseRequest.getApproverList()) {
			if(Long.valueOf(approverId) == Long.valueOf(approver.getApproverId())) {
				purchaseRequest.getApproverList().remove(approver);
				break;
			}
		}
		purchaseRequestRepository.save(purchaseRequest);
		logger.debug("O:");
	}

	@Override
	public void documentUpload(String purchaseRequestIdEncrypt, Document document, User userSession) throws GodungIdException {
		logger.debug("I:");
		PurchaseRequest purchaseRequest = purchaseRequestRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestIdEncrypt));
		godungService.checkGodungId(purchaseRequest.getGodung().getGodungId(), userSession);
		purchaseRequest.getDocumentList().add(document);
		purchaseRequestRepository.save(purchaseRequest);
		logger.debug("O:");
	}

	@Override
	public void documentDelete(String purchaseRequestIdEncrypt, String documentIdEncrypt,User userSession) throws GodungIdException {
		logger.debug("I:");
		PurchaseRequest purchaseRequest = purchaseRequestRepository.findOne(AESencrpUtils.decryptLong(purchaseRequestIdEncrypt));
		godungService.checkGodungId(purchaseRequest.getGodung().getGodungId(), userSession);
		Long documentId = AESencrpUtils.decryptLong(documentIdEncrypt);
		for (Document document : purchaseRequest.getDocumentList()) {
			if(Long.valueOf(documentId) == Long.valueOf(document.getDocumentId())) {
				purchaseRequest.getDocumentList().remove(document);
				break;
			}
		}
		purchaseRequestRepository.save(purchaseRequest);
		logger.debug("O:");
	}
	
}