package com.yeamgood.godungonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Dealer;


@Repository("dealerRepository")
public interface DealerRepository extends JpaRepository<Dealer, Long> {
}
