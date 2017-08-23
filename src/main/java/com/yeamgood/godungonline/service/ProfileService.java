package com.yeamgood.godungonline.service;

import com.yeamgood.godungonline.form.ProfileForm;

public interface ProfileService {
	public void loadProfile(ProfileForm profileForm, Long userId, Long godungId);
	public void updateProfile(ProfileForm profileForm, Long userId, Long godungId);
}