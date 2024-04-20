package com.example.security.fishinformation.dto;

import com.example.security.fishinformation.entity.FishDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FishDto {
    private Long id;
    private String name;
    private String imageUrl;
    private Set<FishDetailsDto> fishDetails;
}
