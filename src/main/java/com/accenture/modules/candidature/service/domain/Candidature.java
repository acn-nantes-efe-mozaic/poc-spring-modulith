package com.accenture.modules.candidature.service.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain model representing a candidature (application) linking a candidate and an offer.
 * This model only stores identifiers to avoid cross-module type coupling.
 */
public class Candidature {
    private final UUID id;
    private final UUID candidateId;
    private final UUID offerId;
    private final Instant createdAt;

    public Candidature(UUID id, UUID candidateId, UUID offerId, Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.candidateId = Objects.requireNonNull(candidateId);
        this.offerId = Objects.requireNonNull(offerId);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static Candidature of(UUID candidateId, UUID offerId) {
        return new Candidature(UUID.randomUUID(), candidateId, offerId, Instant.now());
    }

    public UUID getId() {
        return id;
    }

    public UUID getCandidateId() {
        return candidateId;
    }

    public UUID getOfferId() {
        return offerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Candidature{" +
                "id=" + id +
                ", candidateId=" + candidateId +
                ", offerId=" + offerId +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Candidature that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
