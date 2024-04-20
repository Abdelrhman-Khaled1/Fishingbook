package com.example.security.fishinformation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class FishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String imageUrl;

    @OneToMany(mappedBy = "fish", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FishDetails> fishDetails = new HashSet<>();

}
