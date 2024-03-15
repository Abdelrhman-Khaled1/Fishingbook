package com.example.security.product;

import com.example.security.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ProductRepository productRepository;

    public void createProduct(ProductDto productDto){
//        Product product = new Product();
//        product.setTitle(product.getTitle());
//        product.setContent(productDto.getContent());
//        User currentUser = authenticationService.getCurrentUser().orElseThrow(()->new IllegalArgumentException("No user logged in"));
//        product.setUsername(currentUser.getUsername());

        Product product = mapFromDtoToProduct(productDto);
        product.setCreatedOn(Instant.now());

        productRepository.save(product);
    }

    public List<ProductDto> showAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapFromProductToDto).collect(Collectors.toList());
    }

    public ProductDto readSingleProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("For id "+id));
        return mapFromProductToDto(product);
    }

    private ProductDto mapFromProductToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setTitle(product.getTitle());
        productDto.setContent(product.getContent());
        productDto.setUsername(product.getUsername());
        return productDto;
    }

    private Product mapFromDtoToProduct(ProductDto productDto) {
        Product product = new Product();
        product.setTitle(productDto.getTitle());
        product.setContent(productDto.getContent());
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        product.setUsername(loggedInUser.getUsername());
        product.setUpdatedOn(Instant.now());
        return product;
    }

    public void updateProduct(Long id ,ProductDto productDto){

        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        Product product = productRepository.findById(id).get();
        if(product.getUsername().equals(loggedInUser.getUsername())){
            product.setTitle(productDto.getTitle());
            product.setContent(productDto.getContent());
            product.setUpdatedOn(Instant.now());

            productRepository.save(product);
        }else {
            throw new IllegalArgumentException("User Mismatch");
        }

    }

}
