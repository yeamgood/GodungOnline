package com.yeamgood.godungonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Godung;


@Repository("godungRepository")
public interface GodungRepository extends JpaRepository<Godung, Long> {
	public Godung findById(Long id);
}