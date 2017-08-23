package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Common;


@Repository("commonRepository")
public interface CommonRepository extends JpaRepository<Common, Long> {
	List<Common> findByType(String type);
}
