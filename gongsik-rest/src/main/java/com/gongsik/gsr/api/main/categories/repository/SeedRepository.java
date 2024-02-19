package com.gongsik.gsr.api.main.categories.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.main.categories.entity.SeedEntity;

@Repository
public interface SeedRepository extends JpaRepository<SeedEntity, Long>{

	Optional<SeedEntity> findBySeedNo(String cartItemNo);

}
