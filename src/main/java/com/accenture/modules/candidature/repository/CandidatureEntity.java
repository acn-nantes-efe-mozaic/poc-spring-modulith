package com.accenture.modules.candidature.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity representing a candidature (application) linking a candidate and an offer.
 * Mirrors the domain model com.accenture.modules.candidature.service.domain.Candidature.
 */
@Entity
@Table(name = "candidatures")
public class CandidatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID candidateId;

    private UUID offerId;

    private Instant createdAt;

    public CandidatureEntity() {
    }

    public CandidatureEntity(UUID id, UUID candidateId, UUID offerId, Instant createdAt) {
        this.id = id;
        this.candidateId = candidateId;
        this.offerId = offerId;
        this.createdAt = createdAt;
    }

    public CandidatureEntity(UUID candidateId, UUID offerId, Instant createdAt) {
        this.candidateId = candidateId;
        this.offerId = offerId;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(UUID candidateId) {
        this.candidateId = candidateId;
    }

    public UUID getOfferId() {
        return offerId;
    }

    public void setOfferId(UUID offerId) {
        this.offerId = offerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
