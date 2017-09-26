package com.yeamgood.godungonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Document;


@Repository("documentRepository")
public interface DocumentRepository extends JpaRepository<Document, Long> {
}
