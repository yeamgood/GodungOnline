
package com.yeamgood.godungonline.controller;

import java.util.Locale;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import com.yeamgood.godungonline.form.ResetPasswordForm;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.MailContentBuilder;
import com.yeamgood.godungonline.service.PasswordResetTokenService;
import com.yeamgood.godungonline.service.UserService;

@Controller
public class ForgotPasswordController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private MessageSource message;
	
	@Autowired
	private PasswordResetTokenService passwordResetTokenService;
	
	 @Autowired
	 private JavaMailSender emailSender;
	 
	 @Autowired
	 private HttpServletRequest httpServletRequest;
	 
	 @Autowired
	 private MailContentBuilder mailContentBuilder;
	 
	@RequestMapping(value={"/forgot_password"}, method = RequestMethod.GET)
	public ModelAndView forgotPassword(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("user", new User());
		modelAndView.setViewName("forgot_password");
		return modelAndView;
	}
	
	@RequestMapping(value={"/forgot_password"}, method = RequestMethod.POST)
	public ModelAndView forgotPasswordSendEmail(@Valid User user, BindingResult bindingResult,RedirectAttributes redirectAttributes) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (!bindingResult.hasFieldErrors("email") && userExists == null) {
			bindingResult.rejectValue("email", "error.user", message.getMessage("validation.required.email.not.registered",null,LocaleContextHolder.getLocale()));
		}
		if (bindingResult.hasFieldErrors("email")) {
			modelAndView.setViewName("forgot_password");
		} else {
			Locale userLocale = LocaleContextHolder.getLocale();
			String token = UUID.randomUUID().toString();
			passwordResetTokenService.createPasswordResetTokenForUser(userExists, token);
			
			String appUrl = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + 
					        ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath();
			sendEmail(appUrl,userLocale,token,userExists);
			
			Pnotify pnotify = new Pnotify();
			pnotify.setTitle(message.getMessage("pnotify.title.success",null,userLocale));
			pnotify.setType(message.getMessage("pnotify.type.success",null,userLocale));
			pnotify.setText(message.getMessage("message.forgotpassword.sendmail.success",null,userLocale));
			redirectAttributes.addFlashAttribute(pnotify);
			modelAndView.setViewName("redirect:/login");
		}
		return modelAndView;
	}
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String showChangePasswordPage(Locale locale, Model model, @RequestParam("id") long id,@RequestParam("token") String token,RedirectAttributes redirectAttributes) {
		String result = passwordResetTokenService.validatePasswordResetToken(id, token);
		if (result != null) {
			Locale userLocale = LocaleContextHolder.getLocale();
			Pnotify pnotify = new Pnotify();
			pnotify.setTitle(message.getMessage("pnotify.title.error",null,userLocale));
			pnotify.setType(message.getMessage("pnotify.type.error",null,userLocale));
			pnotify.setText(message.getMessage("auth.message." + result,null,userLocale));
			redirectAttributes.addFlashAttribute(pnotify);
			return "redirect:/login?lang=" + locale.getLanguage();
		}
		return "redirect:/reset_password?lang=" + locale.getLanguage();
	}
	
	@RequestMapping(value={"/reset_password"}, method = RequestMethod.GET)
	public ModelAndView resetPassword(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("resetPasswordForm", new ResetPasswordForm());
		modelAndView.setViewName("reset_password");
		return modelAndView;
	}
	
	@RequestMapping(value={"/savePassword"}, method = RequestMethod.POST)
	public ModelAndView savePassword(@Valid ResetPasswordForm resetPasswordForm, BindingResult bindingResult,RedirectAttributes redirectAttributes){
		ModelAndView modelAndView = new ModelAndView();
		if(!bindingResult.hasErrors() && !resetPasswordForm.getPassword().equals(resetPasswordForm.getConfirmPassword())) {
			String messageError = message.getMessage("validation.required.password.compare",null,LocaleContextHolder.getLocale());
			bindingResult.rejectValue("password", "error.resetPasswordForm", messageError);
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("reset_password");
		}else {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userService.changeUserPassword(user, resetPasswordForm.getPassword());
			
			Pnotify pnotify = new Pnotify();
			pnotify.setTitle(message.getMessage("pnotify.title.success",null,LocaleContextHolder.getLocale()));
			pnotify.setType(message.getMessage("pnotify.type.success",null,LocaleContextHolder.getLocale()));
			pnotify.setText(message.getMessage("message.resetpassword.success",null,LocaleContextHolder.getLocale()));
			redirectAttributes.addFlashAttribute(pnotify);
			modelAndView.setViewName("redirect:/login");
		}
		return modelAndView;
	}
	
	private void sendEmail(String contextPath, Locale locale, String token, User user) throws Exception{
        MimeMessage mineMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mineMessage);
        
        String url = contextPath + "/changePassword?id=" + user.getId() + "&token=" + token;
        String content = mailContentBuilder.build(url,user);
       
        helper.setSubject(message.getMessage("email.resetpassword.subject",null,locale));
        helper.setTo(user.getEmail());
        helper.setText(content,true);
       
        emailSender.send(mineMessage);
    }
	
	
}