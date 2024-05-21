package com.example.security.fishinformation;

import com.example.security.fishinformation.dto.FishDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fish")
public class FishController {
    @Autowired
    private FishService fishService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public void save(@RequestBody FishDto fishDto) {
        fishService.save(fishDto);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public void update(@RequestBody FishDto fishDto) {
        fishService.update(fishDto);
    }
    @GetMapping
    public List<FishDto> getAll(){
        return fishService.getFishes();
    }
}
