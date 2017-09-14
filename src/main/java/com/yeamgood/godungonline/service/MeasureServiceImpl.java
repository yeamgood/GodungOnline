package com.yeamgood.godungonline.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Measure;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.MeasureRepository;
import com.yeamgood.godungonline.utils.AESencrpUtils;
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("measureService")
public class MeasureServiceImpl implements MeasureService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MeasureRepository measureRepository;

	@Override
	public Measure findByIdEncrypt(String idEncrypt) throws Exception {
		logger.debug("I:");
		Measure measure = measureRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		measure.encryptData(measure);
		logger.debug("O:");
		return measure;
	}
	
	@Override
	public Measure findByIdEncrypt(String idEncrypt, User userSession) throws Exception {
		logger.debug("I:");
		Measure measure = measureRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		checkGodungId(measure, userSession);
		measure.encryptData(measure);
		logger.debug("O:");
		return measure;
	}

	@Override
	public List<Measure> findAllOrderByMeasureNameAsc() throws Exception {
		logger.debug("I:");
		List<Measure> measureList = measureRepository.findAll(sortByMeasureNameAsc());
		for (Measure measure : measureList) {
			measure.setMeasureIdEncrypt(AESencrpUtils.encryptLong(measure.getMeasureId()));
			measure.encryptData(measure);
		}
		logger.debug("O:");
		return measureList;
	}

	@Override
	public List<Measure> findAllByGodungGodungIdOrderByMeasureNameAsc(Long godungId) throws Exception {
		logger.debug("I:[godungId]:" + godungId);
		List<Measure> measureList = measureRepository.findAllByGodungGodungIdOrderByMeasureNameAsc(godungId);
		for (Measure measure : measureList) {
			measure.setMeasureIdEncrypt(AESencrpUtils.encryptLong(measure.getMeasureId()));
			measure.encryptData(measure);
		}
		logger.debug("O:");
		return measureList;
	}
	
	private Sort sortByMeasureNameAsc() {
        return new Sort(Sort.Direction.ASC, "measureName");
    }

	@Override
	public long count(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return measureRepository.countByGodungGodungId(godungId);
	}

	@Override
	public List<Measure> findByGodungGodungIdAndMeasureNameIgnoreCaseContaining(Long godungId, String measureName, Pageable pageable) throws Exception {
		logger.debug("I:");
		List<Measure> measureList = measureRepository.findByGodungGodungIdAndMeasureNameIgnoreCaseContaining(godungId, measureName, pageable);
		for (Measure measure : measureList) {
			measure.setMeasureIdEncrypt(AESencrpUtils.encryptLong(measure.getMeasureId()));
			measure.encryptData(measure);
		}
		logger.debug("O:");
		return measureList;
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Measure measure,User user) throws Exception {
		logger.debug("I:");
		if(StringUtils.isBlank(measure.getMeasureIdEncrypt())) {
			Measure maxMeasure = measureRepository.findTopByGodungGodungIdOrderByMeasureCodeDesc(user.getGodung().getGodungId());
			if(maxMeasure == null) {
				logger.debug("I:Null Max Data");
				maxMeasure = new Measure();
			}
			String generateCode = GenerateCodeUtils.generateCode(GenerateCodeUtils.TYPE_MEASURE, maxMeasure.getMeasureCode());
			measure.setGodung(user.getGodung());
			measure.setCreate(user.getEmail(), new Date());
			measure.setMeasureCode(generateCode);
			measure.setGodung(user.getGodung());
			measureRepository.save(measure);
		}else {
			Measure measureTemp = measureRepository.findOne(AESencrpUtils.decryptLong(measure.getMeasureIdEncrypt()));
			measureTemp.setMeasureName(measure.getMeasureName());
			measureTemp.setDescription(measure.getDescription());
			measureTemp.setUpdate(user.getEmail(), new Date());
			measureRepository.save(measureTemp);
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(Measure measure, User user) throws Exception{
		logger.debug("I:");
		Measure measureTemp = measureRepository.findOne(AESencrpUtils.decryptLong(measure.getMeasureIdEncrypt()));
		checkGodungId(measureTemp, user);
		measureRepository.delete(measureTemp);
		logger.debug("O:");
	}

	public void checkGodungId(Measure measure,User userSession) throws GodungIdException {
		long godungIdTemp = measure.getGodung().getGodungId().longValue();
		long godungIdSession = userSession.getGodung().getGodungId().longValue();
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
	}
	
}