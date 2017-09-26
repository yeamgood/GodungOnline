package com.yeamgood.godungonline.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Rolegodung;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.RolegodungRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("rolegodungService")
public class RolegodungServiceImpl implements RolegodungService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RolegodungRepository rolegodungRepository;
	
	@Autowired
	private GodungService godungService;
	
	@Override
	public Rolegodung findByIdEncrypt(String idEncrypt,User userSession) throws GodungIdException  {
		logger.debug("I:");
		logger.debug("O:");
		Rolegodung rolegodung = rolegodungRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		rolegodung.encryptData();
		godungService.checkGodungId(rolegodung.getGodung().getGodungId(), userSession);
		return rolegodung;
	}

	@Override
	public List<Rolegodung> findAllByGodungGodungIdOrderByRolegodungNameAsc(Long godungId)  {
		logger.debug(Constants.LOG_INPUT, godungId);
		logger.debug("O:");
		List<Rolegodung> rolegodungList = rolegodungRepository.findAllByGodungGodungIdOrderByRolegodungCodeAsc(godungId);
		for (Rolegodung rolegodung : rolegodungList) {
			rolegodung.setRolegodungIdEncrypt(AESencrpUtils.encryptLong(rolegodung.getRolegodungId()));
			rolegodung.encryptData();
		}
		return rolegodungList;
	}

	@Override
	public long count(Long godungId) {
		logger.debug(Constants.LOG_INPUT, godungId);
		logger.debug("O:");
		return rolegodungRepository.countByGodungGodungId(godungId);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Rolegodung rolegodung,User userSession)  {
		logger.debug("I:");
		logger.debug(Constants.LOG_INPUT, rolegodung);
		if(StringUtils.isBlank(rolegodung.getRolegodungIdEncrypt())) {
			Rolegodung maxRolegodung = rolegodungRepository.findTopByGodungGodungIdOrderByRolegodungCodeDesc(userSession.getGodung().getGodungId());
			if(maxRolegodung == null) {
				logger.debug("I:Null Max Data");
				maxRolegodung = new Rolegodung();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_ROLEGODUNG , maxRolegodung.getRolegodungCode());
			rolegodung.setRolegodungCode(generateCode);
			rolegodung.setGodung(userSession.getGodung());
			rolegodung.setCreate(userSession);
			rolegodungRepository.save(rolegodung);
			rolegodung.setRolegodungIdEncrypt(AESencrpUtils.encryptLong(rolegodung.getRolegodungId()));
		}else {
			Long id = AESencrpUtils.decryptLong(rolegodung.getRolegodungIdEncrypt());
			Rolegodung rolegodungTemp = rolegodungRepository.findOne(id);
			rolegodungTemp.setObject(rolegodung);
			rolegodungTemp.setUpdate(userSession);
			rolegodungRepository.save(rolegodungTemp);
			rolegodung.setRolegodungIdEncrypt(AESencrpUtils.encryptLong(rolegodungTemp.getRolegodungId()));
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(String idEncrypt, User userSession) throws GodungIdException{
		logger.debug("I:");
		Rolegodung rolegodungTemp = rolegodungRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		godungService.checkGodungId(rolegodungTemp.getGodung().getGodungId(), userSession);
		rolegodungRepository.delete(rolegodungTemp.getRolegodungId());
		logger.debug("O:");
	}
	
}