package com.yeamgood.godungonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.PasswordResetToken;


@Repository("passwordResetTokenRepository")
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	 PasswordResetToken findByToken(String token);
}