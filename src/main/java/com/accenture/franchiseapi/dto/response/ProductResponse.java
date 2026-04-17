package com.accenture.franchiseapi.dto.response;

public record ProductResponse(
        Long id,
        String name,
        Integer stock,
        Long branchId
) {
}
