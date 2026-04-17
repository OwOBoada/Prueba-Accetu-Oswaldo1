package com.accenture.franchiseapi.controller;

import com.accenture.franchiseapi.dto.request.CreateProductRequest;
import com.accenture.franchiseapi.dto.request.UpdateNameRequest;
import com.accenture.franchiseapi.dto.request.UpdateStockRequest;
import com.accenture.franchiseapi.dto.response.BranchResponse;
import com.accenture.franchiseapi.dto.response.ProductResponse;
import com.accenture.franchiseapi.mapper.DomainMapper;
import com.accenture.franchiseapi.service.FranchiseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    private final FranchiseService franchiseService;
    private final DomainMapper mapper;

    public BranchController(FranchiseService franchiseService, DomainMapper mapper) {
        this.franchiseService = franchiseService;
        this.mapper = mapper;
    }

    @PostMapping("/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse addProduct(
            @PathVariable Long branchId,
            @RequestBody @Valid CreateProductRequest request
    ) {
        return mapper.toResponse(franchiseService.addProduct(branchId, request));
    }

    @DeleteMapping("/{branchId}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductFromBranch(@PathVariable Long branchId, @PathVariable Long productId) {
        franchiseService.deleteProductFromBranch(branchId, productId);
    }

    @PatchMapping("/{branchId}/products/{productId}/stock")
    public ProductResponse updateProductStock(
            @PathVariable Long branchId,
            @PathVariable Long productId,
            @RequestBody @Valid UpdateStockRequest request
    ) {
        return mapper.toResponse(franchiseService.updateProductStock(branchId, productId, request));
    }

    @PatchMapping("/{branchId}/name")
    public BranchResponse updateBranchName(
            @PathVariable Long branchId,
            @RequestBody @Valid UpdateNameRequest request
    ) {
        return mapper.toResponse(franchiseService.updateBranchName(branchId, request));
    }
}
