package com.example.security.fishinformation;

import com.example.security.fishinformation.dto.FishDetailsDto;
import com.example.security.fishinformation.dto.FishDto;
import com.example.security.fishinformation.entity.FishDetails;
import com.example.security.fishinformation.entity.FishEntity;
import com.example.security.fishinformation.repo.FishDetailsRepository;
import com.example.security.fishinformation.repo.FishRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FishService {
    @Autowired
    private FishRepository fishRepository;
    @Autowired
    private FishDetailsRepository detailsRepo;

    public List<FishDto> getFishes() {
        return fishRepository.findAll().stream().map(fishEntity -> {
            return new FishDto(fishEntity.getId(),
                    fishEntity.getName(),
                    fishEntity.getImageUrl(),
                    fishEntity.getFishDetails().stream().map(fishDetails -> {
                                return new FishDetailsDto(fishDetails.getHeader(),
                                        fishDetails.getContent());
                            }
                    ).collect(Collectors.toList())
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public void save(FishDto fishDto) {
        // Convert FishDto to FishEntity
        FishEntity fishEntity = new FishEntity();
        fishEntity.setId(fishDto.getId());
        fishEntity.setName(fishDto.getName());
        fishEntity.setImageUrl(fishDto.getImageUrl());

        List<FishDetails> fishDetailsList = new ArrayList<>();
        if (fishDto.getFishDetails() != null) {
            for (FishDetailsDto detailsDto : fishDto.getFishDetails()) {
                FishDetails detail = new FishDetails();
                detail.setHeader(detailsDto.getHeader());
                detail.setContent(detailsDto.getContent());
                detail.setFish(fishEntity); // Set the relationship
                fishDetailsList.add(detail);
            }
        }

        fishEntity.setFishDetails(fishDetailsList);

        // Save FishEntity along with FishDetails
        fishRepository.save(fishEntity);
    }

}
