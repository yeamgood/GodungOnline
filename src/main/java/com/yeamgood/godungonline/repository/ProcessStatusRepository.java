package com.yeamgood.godungonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.ProcessStatus;


@Repository("processStatusRepository")
public interface ProcessStatusRepository extends JpaRepository<ProcessStatus, Long> {
	public ProcessStatus findOneByProcessStatusCode(String processStatusCode);
}
