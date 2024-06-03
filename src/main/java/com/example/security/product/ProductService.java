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

    private List<Product> getProductsByUserId(Long id){
        return productRepository.findByPublisherId(id);
    }

    public List<ProductDtoResponseIOwn> getProductsThatIOwn() {
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();
        Long userId = user.getId();

        List<Product> productsByUserId = getProductsByUserId(userId);

        return productsByUserId.stream()
                .map(product -> ProductDtoResponseIOwn.builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .content(product.getContent())
                        .price(product.getPrice())
                        .imageUrl(product.getImageUrl())
                        .categoryId(product.getCategory().getId())
                        .createdOn(product.getCreatedOn().toString())
                        .updatedOn(product.getUpdatedOn() != null ? product.getUpdatedOn().toString() : null)
                        .build()
                )
                .collect(Collectors.toList());
    }

    public void deleteProduct(Long id) {
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("For id " + id));
        if (product.getPublisher().getEmail().equals(loggedInUser.getUsername())) {

            Set<User> likedUsers = product.getLikedUsers();
            likedUsers.stream()
                    .forEach(user -> unAssignLikedProductFromUser(user, product));
            productRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("User Mismatch");
        }
    }

    private void unAssignLikedProductFromUser(User user, Product product) {
        List<Product> productList = null;
        productList = user.getLikedProducts();
        productList.removeIf(item -> item.equals(product));
        user.setLikedProducts(productList);
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

        List<Product> productList = null;
        productList = user.getLikedProducts();
        productList.add(product);
        user.setLikedProducts(productList);
        userService.save(user);

    }

    public void deleteProductFromLiked(Long id) {
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();

        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("For id " + id));
        unAssignLikedProductFromUser(user, product);
    }


    public List<ProductDtoResponseWithUserData> getLikedProducts() {
        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();
        return user.getLikedProducts().stream()
                .map(product -> new ProductDtoResponseWithUserData(
                        product.getId(),
                        product.getTitle(),
                        product.getContent(),
                        product.getPublisher().getId(),
                        product.getPublisher().getFirstname() + " " + product.getPublisher().getLastname(),
                        product.getPublisher().getImageUrl(),
                        product.getCreatedOn().toString(),
                        product.getUpdatedOn() != null ? product.getUpdatedOn().toString() : null,
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
    public List<ProductDtoLiked> allProductsWithFlagLikedOnes() {//TODO:: Need to be refactored

        List<ProductDtoResponseWithUserData> likedProducts = getLikedProducts();
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
                            publisher.getImageUrl(),
                            product.getCreatedOn().toString(),
                            product.getUpdatedOn() != null ? product.getUpdatedOn().toString() : null,
                            product.getPrice(),
                            product.getImageUrl()
                    );

                    boolean isLiked = likedProducts.stream().anyMatch(likedProduct -> likedProduct.getId() == product.getId());
                    productDtoLiked.setLiked(isLiked);
                    productsWithLikedFlag.add(productDtoLiked);
                });
        return productsWithLikedFlag;
    }

    public void reportProduct(Long id) {

        UserDetails loggedInUser = authenticationService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        User user = userService.findByEmail(loggedInUser.getUsername()).get();

        Product product = productRepository.findById(id).get();
        Set<User> reporters = product.getProduct_reports();
        if (!reporters.contains(user)) {
            reporters.add(user);
            product.setProduct_reports(reporters);
            product.setNumberOfReports(product.getNumberOfReports() + 1);
            productRepository.save(product);
        }
    }

    public void deleteAllLikesThatIMadeForAllProducts(User user) {
        List<Product> likedProducts = user.getLikedProducts();
        likedProducts.forEach(product -> {
            Set<User> likedUsers = product.getLikedUsers();
            likedUsers.remove(user);
            productRepository.save(product);
        });
    }

    public void deleteReportsByUser(User user) {
        Set<Product> ReportedProducts = user.getProductsToReport();
        ReportedProducts.stream().forEach(product -> {
            product.setProduct_reports(null);
            productRepository.save(product);
        });
        user.setProductsToReport(null);
        userService.save(user);
    }

    public void deleteAllLikesForMyProducts(User user) {
        List<Product> publishedProducts = user.getPublishedProducts();
        publishedProducts.stream().forEach(product -> {

            Set<User> likedUsers = product.getLikedUsers();
            likedUsers.stream().forEach(user1 -> {
                List<Product> likedProducts = user1.getLikedProducts();
                likedProducts.remove(product);
                user1.setLikedProducts(likedProducts);
                userService.save(user1);
            });

            product.setLikedUsers(null);
            productRepository.save(product);

        });
        userService.save(user);
    }

    public List<ReportedProductDto> getReportedProducts() {
        return productRepository.findAllReportedProductsOrderByNumberOfReports().stream().map(product -> {
            return new ReportedProductDto(
                    product.getId(),
                    product.getTitle(),
                    product.getContent(),
                    product.getPublisher().getId(),
                    product.getPublisher().getFirstname(),
                    product.getPublisher().getImageUrl(),
                    product.getCreatedOn().toString(),
                    product.getPrice(),
                    product.getImageUrl(),
                    product.getNumberOfReports()
            );
        }).collect(Collectors.toList());
    }

}
