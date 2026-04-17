package com.accenture.franchiseapi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateFranchiseRequest(
        @NotBlank(message = "El nombre de la franquicia es obligatorio")
        String name
) {
}
