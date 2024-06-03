package com.example.security.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByPublisherId(Long id);

    List<Product> findByCategory_Id(Long categoryId);

    List<Product> findByTitleContaining(String title);

    @Query("SELECT p FROM Product p WHERE p.numberOfReports > 0 ORDER BY p.numberOfReports DESC")
    List<Product> findAllReportedProductsOrderByNumberOfReports();

}
