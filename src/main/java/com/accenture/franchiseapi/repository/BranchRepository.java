package com.accenture.franchiseapi.repository;

import com.accenture.franchiseapi.domain.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
}
