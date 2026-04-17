package com.accenture.franchiseapi.repository;

import com.accenture.franchiseapi.domain.entity.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FranchiseRepository extends JpaRepository<Franchise, Long> {

    @Query("""
            select distinct f
            from Franchise f
            left join fetch f.branches b
            left join fetch b.products
            where f.id = :id
            """)
    Optional<Franchise> findByIdWithBranchesAndProducts(Long id);
}
