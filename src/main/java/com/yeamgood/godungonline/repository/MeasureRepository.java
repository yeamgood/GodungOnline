package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Measure;


@Repository("measureRepository")
public interface MeasureRepository extends JpaRepository<Measure, Long> {
	public long countByGodungGodungId(Long godungId);
    public Measure findTopByGodungGodungIdOrderByMeasureCodeDesc(Long godungId);
	public List<Measure> findAllByGodungGodungIdOrderByMeasureNameAsc(Long godungId);
    public List<Measure> findByGodungGodungIdAndMeasureNameIgnoreCaseContaining(Long godungId,String measureName,Pageable pageable);

}
