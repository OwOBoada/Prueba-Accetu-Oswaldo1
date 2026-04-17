package com.accenture.franchiseapi.mapper;

import com.accenture.franchiseapi.domain.entity.Branch;
import com.accenture.franchiseapi.domain.entity.Franchise;
import com.accenture.franchiseapi.domain.entity.Product;
import com.accenture.franchiseapi.dto.response.BranchResponse;
import com.accenture.franchiseapi.dto.response.FranchiseResponse;
import com.accenture.franchiseapi.dto.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class DomainMapper {

    public FranchiseResponse toResponse(Franchise franchise) {
        return new FranchiseResponse(franchise.getId(), franchise.getName());
    }

    public BranchResponse toResponse(Branch branch) {
        return new BranchResponse(branch.getId(), branch.getName(), branch.getFranchise().getId());
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getStock(), product.getBranch().getId());
    }
}
