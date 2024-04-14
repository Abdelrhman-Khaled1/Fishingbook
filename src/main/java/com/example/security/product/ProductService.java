package com.example.security.product;

import com.example.security.auth.AuthenticationService;
import com.example.security.user.User;
import com.example.security.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ProductRepository productRepository;

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

        product.setPrice(productDtoRequest.getPrice());
        product.setImageUrl(productDtoRequest.getImageUrl());
//        Product product = mapFromDtoToProduct(productDtoRequest);

        productRepository.save(product);
    }

    public List<ProductDtoResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapFromProductToDto).collect(Collectors.toList());
    }

    public List<ProductDtoResponse> getProductsByCategoryId(Long id) {
        List<Product> productsByCategory_id = productRepository.findByCategory_Id(id);
        return productsByCategory_id.stream()
                .map(product -> new ProductDtoResponse(
                        product.getId(),
                        product.getTitle(),
                        product.getContent(),
                        product.getPublisher().getId(),
                        product.getPrice(),
                        product.getImageUrl()
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
        productDtoResponse.setPrice(product.getPrice());
        productDtoResponse.setImageUrl(product.getImageUrl());
        return productDtoResponse;
    }

    private Product mapFromDtoToProduct(ProductDtoRequest productDtoRequest) {
        Product product = new Product();
        product.setTitle(productDtoRequest.getTitle());
        product.setContent(productDtoRequest.getContent());
        product.setUpdatedOn(Instant.now());
        product.setCategory(new Category(productDtoRequest.getCategoryId()));
        product.setPrice(productDtoRequest.getPrice());
        product.setImageUrl(productDtoRequest.getImageUrl());
        return product;
    }

    public void updateProduct(ProductDtoUpdate productDtoUpdate) {

        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        Long productId = productDtoUpdate.getId();
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("For id " + productId));
        if (product.getPublisher().getEmail().equals(loggedInUser.getUsername())) {
            product.setTitle(productDtoUpdate.getTitle());
            product.setContent(productDtoUpdate.getContent());
            product.setUpdatedOn(Instant.now());
            product.setCategory(new Category(productDtoUpdate.getCategoryId()));
            product.setPrice(productDtoUpdate.getPrice());
            product.setImageUrl(productDtoUpdate.getImageUrl());

            productRepository.save(product);
        } else {
            throw new IllegalArgumentException("User Mismatch");
        }

    }

    public List<ProductDtoResponse> getProductByUserId(Long id) {
        List<Product> productsByUserId = productRepository.findByPublisherId(id);
        return productsByUserId.stream().map(this::mapFromProductToDto).collect(Collectors.toList());
    }

    public List<ProductDtoResponse> getProductsThatIOwn() {
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();
        Long userId = user.getId();
        return getProductByUserId(userId);
    }

    public void deleteProduct(Long id) {
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("For id " + id));
        if (product.getPublisher().getEmail().equals(loggedInUser.getUsername())) {

            Set<User> likedEmployees = product.getLikedEmployees();
            likedEmployees.stream()
                    .forEach(user -> unAssignLikedProductFromUser(user, product));
            productRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("User Mismatch");
        }
    }

    private void unAssignLikedProductFromUser(User user, Product product) {
        Set<Product> productSet = null;
        productSet = user.getLikedProjects();
        productSet.removeIf(item -> item.equals(product));
        user.setLikedProjects(productSet);
        userService.save(user);
    }


    public List<ProductDtoResponse> getProductsByTitleContains(String title) {
        List<Product> ProductsByTitleContains = productRepository.findByTitleContaining(title);
        return ProductsByTitleContains.stream()
                .map(product -> new ProductDtoResponse(
                        product.getId(),
                        product.getTitle(),
                        product.getContent(),
                        product.getPublisher().getId(),
                        product.getPrice(),
                        product.getImageUrl()
                )).collect(Collectors.toList());
    }

    public void addProductToLiked(Long id) {
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("For id " + id));

        Set<Product> productSet = null;
        productSet = user.getLikedProjects();
        productSet.add(product);
        user.setLikedProjects(productSet);
        userService.save(user);

    }

    public void deleteProductFromLiked(Long id) {
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();

        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("For id " + id));
        unAssignLikedProductFromUser(user, product);
    }


    public List<ProductDtoResponse> getLikedProducts() {
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();
        return user.getLikedProjects().stream()
                .map(product -> new ProductDtoResponse(
                        product.getId(),
                        product.getTitle(),
                        product.getContent(),
                        user.getId(),
                        product.getPrice(),
                        product.getImageUrl()
                )).collect(Collectors.toList());
    }

    //    public List<ProductDtoLiked> allProductsWithFlagLikedOnes() {
//        List<ProductDtoResponse> likedProducts = getLikedProducts();
//        List<ProductDtoResponse> allProducts = getAllProducts();
//        List<ProductDtoLiked> productsWithLikedFlag = new ArrayList<>();
//
//        allProducts.stream()
//                .forEach(productDtoResponse -> {
//                    if (likedProducts.contains(productDtoResponse)) {
//                        productsWithLikedFlag.add(
//                                new ProductDtoLiked(
//                                        productDtoResponse.getId(),
//                                        productDtoResponse.getTitle(),
//                                        productDtoResponse.getContent(),
//                                        productDtoResponse.getPublisherId(),
//                                        productDtoResponse.getPrice(),
//                                        productDtoResponse.getImageUrl(),
//                                        true
//                                ));
//                    } else {
//                        productsWithLikedFlag.add(
//                                new ProductDtoLiked(
//                                        productDtoResponse.getId(),
//                                        productDtoResponse.getTitle(),
//                                        productDtoResponse.getContent(),
//                                        productDtoResponse.getPublisherId(),
//                                        productDtoResponse.getPrice(),
//                                        productDtoResponse.getImageUrl(),
//                                        false
//                                ));
//                    }
//                });
//
//        return productsWithLikedFlag;
//    }
    public List<ProductDtoLiked> allProductsWithFlagLikedOnes() {

        List<ProductDtoResponse> likedProducts = getLikedProducts();
        List<Product> allProducts = productRepository.findAll();

        List<ProductDtoLiked> productsWithLikedFlag = new ArrayList<>();

        allProducts.stream()
                .forEach(product -> {
                    User publisher = product.getPublisher();
                    ProductDtoLiked productDtoLiked = new ProductDtoLiked(
                            product.getId(),
                            product.getTitle(),
                            product.getContent(),
                            product.getCategory().getId(),
                            publisher.getId(),
                            publisher.getFirstname() + " " + publisher.getLastname(),
                            null,
                            product.getCreatedOn(),
                            product.getUpdatedOn(),
                            product.getPrice(),
                            product.getImageUrl()
                    );

                    boolean isLiked = likedProducts.stream().anyMatch(likedProduct -> likedProduct.getId() == product.getId());
                    productDtoLiked.setLiked(isLiked);
                    productsWithLikedFlag.add(productDtoLiked);
                });
        return productsWithLikedFlag;
    }

}
