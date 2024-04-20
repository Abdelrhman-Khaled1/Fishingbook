package com.example.security.fishinformation.repo;

import com.example.security.fishinformation.entity.FishDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FishDetailsRepository extends JpaRepository<FishDetails,Long> {
}
