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
import com.yeamgood.godungonline.model.History;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.HistoryRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Service("historyService")
public class HistoryServiceImpl implements HistoryService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HistoryRepository historyRepository;
	
	@Autowired
	GodungService godungService;
	
	@Override
	public History findByIdEncrypt(String idEncrypt,User userSession)  {
		logger.debug("I:");
		logger.debug("O:");
		History history = historyRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		return history;
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(History history,User userSession) throws ParseException  {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, history);
		history.setCreate(userSession);
		historyRepository.save(history);
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String historyIdEncrypt, User userSession) throws GodungIdException {
		logger.debug("I:");
		History history = historyRepository.findOne(AESencrpUtils.decryptLong(historyIdEncrypt));
		godungService.checkGodungId(history.getGodung().getGodungId(), userSession);
		historyRepository.delete(history);
		logger.debug("O:");
	}

	@Override
	public List<History> findAll(Long godungId, String historyCode) {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, godungId);
		logger.debug(Constants.LOG_INPUT, historyCode);
		List<History> historyList = historyRepository.findAllByGodungGodungIdAndHistoryCodeOrderByCreateDateDesc(godungId, historyCode);
		logger.debug("O:");
		return historyList;
	}
	
}