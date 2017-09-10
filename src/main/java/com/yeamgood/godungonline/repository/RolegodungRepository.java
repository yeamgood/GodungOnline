package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Rolegodung;


@Repository("rolegodungRepository")
public interface RolegodungRepository extends JpaRepository<Rolegodung, Long> {
	public long countByGodungGodungId(Long godungId);
    public Rolegodung findTopByGodungGodungIdOrderByRolegodungCodeDesc(Long godungId);
	public List<Rolegodung> findAllByGodungGodungIdOrderByRolegodungCodeAsc(Long godungId);
}
