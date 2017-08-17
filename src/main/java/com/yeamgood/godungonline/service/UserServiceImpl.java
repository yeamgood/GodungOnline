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
import com.yeamgood.godungonline.model.User;
import com.yeamgood.godungonline.repository.GodungRepository;
import com.yeamgood.godungonline.repository.RoleRepository;
import com.yeamgood.godungonline.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
    private RoleRepository roleRepository;
	@Autowired
    private GodungRepository godungRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional(rollbackFor={Exception.class})
	public void saveUser(User user) {
		
		Godung godung = new Godung();
		godung.setActive(1);
		godung.setCreateUser("SYSTEM");
		godung.setCreateDate(new Date());
		godungRepository.save(godung);
		List<Godung> godungs = new ArrayList<Godung>();
		godungs.add(godung);

		user.setGodungs(godungs);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

	@Override
	public void changeUserPassword(User user, String password) {
		user.setPassword(bCryptPasswordEncoder.encode(password));
		userRepository.save(user);
	}
	
	

}