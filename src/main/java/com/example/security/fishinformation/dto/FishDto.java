package com.example.security.fishinformation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FishDto {
    private Long id;
    private String name;
    private String imageUrl;
    private List<FishDetailsDto> fishDetails;
}
