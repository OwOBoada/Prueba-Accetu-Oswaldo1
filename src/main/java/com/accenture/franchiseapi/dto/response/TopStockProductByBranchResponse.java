package com.accenture.franchiseapi.dto.response;

public record TopStockProductByBranchResponse(
        Long branchId,
        String branchName,
        Long productId,
        String productName,
        Integer stock
) {
}
