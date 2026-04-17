package com.accenture.franchiseapi.controller;

import com.accenture.franchiseapi.dto.request.UpdateNameRequest;
import com.accenture.franchiseapi.dto.response.ProductResponse;
import com.accenture.franchiseapi.mapper.DomainMapper;
import com.accenture.franchiseapi.service.FranchiseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final FranchiseService franchiseService;
    private final DomainMapper mapper;

    public ProductController(FranchiseService franchiseService, DomainMapper mapper) {
        this.franchiseService = franchiseService;
        this.mapper = mapper;
    }

    @PatchMapping("/{productId}/name")
    public ProductResponse updateProductName(
            @PathVariable Long productId,
            @RequestBody @Valid UpdateNameRequest request
    ) {
        return mapper.toResponse(franchiseService.updateProductName(productId, request));
    }
}
