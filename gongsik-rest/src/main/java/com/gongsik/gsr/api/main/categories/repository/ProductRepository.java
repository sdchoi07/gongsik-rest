package com.gongsik.gsr.api.main.categories.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.main.categories.dto.CategoriesDto;
import com.gongsik.gsr.api.main.categories.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String>{

	Optional<ProductEntity> findByProductNo(String cartNo);


}
