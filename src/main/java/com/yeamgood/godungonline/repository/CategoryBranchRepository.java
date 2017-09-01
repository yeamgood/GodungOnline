package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.CategoryBranch;


@Repository("categoryBranchRepository")
public interface CategoryBranchRepository extends JpaRepository<CategoryBranch, Long> {
	List<CategoryBranch> findAllByCategoryId(Long categoryId);
	List<CategoryBranch> findAllByCategoryRefId(Long categoryRefId);
}
