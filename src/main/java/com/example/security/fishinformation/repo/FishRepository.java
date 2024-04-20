package com.example.security.fishinformation.repo;

import com.example.security.fishinformation.entity.FishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FishRepository extends JpaRepository<FishEntity,Long> {
}
