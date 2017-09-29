package com.yeamgood.godungonline.service;

import com.yeamgood.godungonline.model.ApproverAction;

public interface ApproverActionService {
	public ApproverAction findOneByApproverActionCode(String code);
}