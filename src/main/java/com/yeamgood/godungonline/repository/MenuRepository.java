package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Menu;


@Repository("menuRepository")
public interface MenuRepository extends JpaRepository<Menu, Long> {
	public List<Menu> findAllByParentId(Long parentId);
}
