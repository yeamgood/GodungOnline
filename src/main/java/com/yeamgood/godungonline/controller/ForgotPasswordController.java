package com.yeamgood.godungonline.controller;

import java.util.Locale;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
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
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.service.PasswordResetTokenService;
import com.yeamgood.godungonline.service.UserService;

@Controller
public class ForgotPasswordController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private MessageSource messageSource;
	
	@Autowired
	private PasswordResetTokenService passwordResetTokenService;
	
	 @Autowired
	 private JavaMailSender emailSender;

	 @Autowired
     private ServletContext servletContext;
	 
	 @Autowired
	 private HttpServletRequest httpServletRequest;
	 
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
			bindingResult.rejectValue("email", "error.user", messageSource.getMessage("validation.required.email.not.registered",null,LocaleContextHolder.getLocale()));
		}
		if (bindingResult.hasFieldErrors("email")) {
			modelAndView.setViewName("forgot_password");
		} else {
			Locale userLocale = LocaleContextHolder.getLocale();
			String token = UUID.randomUUID().toString();
			passwordResetTokenService.createPasswordResetTokenForUser(userExists, token);
			
			//mailSender.send(constructResetTokenEmail(getAppUrl(request),request.getLocale(), token, user));
			String appUrl = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + 
					        ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath();
			sendEmail(appUrl,userLocale,token,userExists);
			
			Pnotify pnotify = new Pnotify();
			pnotify.setTitle(messageSource.getMessage("pnotify.title.success",null,userLocale));
			pnotify.setType(messageSource.getMessage("pnotify.type.success",null,userLocale));
			pnotify.setText(messageSource.getMessage("message.user.register.success",null,userLocale));
			redirectAttributes.addFlashAttribute(pnotify);
			//modelAndView.addObject("pnotify", pnotify);
			
			//modelAndView.addObject("user", new User());
			modelAndView.setViewName("redirect:/login");
		}
		return modelAndView;
	}
	
	private void sendEmail(String contextPath, Locale locale, String token, User user) throws Exception{
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        
        String url = contextPath + "/changePassword?id=" + user.getId() + "&token=" + token;
        String messages = messageSource.getMessage("email.message.resetpassword",null,locale);
    	
        helper.setTo(user.getEmail());
        helper.setText(messages + url);
        helper.setSubject("Reset Password");
       
        emailSender.send(message);
    }
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String showChangePasswordPage(Locale locale, Model model, @RequestParam("id") long id,@RequestParam("token") String token) {
		String result = passwordResetTokenService.validatePasswordResetToken(id, token);
		if (result != null) {
			model.addAttribute("message", messageSource.getMessage("auth.message." + result, null, locale));
			return "redirect:/login?lang=" + locale.getLanguage();
		}
		return "redirect:/updatePassword?lang=" + locale.getLanguage();
	}
	
//	@RequestMapping(value = "/savePassword", method = RequestMethod.POST)
//	public ModelAndView savePassword(Locale locale, @Valid PasswordDto passwordDto) {
//	    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//	     
//	    userService.changeUserPassword(user, passwordDto.getNewPassword());
//	    return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
//	}
	
}