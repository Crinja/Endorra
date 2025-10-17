package com.techtech.endorra.stockmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techtech.endorra.stockmanagement.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> 
{
    
}
