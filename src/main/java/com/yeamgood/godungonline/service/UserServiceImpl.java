package com.yeamgood.godungonline.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.exception.GodungIdException;
import com.yeamgood.godungonline.form.UserForm;
import com.yeamgood.godungonline.form.UserPasswordForm;
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
import com.yeamgood.godungonline.utils.AESencrpUtils;

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
	
	private static final String ROLELOGIN_USER = "USER";
	private static final Long ROLE_ADMIN_FREE = (long) 2;
	private static final String USER_SYSTEM = "SYSTEM";
	private static final int ACTIVE = 1;
	
	@Override
	public User findByIdEncrypt(String idEncrypt,User userSession) throws GodungIdException  {
		logger.debug("I:");
		User user = userRepository.findOne(AESencrpUtils.decryptLong(idEncrypt));
		user.encryptData();
		logger.debug("testyeam " + user.getEmail());
		logger.debug("O:");
		return user;
	}
	
	@Override
	@Transactional(rollbackFor={Exception.class})
	public void saveUser(User user) {
		logger.debug("I:");
		Role role = roleRepository.findOne(ROLE_ADMIN_FREE);
		User userSystem = new User();
		userSystem.setEmail(USER_SYSTEM);
		
		Godung godung = user.getGodung();
		godung.setActive(ACTIVE);
		godung.setCreate(userSystem);
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
	public void updateUser(UserForm userForm) {
		logger.debug("I:");
		User user = userRepository.findOne(AESencrpUtils.decryptLong(userForm.getUserIdEncrypt()));
		user.setEmail(userForm.getEmail());
		user.setName(userForm.getName());
		user.setLanguage(userForm.getLanguage());
		user.setActive(userForm.getActive());
		userRepository.save(user);
		logger.debug("O:");
	}

	@Override
	public void changeUserPassword(User user, String password) {
		user.setPassword(bCryptPasswordEncoder.encode(password));
		userRepository.save(user);
	}
	
	@Override
	public void changeUserPassword(UserPasswordForm userPasswordForm) {
		User user = userRepository.findOne(AESencrpUtils.decryptLong(userPasswordForm.getUserIdEncrypt()));
		user.setPassword(bCryptPasswordEncoder.encode(userPasswordForm.getPassword()));
		userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		logger.debug("I");
		List<User> userList = userRepository.findAll();
		for (User user : userList) {
			user.encryptData();
		}
		logger.debug("O");
		return userList;
	}

	@Override
	public void delete(User user) {
		logger.debug("I:");
		User userTemp = userRepository.findOne(AESencrpUtils.decryptLong(user.getUserIdEncrypt()));
		userRepository.delete(userTemp);
		logger.debug("O:");
	}


}