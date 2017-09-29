package com.yeamgood.godungonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.ApproverAction;


@Repository("approverActionRepository")
public interface ApproverActionRepository extends JpaRepository<ApproverAction, Long> {
	public ApproverAction findOneByApproverActionCode(String code);
}
