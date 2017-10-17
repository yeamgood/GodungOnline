package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.form.UserForm;
import com.yeamgood.godungonline.form.UserPasswordForm;
import com.yeamgood.godungonline.model.User;

public interface UserService {
	public User findByIdEncrypt(String idEncrypt,User userSession) throws GodungIdException ;
	public User findUserByEmail(String email);
	public void saveUser(User user);
	public void updateUser(UserForm userForm);
	public void changeUserPassword(User user,String password);
	public void changeUserPassword(UserPasswordForm userPasswordForm);
	public void delete(User user);
	public List<User> findAll();
}