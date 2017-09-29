package com.yeamgood.godungonline.service;

import java.util.List;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.model.User;

public interface UserService {
	public User findByIdEncrypt(String idEncrypt,User userSession) throws GodungIdException ;
	public User findUserByEmail(String email);
	public void saveUser(User user);
	public void changeUserPassword(User user,String password);
	public List<User> findAll();
}