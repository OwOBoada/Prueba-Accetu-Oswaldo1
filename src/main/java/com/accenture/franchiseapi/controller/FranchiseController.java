package com.accenture.franchiseapi.controller;

import com.accenture.franchiseapi.dto.request.CreateBranchRequest;
import com.accenture.franchiseapi.dto.request.CreateFranchiseRequest;
import com.accenture.franchiseapi.dto.request.UpdateNameRequest;
import com.accenture.franchiseapi.dto.response.BranchResponse;
import com.accenture.franchiseapi.dto.response.FranchiseResponse;
import com.accenture.franchiseapi.dto.response.TopStockProductByBranchResponse;
import com.accenture.franchiseapi.mapper.DomainMapper;
import com.accenture.franchiseapi.service.FranchiseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/franchises")
public class FranchiseController {

    private final FranchiseService franchiseService;
    private final DomainMapper mapper;

    public FranchiseController(FranchiseService franchiseService, DomainMapper mapper) {
        this.franchiseService = franchiseService;
        this.mapper = mapper;
    }

    @GetMapping("/{franchiseId}")
    public FranchiseResponse getFranchise(@PathVariable Long franchiseId) {
        return mapper.toResponse(franchiseService.getFranchiseOrThrow(franchiseId));
    }

    @GetMapping
    public List<FranchiseResponse> getAllFranchises() {
        return franchiseService.getAllFranchises().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @PostMapping("/{franchiseId}/branches")
    @ResponseStatus(HttpStatus.CREATED)
    public BranchResponse addBranch(
            @PathVariable Long franchiseId,
            @RequestBody @Valid CreateBranchRequest request
    ) {
        return mapper.toResponse(franchiseService.addBranch(franchiseId, request));
    }

    @GetMapping("/{franchiseId}/top-stock-products-by-branch")
    public List<TopStockProductByBranchResponse> getTopStockProductsByBranch(@PathVariable Long franchiseId) {
        return franchiseService.getTopStockProductsByBranch(franchiseId);
    }

    @PatchMapping("/{franchiseId}/name")
    public FranchiseResponse updateFranchiseName(
            @PathVariable Long franchiseId,
            @RequestBody @Valid UpdateNameRequest request
    ) {
        return mapper.toResponse(franchiseService.updateFranchiseName(franchiseId, request));
    }
}
