package com.accenture.modules.recruteur.web.dto;

import java.util.UUID;

public record OfferResponseDto(UUID id, String title, String description, int nbCandidatures) {
}
