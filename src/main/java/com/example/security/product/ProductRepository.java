package com.example.security.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByPublisherId(Long id);

    List<Product> findByCategory_Id(Long categoryId);

    List<Product> findByTitleContaining(String title);

}
