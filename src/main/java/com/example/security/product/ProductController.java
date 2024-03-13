package com.example.security.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity createProduct(@RequestBody ProductDto productDto){
        productService.createProduct(productDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> showAllProducts(){
        return new ResponseEntity<>(productService.showAllProducts(),HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable @RequestBody Long id){
        return new ResponseEntity<>(productService.readSingleProduct(id),HttpStatus.OK);
    }

}

