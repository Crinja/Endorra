package com.techtech.endorra.stockmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techtech.endorra.stockmanagement.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> 
{
    Optional<Product> findByNameIgnoreCase(@Param("name") String name);

    @Query("""
        SELECT DISTINCT p 
        FROM Product p 
        JOIN p.tags t 
        WHERE LOWER(t) IN :tags
    """)
    List<Product> findByAnyTags(@Param("tags") List<String> tags);
}
