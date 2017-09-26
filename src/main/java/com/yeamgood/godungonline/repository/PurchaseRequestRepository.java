package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.PurchaseRequest;


@Repository("purchaseRequestRepository")
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
	public long countByGodungGodungId(Long godungId);
    public PurchaseRequest findTopByGodungGodungIdOrderByPurchaseRequestCodeDesc(Long godungId);
	public List<PurchaseRequest> findAllByGodungGodungIdOrderByPurchaseRequestCodeAsc(Long godungId);
}
