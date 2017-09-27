package com.yeamgood.godungonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Approver;


@Repository("approverRepository")
public interface ApproverRepository extends JpaRepository<Approver, Long> {
}
