package com.kristof.assignment.db.repository;

import com.kristof.assignment.db.entity.DogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DogRepository extends JpaRepository<DogEntity, Long>, JpaSpecificationExecutor<DogEntity> {

        Optional<DogEntity> findFirstByImageIsNullOrImageEquals(String image);
}
