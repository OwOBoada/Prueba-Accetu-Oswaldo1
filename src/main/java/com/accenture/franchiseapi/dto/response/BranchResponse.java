package com.accenture.franchiseapi.dto.response;

public record BranchResponse(
        Long id,
        String name,
        Long franchiseId
) {
}
