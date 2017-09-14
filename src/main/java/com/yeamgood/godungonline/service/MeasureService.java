package com.yeamgood.godungonline.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Measure;
import com.yeamgood.godungonline.model.User;

public interface MeasureService {
	public Measure findByIdEncrypt(String idEncrypt) throws Exception;
	public Measure findByIdEncrypt(String idEncrypt,User user) throws Exception;
	public List<Measure> findAllOrderByMeasureNameAsc() throws Exception;
	public List<Measure> findAllByGodungGodungIdOrderByMeasureNameAsc(Long godungId) throws Exception;
	public List<Measure> findByGodungGodungIdAndMeasureNameIgnoreCaseContaining(Long godungId, String measure, Pageable pageable) throws Exception;
	public long count(Long godungId);
	public void save(Measure measure,User user) throws Exception;
	public void delete(Measure measure,User user) throws GodungIdException, Exception;
}