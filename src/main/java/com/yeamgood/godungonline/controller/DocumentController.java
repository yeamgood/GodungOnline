package com.yeamgood.godungonline.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeamgood.godungonline.bean.JsonResponse;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.datatables.DocumentDatatables;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.form.DocumentForm;
import com.yeamgood.godungonline.model.Document;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.DocumentService;
import com.yeamgood.godungonline.utils.DocumentUtils;

@Controller
public class DocumentController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	DocumentService documentService;
	
	@PostMapping(value="/user/document/upload/{referenceCode}")
	public @ResponseBody JsonResponse load(@Valid DocumentForm documentForm,@PathVariable String referenceCode,HttpSession session,BindingResult bindingResult){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT,referenceCode);
		 Document document = new Document();
		JsonResponse jsonResponse = new JsonResponse();
		
		  if (documentForm.getFile() != null && documentForm.getFile().isEmpty()){
			  logger.debug("file null");
			  bindingResult.addError(new FieldError("file", "file", messageSource.getMessage("file.empty",null,LocaleContextHolder.getLocale())));
		  }
		  
		if (bindingResult.hasErrors()) {
			 jsonResponse.setBinddingResultError(bindingResult);
		}else {
			try {
				User userSession = (User) session.getAttribute("user");
		        MultipartFile multipartFile = documentForm.getFile();
		        document.setName(multipartFile.getOriginalFilename());
		        document.setDescription(documentForm.getDescription());
		        document.setType(multipartFile.getContentType());
		        document.setSize(multipartFile.getSize());
		        document.setContent(multipartFile.getBytes());
		        document.setReferenceCode(referenceCode);
		        document.setGodung(userSession.getGodung());
		        documentService.save(document, userSession);
				 
				jsonResponse.setStatus(Constants.STATUS_SUCCESS);
				jsonResponse.setSaveSuccess(messageSource);
			} catch (Exception e) {
				logger.error(Constants.MESSAGE_ERROR,e);
				jsonResponse.setSaveError(messageSource);
			}
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@RequestMapping(value="/user/document/list/ajax/{referenceCode}", method=RequestMethod.GET)
	public @ResponseBody String userSaleList(@PathVariable String referenceCode,DataTablesRequest datatableRequest, HttpSession session) throws GodungIdException, JsonProcessingException {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, referenceCode);
		User userSession = (User) session.getAttribute("user");

		List<Document> documentList = documentService.findAllByGodungGodungIdAndReferenceCode(userSession.getGodung().getGodungId(), referenceCode);
		
		DocumentDatatables documentDatatables;
		List<DocumentDatatables> documentDatatablesList = new ArrayList<>();
		for (Document document : documentList) {
			documentDatatables = new DocumentDatatables();
			documentDatatables.setDocumentIdEncrypt(document.getDocumentIdEncrypt());
			documentDatatables.setName(document.getName());
			documentDatatables.setDescription(document.getDescription());
			documentDatatables.setType(document.getType());
			documentDatatables.setSize(DocumentUtils.getStringSizeLengthFile(document.getSize()));
			documentDatatablesList.add(documentDatatables);
		}
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(documentDatatablesList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	@PostMapping(value="/user/document/delete/{documentIdEncrypt}")
	public @ResponseBody JsonResponse delete(@PathVariable String documentIdEncrypt,HttpSession session){
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT,documentIdEncrypt);
		JsonResponse jsonResponse = new JsonResponse();
		User userSession = (User) session.getAttribute("user");
		
		try {
			documentService.delete(documentIdEncrypt, userSession);
			jsonResponse.setStatus(Constants.STATUS_SUCCESS);
			jsonResponse.setSaveSuccess(messageSource);
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			jsonResponse.setSaveError(messageSource);
		}
		logger.debug("O");
		return jsonResponse;
	}
	
	@GetMapping(value="/user/document/downloadDocument/{documentIdEncrypt}")
    public void downloadDocument(@PathVariable String documentIdEncrypt, HttpSession session,HttpServletResponse response) {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, documentIdEncrypt);
		
		try {
			User userSession = (User) session.getAttribute("user");
	        Document document = documentService.findByIdEncrypt(documentIdEncrypt, userSession);
	        response.setContentType(document.getType());
	        response.setContentLength(document.getContent().length);
	        response.setHeader("Content-Disposition","attachment; filename=\"" + document.getName() +"\"");
  
			FileCopyUtils.copy(document.getContent(), response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			logger.error(Constants.MESSAGE_ERROR,e);
		}
        logger.debug("O");
    }
	
//	@RequestMapping(value = { "/download-document-{userId}-{docId}" }, method = RequestMethod.GET)
//    public String downloadDocument(@PathVariable int userId, @PathVariable int docId, HttpServletResponse response) throws IOException {
//        UserDocument document = userDocumentService.findById(docId);
//        response.setContentType(document.getType());
//        response.setContentLength(document.getContent().length);
//        response.setHeader("Content-Disposition","attachment; filename=\"" + document.getName() +"\"");
//  
//        FileCopyUtils.copy(document.getContent(), response.getOutputStream());
//  
//        return "redirect:/add-document-"+userId;
//    }
	
}