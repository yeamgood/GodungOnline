package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.model.ApproverRole;

public interface ApproverRoleService {
	public ApproverRole findOneByApproverRoleCode(String code);
	public List<ApproverRole> findAll();
}