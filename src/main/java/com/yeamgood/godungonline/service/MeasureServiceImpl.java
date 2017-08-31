package com.yeamgood.godungonline.service;

import java.util.Date;
import java.util.List;

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
import com.yeamgood.godungonline.utils.GenerateCodeUtils;

@Service("measureService")
public class MeasureServiceImpl implements MeasureService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MeasureRepository measureRepository;

	@Override
	public Measure findById(Long id) {
		logger.debug("I:");
		logger.debug("O:");
		return measureRepository.findOne(id);
	}

	@Override
	public List<Measure> findAllOrderByMeasureNameAsc() {
		logger.debug("I:");
		logger.debug("O:");
		return measureRepository.findAll(sortByMeasureNameAsc());
	}

	@Override
	public List<Measure> findAllByGodungGodungIdOrderByMeasureNameAsc(Long godungId) {
		logger.debug("I:[godungId]:" + godungId);
		logger.debug("O:");
		return measureRepository.findAllByGodungGodungIdOrderByMeasureNameAsc(godungId);
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
	public List<Measure> findByGodungGodungIdAndMeasureNameIgnoreCaseContaining(Long godungId, String measureName, Pageable pageable) {
		return measureRepository.findByGodungGodungIdAndMeasureNameIgnoreCaseContaining(godungId, measureName, pageable);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void save(Measure measure,User user) {
		logger.debug("I:");
		if(measure.getMeasureId() == null) {
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
			Measure measureTemp = measureRepository.findOne(measure.getMeasureId());
			measureTemp.setMeasureName(measure.getMeasureName());
			measureTemp.setDescription(measure.getDescription());
			measureTemp.setUpdate(user.getEmail(), new Date());
			measureRepository.save(measureTemp);
		}
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void delete(Measure measure, User user) throws GodungIdException {
		logger.debug("I:");
		Measure measureTemp = measureRepository.findOne(measure.getMeasureId());
		long godungIdTemp = measureTemp.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		
		if(godungIdTemp ==  godungIdSession) {
			measureRepository.delete(measureTemp);
		}else {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		logger.debug("O:");
	}

	@Override
	public Measure findById(Long id, User user) throws GodungIdException {
		logger.debug("I:");
		Measure measure = measureRepository.findOne(id);
		long godungIdTemp = measure.getGodung().getGodungId().longValue();
		long godungIdSession = user.getGodung().getGodungId().longValue();
		
		if(godungIdTemp !=  godungIdSession) {
			 throw new GodungIdException("GodungId database is " + godungIdTemp + " not equals session user is" + godungIdSession);
		}
		
		logger.debug("O:");
		return measure;
	}
}