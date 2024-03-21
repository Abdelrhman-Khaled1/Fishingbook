package com.example.security.product;

import com.example.security.auth.AuthenticationService;
import com.example.security.user.User;
import com.example.security.user.UserRepository;
import com.example.security.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;//TODO:: convert into user service

    @Autowired
    private UserService userService;

    public void createProduct(ProductDtoRequest productDtoRequest) {
        Product product = new Product();

        product.setTitle(productDtoRequest.getTitle());
        product.setContent(productDtoRequest.getContent());

        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();

        product.setPublisher(user);
        product.setCreatedOn(Instant.now());
        product.setCategory(new Category(productDtoRequest.getCategoryId()));

//        Product product = mapFromDtoToProduct(productDtoRequest);

        productRepository.save(product);
    }

    public List<ProductDtoResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapFromProductToDto).collect(Collectors.toList());
    }

    public List<ProductDtoResponse> getProductsByCategoryId(Long id){
        List<Product> productsByCategory_id = productRepository.findByCategory_Id(id);
        return productsByCategory_id.stream()
                .map(product -> new ProductDtoResponse(
                        product.getId(),
                        product.getTitle(),
                        product.getContent(),
                        product.getPublisher().getId()
                )).collect(Collectors.toList());
    }

    public ProductDtoResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("For id " + id));
        return mapFromProductToDto(product);
    }

    private ProductDtoResponse mapFromProductToDto(Product product) {
        ProductDtoResponse productDtoResponse = new ProductDtoResponse();
        productDtoResponse.setId(product.getId());
        productDtoResponse.setTitle(product.getTitle());
        productDtoResponse.setContent(product.getContent());
        productDtoResponse.setPublisherId(product.getPublisher().getId());
        return productDtoResponse;
    }

    private Product mapFromDtoToProduct(ProductDtoRequest productDtoRequest) {
        Product product = new Product();
        product.setTitle(productDtoRequest.getTitle());
        product.setContent(productDtoRequest.getContent());
        product.setUpdatedOn(Instant.now());
        product.setCategory(new Category(productDtoRequest.getCategoryId()));
        return product;
    }

    public void updateProduct(Long id, ProductDtoRequest productDtoRequest) {

        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        Product product = productRepository.findById(id).get();
        if (product.getPublisher().getEmail().equals(loggedInUser.getUsername())) {
            product.setTitle(productDtoRequest.getTitle());
            product.setContent(productDtoRequest.getContent());
            product.setUpdatedOn(Instant.now());
            product.setCategory(new Category(productDtoRequest.getCategoryId()));

            productRepository.save(product);
        } else {
            throw new IllegalArgumentException("User Mismatch");
        }

    }

    public List<ProductDtoResponse> getProductByUserId(Long id) {
        List<Product> productsByUserId = productRepository.findByPublisherId(id);
        return productsByUserId.stream().map(this::mapFromProductToDto).collect(Collectors.toList());
    }

}
