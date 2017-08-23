package com.yeamgood.godungonline.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.model.Godung;
import com.yeamgood.godungonline.model.GodungUserRole;
import com.yeamgood.godungonline.model.Role;
import com.yeamgood.godungonline.model.RoleLogin;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.GodungRepository;
import com.yeamgood.godungonline.repository.GodungUserRoleRepository;
import com.yeamgood.godungonline.repository.RoleLoginRepository;
import com.yeamgood.godungonline.repository.RoleRepository;
import com.yeamgood.godungonline.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private RoleLoginRepository roleLoginRepository;
	
	@Autowired
    private GodungRepository godungRepository;
	
	@Autowired
    private RoleRepository roleRepository;
	
	@Autowired
	private GodungUserRoleRepository godungUserRoleRepository;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	private final String ROLELOGIN_USER = "USER";
	private final Long ROLE_ADMIN_FREE = (long) 2;
	private final String USER_SYSTEM = "SYSTEM";
	private final int ACTIVE = 1;
	
	@Override
	@Transactional(rollbackFor={Exception.class})
	public void saveUser(User user) {
		logger.debug("I:");
		Role role = roleRepository.findById(ROLE_ADMIN_FREE);
		
		Godung godung = user.getGodung();
		godung.setActive(ACTIVE);
		godung.setCreateUser(USER_SYSTEM);
		godung.setCreateDate(new Date());
		godungRepository.save(godung);
		
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(ACTIVE);
        RoleLogin userRole = roleLoginRepository.findByRole(ROLELOGIN_USER);
        user.setRoles(new HashSet<RoleLogin>(Arrays.asList(userRole)));
        user.setLanguage(LocaleContextHolder.getLocale().getLanguage());
		userRepository.save(user);
		
		GodungUserRole godungUserRole = new GodungUserRole();
		godungUserRole.setGodung(godung);
		godungUserRole.setUser(user);
		godungUserRole.setRole(role);
		godungUserRoleRepository.save(godungUserRole);
		logger.debug("O:");
	}

	@Override
	public void changeUserPassword(User user, String password) {
		user.setPassword(bCryptPasswordEncoder.encode(password));
		userRepository.save(user);
	}

	@Override
	public void updateProfile(User user) {
		
	}

}