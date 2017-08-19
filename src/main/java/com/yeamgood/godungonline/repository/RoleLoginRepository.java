package com.yeamgood.godungonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.RoleLogin;

@Repository("roleloginRepository")
public interface RoleLoginRepository extends JpaRepository<RoleLogin, Integer>{
	RoleLogin findByRole(String role);

}