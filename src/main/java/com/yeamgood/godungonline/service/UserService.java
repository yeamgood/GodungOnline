package com.yeamgood.godungonline.service;

import com.yeamgood.godungonline.model.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
	public void changeUserPassword(User user,String password);
	public void updateProfile(User user);
}