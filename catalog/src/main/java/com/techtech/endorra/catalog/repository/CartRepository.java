package com.techtech.endorra.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techtech.endorra.catalog.model.Cart;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> 
{
    List<Cart> findByUserEmail(String userEmail);
}
