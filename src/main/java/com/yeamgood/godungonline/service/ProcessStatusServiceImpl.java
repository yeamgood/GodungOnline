package com.yeamgood.godungonline.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.bean.ProcessStatusCode;
import com.yeamgood.godungonline.model.ProcessStatus;
import com.yeamgood.godungonline.repository.ProcessStatusRepository;

@Service("processStatusService")
public class ProcessStatusServiceImpl implements ProcessStatusService{
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProcessStatusRepository processStatusRepository;

	@Override
	public ProcessStatus findOneByProcessStatusCode(String processStatusCode) {
		logger.debug("I");
		logger.debug("O");
		return processStatusRepository.findOneByProcessStatusCode(processStatusCode);
	}

	@Override
	public ProcessStatus generateProcessStatus(ProcessStatus processStatus) {
		logger.debug("I");
		ProcessStatus processStatusResult;
		if(processStatus == null) {
			processStatusResult = processStatusRepository.findOneByProcessStatusCode(ProcessStatusCode.process.toString());
		}else {
			processStatusResult = processStatusRepository.findOneByProcessStatusCode(processStatus.getProcessStatusCode());
		}
		logger.debug("O");
		return processStatusResult;
	}
	
}