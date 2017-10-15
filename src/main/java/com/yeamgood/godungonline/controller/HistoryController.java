package com.yeamgood.godungonline.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.datatable.DataTableObject;
import com.yeamgood.godungonline.datatable.DataTablesRequest;
import com.yeamgood.godungonline.datatables.HistoryDatatables;
import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.History;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.HistoryService;
import com.yeamgood.godungonline.utils.DateUtils;

@Controller
public class HistoryController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    MessageSource messageSource;
	
	@Autowired
	HistoryService historyService;
	
	@RequestMapping(value="/user/history/list/ajax/{historyCode}", method=RequestMethod.GET)
	public @ResponseBody String list(@PathVariable String historyCode,DataTablesRequest datatableRequest, HttpSession session) throws GodungIdException, JsonProcessingException {
		logger.debug("I");
		logger.debug(Constants.LOG_INPUT, historyCode);
		User userSession = (User) session.getAttribute("user");

		List<History> historyList = historyService.findAll(userSession.getGodung().getGodungId(), historyCode);
		
		HistoryDatatables historyDatatables;
		List<HistoryDatatables> historyDatatablesList = new ArrayList<>();
		for (History history : historyList) {
			historyDatatables = new HistoryDatatables();
			historyDatatables.setHistoryType(history.getHistoryType());
			historyDatatables.setHistoryCode(history.getHistoryCode());
			historyDatatables.setEmployeeName(history.getEmployeeName());
			historyDatatables.setAction(history.getAction());
			historyDatatables.setDescription(history.getDescription());
			historyDatatables.setCreateDate(DateUtils.dateToString(history.getCreateDate(), DateUtils.DDMMYYYY_HHMMSS));
			historyDatatablesList.add(historyDatatables);
		}
		
		DataTableObject dataTableObject = new DataTableObject();
		dataTableObject.setAaData(new ArrayList<Object>(historyDatatablesList));
		logger.debug("O");
		return new ObjectMapper().writeValueAsString(dataTableObject);
	}
	
	

	
}