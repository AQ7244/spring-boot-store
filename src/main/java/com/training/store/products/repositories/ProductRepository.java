package com.training.store.products.repositories;

import com.training.store.products.entities.Category;
import com.training.store.products.entities.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = "category")
    List<Product> findProductsByCategory(Category category);

    @EntityGraph(attributePaths = "category")
    @Query("SELECT p from Product p")
    List<Product> findAllWithCategory();

    @EntityGraph(attributePaths = "category")
    @Query("SELECT p FROM Product p WHERE p.id=:productId")
    Optional<Product> findProductWithCategory(@Param("productId") Long productId);
}