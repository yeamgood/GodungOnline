
package com.yeamgood.godungonline.controller;

import java.util.Locale;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yeamgood.godungonline.bean.Pnotify;
import com.yeamgood.godungonline.bean.PnotifyType;
import com.yeamgood.godungonline.constants.Constants;
import com.yeamgood.godungonline.exception.EmailException;
import com.yeamgood.godungonline.form.ResetPasswordForm;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.MailBuilderService;
import com.yeamgood.godungonline.service.PasswordResetTokenService;
import com.yeamgood.godungonline.service.UserService;

@Controller
public class PasswordResetController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private MessageSource messageSource;
	
	@Autowired
	private PasswordResetTokenService passwordResetTokenService;
	
	 @Autowired
	 private JavaMailSender emailSender;
	 
	 @Autowired
	 private HttpServletRequest httpServletRequest;
	 
	 @Autowired
	 private MailBuilderService mailBuilderService;
	 
	 private static final String FORM_PASS_FORGOT = "form_password_forgot";
	 
	@RequestMapping(value={"/forgotPassword"}, method = RequestMethod.GET)
	public ModelAndView forgotPassword(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("user", new User());
		modelAndView.setViewName(FORM_PASS_FORGOT);
		return modelAndView;
	}
	
	@RequestMapping(value={"/forgotPassword"}, method = RequestMethod.POST)
	public ModelAndView forgotPasswordSendEmail(@Valid User user, BindingResult bindingResult,RedirectAttributes redirectAttributes) {
		logger.debug("I");
		ModelAndView modelAndView = new ModelAndView();
		
		if (bindingResult.hasFieldErrors("email")) {
			modelAndView.setViewName(FORM_PASS_FORGOT);
			return modelAndView;
		}
		
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists == null) {
			bindingResult.rejectValue("email", "error.user", messageSource.getMessage("validation.required.email.not.registered",null,LocaleContextHolder.getLocale()));
			modelAndView.setViewName(FORM_PASS_FORGOT);
			return modelAndView;
		}
		
		try {
			Locale userLocale = LocaleContextHolder.getLocale();
			String token = UUID.randomUUID().toString();
			passwordResetTokenService.createPasswordResetTokenForUser(userExists, token);
			String appUrl = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath();
			sendEmail(appUrl,userLocale,token,userExists);
			Pnotify pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"message.forgotpassword.sendmail.success");
			redirectAttributes.addFlashAttribute(pnotify);
			modelAndView.setViewName("redirect:/login");
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			Pnotify pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"message.forgotpassword.sendmail.error");
			modelAndView.addObject(pnotify);
			modelAndView.setViewName(FORM_PASS_FORGOT);
		}
		logger.debug("O");
		return modelAndView;
	}
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String changePassword(Locale locale, Model model, @RequestParam("id") long id,@RequestParam("token") String token,RedirectAttributes redirectAttributes) {
		String result = passwordResetTokenService.validatePasswordResetToken(id, token);
		if (result != null) {
			Pnotify pnotify = new Pnotify(messageSource,PnotifyType.ERROR,"auth.message.invalidToken");
			redirectAttributes.addFlashAttribute(pnotify);
			return "redirect:/login?lang=" + locale.getLanguage();
		}
		return "redirect:/resetPassword?lang=" + locale.getLanguage();
	}
	
	@RequestMapping(value={"/resetPassword"}, method = RequestMethod.GET)
	public ModelAndView resetPassword(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("resetPasswordForm", new ResetPasswordForm());
		modelAndView.setViewName("form_password_reset");
		return modelAndView;
	}
	
	@RequestMapping(value={"/savePassword"}, method = RequestMethod.POST)
	public ModelAndView savePassword(@Valid ResetPasswordForm resetPasswordForm, BindingResult bindingResult,RedirectAttributes redirectAttributes){
		ModelAndView modelAndView = new ModelAndView();
		if(!bindingResult.hasErrors() && !resetPasswordForm.getPassword().equals(resetPasswordForm.getConfirmPassword())) {
			String messageError = messageSource.getMessage("validation.required.password.compare",null,LocaleContextHolder.getLocale());
			bindingResult.rejectValue("password", "error.resetPasswordForm", messageError);
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("form_password_reset");
		}else {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userService.changeUserPassword(user, resetPasswordForm.getPassword());
			Pnotify pnotify = new Pnotify(messageSource,PnotifyType.SUCCESS,"message.resetpassword.success");
			redirectAttributes.addFlashAttribute(pnotify);
			modelAndView.setViewName("redirect:/login");
		}
		return modelAndView;
	}
	
	private void sendEmail(String contextPath, Locale locale, String token, User user) throws EmailException{
		logger.debug("I");
		try {
	        MimeMessage mineMessage = emailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mineMessage);
	        String url = contextPath + "/changePassword?id=" + user.getId() + "&token=" + token;
	        String content = mailBuilderService.passwordResetTemplatebuild(url,user);
	       
	        helper.setSubject(messageSource.getMessage("email.resetpassword.subject",null,locale));
	        helper.setTo(user.getEmail());
	        helper.setText(content,true);
	        emailSender.send(mineMessage);
	    	
		} catch (Exception e) {
			logger.error(Constants.MESSAGE_ERROR,e);
			throw new EmailException("Send email change password error");
		}
		logger.debug("O");
    }
	
	
}