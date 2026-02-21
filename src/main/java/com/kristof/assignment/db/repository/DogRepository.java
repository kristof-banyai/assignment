package com.kristof.assignment.db.repository;

import com.kristof.assignment.db.entity.DogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DogRepository extends JpaRepository<DogEntity, Long> {

        Optional<DogEntity> findFirstByImageIsNullOrImageEquals(String image);
}
