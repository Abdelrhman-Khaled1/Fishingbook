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
    public ResponseEntity createProduct(@RequestBody ProductDtoRequest productDtoRequest){
        productService.createProduct(productDtoRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductDtoResponse>> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(),HttpStatus.OK);
    }

    @GetMapping("/getByCategoryId/{id}")
    public ResponseEntity<List<ProductDtoResponse>> getProductsByCategoryId(@PathVariable Long id){
        return new ResponseEntity<>(productService.getProductsByCategoryId(id),HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDtoResponse> getProduct(@PathVariable Long id){
        return new ResponseEntity<>(productService.getProductById(id),HttpStatus.OK);
    }
    @PostMapping("/update")
    public ResponseEntity updateProduct(@RequestBody ProductDtoUpdate productDtoUpdate){
        productService.updateProduct(productDtoUpdate);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/getProductByUserId/{id}")
    public ResponseEntity<List<ProductDtoResponse>> getProductByUserId (@PathVariable Long id){
        return new ResponseEntity<>(productService.getProductByUserId(id),HttpStatus.OK);
    }



}

