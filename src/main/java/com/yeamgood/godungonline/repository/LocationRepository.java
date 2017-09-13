package com.yeamgood.godungonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Location;


@Repository("locationRepository")
public interface LocationRepository extends JpaRepository<Location, Long> {
//	public long countByGodungGodungId(Long godungId);
//    public Location findTopByGodungGodungIdOrderByLocationCodeDesc(Long godungId);
//	public List<Location> findAllByGodungGodungIdOrderByLocationCodeAsc(Long godungId);
}
