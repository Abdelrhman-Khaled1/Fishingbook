package com.example.security.product;

import com.example.security.auth.AuthenticationService;
import com.example.security.user.User;
import com.example.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public void createProduct(ProductDto productDto){
        Product product = new Product();

        product.setTitle(productDto.getTitle());
        product.setContent(productDto.getContent());
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        Optional<User> user = userRepository.findByEmail(loggedInUser.getUsername());
        product.setUser(user.get());

//        Product product = mapFromDtoToProduct(productDto);
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
        return productDto;
    }

    private Product mapFromDtoToProduct(ProductDto productDto) {
        Product product = new Product();
        product.setTitle(productDto.getTitle());
        product.setContent(productDto.getContent());
        product.setUpdatedOn(Instant.now());
        return product;
    }

    public void updateProduct(Long id ,ProductDto productDto){

        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        Product product = productRepository.findById(id).get();
        if(product.getUser().getEmail().equals(loggedInUser.getUsername())){
            product.setTitle(productDto.getTitle());
            product.setContent(productDto.getContent());
            product.setUpdatedOn(Instant.now());

            productRepository.save(product);
        }else {
            throw new IllegalArgumentException("User Mismatch");
        }

    }

    public List<ProductDto> getProductByUserId(Long id){
        List<Product> productsByUserId = productRepository.findByUserId(id);
        return productsByUserId.stream().map(this::mapFromProductToDto).collect(Collectors.toList());
    }

}
