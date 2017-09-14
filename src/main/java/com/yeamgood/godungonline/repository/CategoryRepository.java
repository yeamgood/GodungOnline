package com.yeamgood.godungonline.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yeamgood.godungonline.model.Category;


@Repository("categoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Long> {
	public long countByGodungGodungId(Long godungId);
    public Category findTopByGodungGodungIdOrderByCategoryCodeDesc(Long godungId);
	public List<Category> findAllByGodungGodungIdOrderByCategoryNameAsc(Long godungId);
    public List<Category> findByGodungGodungIdAndCategoryNameIgnoreCaseContaining(Long godungId,String categoryName,Pageable pageable);

}
