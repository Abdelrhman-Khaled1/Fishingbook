package com.example.security.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<List<ProductDtoLiked>> getProductByUserId (@PathVariable Long id){
        return new ResponseEntity<>(productService.getProductByUserId(id),HttpStatus.OK);
    }

    @GetMapping("/getProductsThatIOwn")
    public ResponseEntity<List<ProductDtoResponseIOwn>> getProductsThatIOwn(){
        return new ResponseEntity<>(productService.getProductsThatIOwn(),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/byTitleContains/{title}")
    public ResponseEntity<List<ProductDtoResponse>> getProductByTitleContains(@PathVariable String title){
        return new ResponseEntity<>(productService.getProductsByTitleContains(title),HttpStatus.OK);
    }

    @PutMapping("/like/{id}")
    public ResponseEntity addProductToLiked(@PathVariable Long id){
        productService.addProductToLiked(id);
        return new ResponseEntity(HttpStatus.OK);
    }
    @PutMapping("/dislike/{id}")
    public void deleteProductFromLiked(@PathVariable Long id){
        productService.deleteProductFromLiked(id);
    }


    @GetMapping("/likedProducts")
    public ResponseEntity<List<ProductDtoResponseWithUserData>> getLikedProducts(){
        return new ResponseEntity<>(productService.getLikedProducts(),HttpStatus.OK);
    }

    @GetMapping("/allProductsWithFlagLikedOnes")
    public ResponseEntity<List<ProductDtoLiked>> allProductsWithFlagLikedOnes(){
        return new ResponseEntity<>(productService.allProductsWithFlagLikedOnes(),HttpStatus.OK);
    }

    @PutMapping("/report/{id}")
    public ResponseEntity reportProduct(@PathVariable Long id){
        productService.reportProduct(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reported")
    public List<ReportedProductDto> getReportedProducts() {
        return productService.getReportedProducts();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/delete/{productId}")
    public ResponseEntity adminDeleteProduct(@PathVariable Long productId) {
        productService.adminDeleteProduct(productId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/reports/delete/{productId}")
    public ResponseEntity adminDeleteAllReportsForProduct(@PathVariable Long productId) {
        productService.deleteAllReportsForProduct(productId);
        return new ResponseEntity(HttpStatus.OK);
    }


}

