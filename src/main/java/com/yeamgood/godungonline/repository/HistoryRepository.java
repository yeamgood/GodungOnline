package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.History;


@Repository("historyRepository")
public interface HistoryRepository extends JpaRepository<History, Long> {
	public List<History> findAllByGodungGodungIdAndHistoryCodeOrderByCreateDateDesc(Long godungId,String historyCode);
}
