package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.MenuPrivilege;


@Repository("menuPrivilegeRepository")
public interface MenuPrivilegeRepository extends JpaRepository<MenuPrivilege, Long> {
	public long countByMenuId(Long menuId);
	public List<MenuPrivilege> findAllByMenuIdOrderByMenuPrivilegeNameAsc(Long menuId);
}
