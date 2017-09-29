package com.yeamgood.godungonline.service;

import java.util.Arrays;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.yeamgood.godungonline.model.PasswordResetToken;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.PasswordResetTokenRepository;


@Service("passwordResetTokenService")
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService{
	
	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;

	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
		PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
	}
	
	@Override
	public String validatePasswordResetToken(long id, String token) {
	    PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);
	    if ((passToken == null) || (passToken.getUser().getUserId() != id)) {
	        return "invalidToken";
	    }
	 
	    Calendar cal = Calendar.getInstance();
	    if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	        return "expired";
	    }
	 
	    User user = passToken.getUser();
	    Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
	    SecurityContextHolder.getContext().setAuthentication(auth);
	    return null;
	}
}