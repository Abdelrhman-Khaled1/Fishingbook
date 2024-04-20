package com.example.security.fishinformation;

import com.example.security.fishinformation.dto.FishDto;
import com.example.security.fishinformation.entity.FishEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/fish")
public class FishController {
    @Autowired
    private FishService fishService;

    @PostMapping("/save")
    public void save(@RequestBody FishDto fishDto) {
        fishService.save(fishDto);
    }
    @GetMapping
    public Set<FishDto> getAll(){
        return fishService.getFishes();
    }
}
