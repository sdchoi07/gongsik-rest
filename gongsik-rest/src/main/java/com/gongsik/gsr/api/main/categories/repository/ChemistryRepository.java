package com.gongsik.gsr.api.main.categories.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.main.categories.entity.ChemistryEntity;

@Repository
public interface ChemistryRepository extends JpaRepository<ChemistryEntity, Long>{

	Optional<ChemistryEntity> findByChemistryNo(String cartItemNo);

}
