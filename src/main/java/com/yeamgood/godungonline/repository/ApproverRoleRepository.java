package com.yeamgood.godungonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.ApproverRole;


@Repository("approverRoleRepository")
public interface ApproverRoleRepository extends JpaRepository<ApproverRole, Long> {
	public ApproverRole findOneByApproverRoleCode(String code);
}
