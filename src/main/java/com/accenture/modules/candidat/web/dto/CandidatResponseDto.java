package com.accenture.modules.candidat.web.dto;

import java.util.UUID;

public record CandidatResponseDto(UUID id, String firstName, String lastName, String email, int nombreCandidatures) {
}
