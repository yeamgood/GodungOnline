package com.yeamgood.godungonline.service;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Document;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.DocumentRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Service("documentService")
public class DocumentServiceImpl implements DocumentService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	GodungService godungService;
	
	@Override
	public Document findByIdEncrypt(String idEncrypt,User userSession)  {
		logger.debug("I:");
		logger.debug("O:");
		Document document = documentRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		document.encryptData();
		return document;
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Document document,User userSession) throws ParseException  {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, document);
		document.setCreate(userSession);
		documentRepository.save(document);
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String documentIdEncrypt, User userSession) throws GodungIdException {
		logger.debug("I:");
		Document document = documentRepository.findOne(AESencrpUtils.decryptLong(documentIdEncrypt));
		godungService.checkGodungId(document.getGodung().getGodungId(), userSession);
		documentRepository.delete(document);
		logger.debug("O:");
	}

	@Override
	public List<Document> findAllByGodungGodungIdAndReferenceCode(Long godungId, String referenceCode) {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, godungId);
		logger.debug(Constants.LOG_INPUT, referenceCode);
		List<Document> documentList = documentRepository.findAllByGodungGodungIdAndReferenceCode(godungId, referenceCode);
		for (Document document : documentList) {
			document.encryptData();
		}
		logger.debug("O:");
		return documentList;
	}
	
}