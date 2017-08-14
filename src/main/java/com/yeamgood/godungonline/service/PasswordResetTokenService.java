package com.yeamgood.godungonline.service;

import com.yeamgood.godungonline.model.User;

public interface PasswordResetTokenService {
	public void createPasswordResetTokenForUser(User user,String token);
	public String validatePasswordResetToken(long id, String token);
}