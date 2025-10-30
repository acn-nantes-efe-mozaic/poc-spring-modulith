package com.accenture.modules.candidature.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CandidatureRepository extends JpaRepository<CandidatureEntity, UUID> {

    List<CandidatureEntity> findByCandidateId(UUID candidateId);
}
