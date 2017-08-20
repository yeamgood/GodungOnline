package com.yeamgood.godungonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.GodungUserRole;


@Repository("godungUserRoleRepository")
public interface GodungUserRoleRepository extends JpaRepository<GodungUserRole, Long> {
	public GodungUserRole findById(Long id);
}