package com.yeamgood.godungonline.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.form.ProfileForm;
import com.yeamgood.godungonline.model.Godung;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.GodungRepository;
import com.yeamgood.godungonline.repository.UserRepository;

@Service("profileService")
public class ProfileServiceImpl implements ProfileService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GodungRepository godungRepository;
	
	@Override
	public void loadProfile(ProfileForm profileForm, Long userId, Long godungId) {
		logger.debug("I:");
		User user = userRepository.findOne(userId);
		Godung godung = godungRepository.findOne(godungId);
		profileForm.setEmail(user.getEmail());
		profileForm.setName(user.getName());
		profileForm.setGodungName(godung.getGodungName());
		profileForm.setLanguage(user.getLanguage());
		logger.debug("O: profileForm:" + profileForm);
		logger.debug("O:");
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void updateProfile(ProfileForm profileForm, Long userId, Long godungId) {
		logger.debug("I:");
		User user = userRepository.findOne(userId);
		user.setName(profileForm.getName());
		user.setLanguage(profileForm.getLanguage());
		userRepository.save(user);
		
		Godung godung = godungRepository.findOne(godungId);
		godung.setGodungName(profileForm.getGodungName());
		godungRepository.save(godung);
		logger.debug("O:");
	}
	
}