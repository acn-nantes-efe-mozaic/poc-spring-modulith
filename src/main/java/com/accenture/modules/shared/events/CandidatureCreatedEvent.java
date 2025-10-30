package com.accenture.modules.shared.events;

import java.time.Instant;
import java.util.UUID;


public record CandidatureCreatedEvent(UUID id, UUID candidateId, UUID offerId, Instant createdAt) {}
