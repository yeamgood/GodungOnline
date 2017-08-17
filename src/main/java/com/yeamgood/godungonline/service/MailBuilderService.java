package com.yeamgood.godungonline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.yeamgood.godungonline.model.User;

@Service
public class MailBuilderService {

	private TemplateEngine templateEngine;
	
	@Autowired
    private MessageSource message;
	 
    @Autowired
    public MailBuilderService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
 
    public String passwordResetTemplatebuild(String url,User user) {
        Context context = new Context();
        context.setVariable("url", url);
        context.setVariable("name", user.getName());
        context.setVariable("message0", message.getMessage("email.resetpassword.message0",null,LocaleContextHolder.getLocale()));
        context.setVariable("message1", message.getMessage("email.resetpassword.message1",null,LocaleContextHolder.getLocale()));
        context.setVariable("message2", message.getMessage("email.resetpassword.message2",null,LocaleContextHolder.getLocale()));
        context.setVariable("message3", message.getMessage("email.resetpassword.message3",null,LocaleContextHolder.getLocale()));
        context.setVariable("message4", message.getMessage("email.resetpassword.message4",null,LocaleContextHolder.getLocale()));
        context.setVariable("message5", message.getMessage("email.resetpassword.message5",null,LocaleContextHolder.getLocale()));
        context.setVariable("message6", message.getMessage("email.resetpassword.message6",null,LocaleContextHolder.getLocale()));
        context.setVariable("message7", message.getMessage("email.resetpassword.message7",null,LocaleContextHolder.getLocale()));
        return templateEngine.process("mail/template_password_reset", context);
    }
    
}
