package com.yeamgood.godungonline.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Godung;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.GodungRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Service("godungService")
public class GodungServiceImpl implements GodungService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GodungRepository godungRepository;
	
	@Override
	public void saveGodung(Godung godung) {
		godung.setCreateUser("SYSTEM");
		godung.setCreateDate(new Date());
		godungRepository.save(godung);
	}

	@Override
	public Godung findGodungByGodungId(long godungId) {
		return godungRepository.findByGodungId(godungId);
	}

	@Override
	public void checkGodungId(Long godungId,User userSession) throws GodungIdException {
		long godungIdSession = userSession.getGodung().getGodungId().longValue();
		if(godungId !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungId + " not equals session user is" + godungIdSession);
		}
	}

	@Override
	public List<Godung> findAllOrderByGodungNameAsc() {
		logger.debug("I");
		List<Godung> godungList = godungRepository.findAll(new Sort(Sort.Direction.ASC, "godungName"));
		for (Godung godung : godungList) {
			godung.encryptData();
		}
		logger.debug("O");
		return godungList;
	}

	@Override
	public Godung findByIdEncrypt(String godungIdEncrypt) {
		logger.debug("I");
		logger.debug("godungIdEncrypt:{}",godungIdEncrypt);
		Godung godung = godungRepository.findOne(AESencrpUtils.decryptLong(godungIdEncrypt));
		godung.encryptData();
		logger.debug("O");
		return godung;
	}

	@Override
	public void save(Godung godung, User user) {
		logger.debug("I");
		logger.debug("godung:{}",godung);
		logger.debug("user:{}",user);
		if(StringUtils.isBlank(godung.getGodungIdEncrypt())) {
			godung.setCreate(user);
			godungRepository.save(godung);
		}else {
			Godung godungTemp = godungRepository.findOne(AESencrpUtils.decryptLong(godung.getGodungIdEncrypt()));
			godungTemp.setGodungName(godung.getGodungName());
			godungTemp.setActive(godung.getActive());
			godungTemp.setUpdate(user);
			godungRepository.save(godungTemp);
		}
		logger.debug("O");
	}

	@Override
	public void delete(Godung godung) {
		logger.debug("I");
		logger.debug("godung:{}",godung);
		Godung godungTemp = godungRepository.findOne(AESencrpUtils.decryptLong(godung.getGodungIdEncrypt()));
		godungRepository.delete(godungTemp);
		logger.debug("O");
	}

}