package com.yeamgood.godungonline.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.Measure;
import com.yeamgood.godungonline.model.User;

public interface MeasureService {
	public Measure findByIdEncrypt(String idEncrypt) ;
	public Measure findByIdEncrypt(String idEncrypt,User user) throws GodungIdException ;
	public List<Measure> findAllOrderByMeasureNameAsc() ;
	public List<Measure> findAllByGodungGodungIdOrderByMeasureNameAsc(Long godungId) ;
	public List<Measure> findByGodungGodungIdAndMeasureNameIgnoreCaseContaining(Long godungId, String measure, Pageable pageable) ;
	public long count(Long godungId);
	public void save(Measure measure,User user) ;
	public void delete(Measure measure,User user) throws GodungIdException;
}