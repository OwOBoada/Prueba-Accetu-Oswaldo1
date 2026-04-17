package com.accenture.franchiseapi.repository;

import com.accenture.franchiseapi.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
