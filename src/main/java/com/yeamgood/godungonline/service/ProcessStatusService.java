package com.yeamgood.godungonline.service;

import com.yeamgood.godungonline.model.ProcessStatus;

public interface ProcessStatusService {
	public ProcessStatus findOneByProcessStatusCode(String processStatusCode);
	public ProcessStatus generateProcessStatus(ProcessStatus processStatus);
}