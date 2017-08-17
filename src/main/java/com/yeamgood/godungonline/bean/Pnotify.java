package com.yeamgood.godungonline.bean;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class Pnotify {

	private String type;
    	private String title;
	private String text;
	
	public Pnotify() {};
	
	public Pnotify(MessageSource messageSource,PnotifyType pnotifyType,String messageI18n) {
		  switch (pnotifyType) {
	          case SUCCESS:
	        	  	  this.type = messageSource.getMessage("pnotify.type.success",null,LocaleContextHolder.getLocale());
	      		  this.title = messageSource.getMessage("pnotify.title.success",null,LocaleContextHolder.getLocale());
	              break;
	                  
	          case INFO:
	        	  	  this.type = messageSource.getMessage("pnotify.type.info",null,LocaleContextHolder.getLocale());
	      		  this.title = messageSource.getMessage("pnotify.title.info",null,LocaleContextHolder.getLocale());
	              break;
	                       
	          case NOTICE:
	        	      this.type = messageSource.getMessage("pnotify.type.notice",null,LocaleContextHolder.getLocale());
	      		  this.title = messageSource.getMessage("pnotify.title.notice",null,LocaleContextHolder.getLocale());
	              break;
	              
	          case ERROR:
	        	      this.type = messageSource.getMessage("pnotify.type.error",null,LocaleContextHolder.getLocale());
	      		  this.title = messageSource.getMessage("pnotify.title.error",null,LocaleContextHolder.getLocale());
	              break;
	              
	          case DARK:
	        	      this.type = messageSource.getMessage("pnotify.type.dark",null,LocaleContextHolder.getLocale());
	      		  this.title = messageSource.getMessage("pnotify.title.dark",null,LocaleContextHolder.getLocale());
	              break;
	                      
	          default:
	        	      this.type = messageSource.getMessage("pnotify.type.info",null,LocaleContextHolder.getLocale());
	      		  this.title = messageSource.getMessage("pnotify.title.info",null,LocaleContextHolder.getLocale());
	              break;
	      }
		  this.text = messageSource.getMessage(messageI18n,null,LocaleContextHolder.getLocale());
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
