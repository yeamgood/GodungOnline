package com.yeamgood.godungonline.bean;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;

import com.yeamgood.godungonline.constants.Constants;

public class JsonResponse {
	private String status;
	private Object result;
	
	public void setObject(String status,Object result) {
		this.status = status;
		this.result = result;
	}
	
	public void setLoadSuccess(MessageSource messageSource) {
		this.status = Constants.STATUS_SUCCESS;
		this.result = new Pnotify(messageSource, PnotifyType.SUCCESS, Constants.ACTION_LOAD_SUCCESS);
	}
	
	public void setLoadError(MessageSource messageSource) {
		this.status = Constants.STATUS_ERROR;
		this.result = new Pnotify(messageSource, PnotifyType.ERROR, Constants.ACTION_LOAD_ERROR);
	}
	
	public void setSaveSuccess(MessageSource messageSource) {
		this.status = Constants.STATUS_SUCCESS;
		this.result = new Pnotify(messageSource, PnotifyType.SUCCESS, Constants.ACTION_SAVE_SUCCESS);
	}
	
	public void setSaveError(MessageSource messageSource) {
		this.status = Constants.STATUS_ERROR;
		this.result = new Pnotify(messageSource, PnotifyType.ERROR, Constants.ACTION_SAVE_ERROR);
	}
	
	public void setDeleteSuccess(MessageSource messageSource) {
		this.status = Constants.STATUS_SUCCESS;
		this.result = new Pnotify(messageSource, PnotifyType.SUCCESS, Constants.ACTION_DELETE_SUCCESS);
	}
	
	public void setDeleteError(MessageSource messageSource) {
		this.status = Constants.STATUS_ERROR;
		this.result = new Pnotify(messageSource, PnotifyType.ERROR, Constants.ACTION_DELETE_ERROR);
	}
	
	public void setBinddingResultError(BindingResult bindingResult) {
		this.status = Constants.STATUS_ERROR;
		this.result = bindingResult.getFieldErrors();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
