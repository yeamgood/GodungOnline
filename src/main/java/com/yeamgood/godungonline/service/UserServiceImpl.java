package com.yeamgood.godungonline.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeamgood.godungonline.model.Godung;
import com.yeamgood.godungonline.model.Role;
import com.yeamgood.godungonline.model.RoleLogin;
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.GodungRepository;
import com.yeamgood.godungonline.repository.RoleLoginRepository;
import com.yeamgood.godungonline.repository.RoleRepository;
import com.yeamgood.godungonline.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private RoleLoginRepository roleLoginRepository;
	
	@Autowired
    private GodungRepository godungRepository;
	
	@Autowired
    private RoleRepository roleRepository;
    
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	private final Long ROLE_ADMIN = (long) 1;
	private final Long ROLE_USER = (long) 2;
	private final String USER_SYSTEM = "SYSTEM";
	private final int ACTIVE = 1;
	
	@Override
	@Transactional(rollbackFor={Exception.class})
	public void saveUser(User user) {
		
		Role role = roleRepository.findById(ROLE_USER);
		List<Role> roleList = new  ArrayList<Role>();
		roleList.add(role);
		
		Godung godung = user.getGodung();
		godung.setCreateUser(USER_SYSTEM);
		godung.setCreateDate(new Date());
		godung.setRoleList(roleList);
		godungRepository.save(godung);
		
		List<Godung> godungs = new ArrayList<Godung>();
		godungs.add(godung);

		user.setGodungs(godungs);
		user.setRole(role);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(ACTIVE);
        RoleLogin userRole = roleLoginRepository.findByRole("USER");
        user.setRoles(new HashSet<RoleLogin>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

	@Override
	public void changeUserPassword(User user, String password) {
		user.setPassword(bCryptPasswordEncoder.encode(password));
		userRepository.save(user);
	}
	
	

}