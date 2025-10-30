package com.accenture.modules.candidat.repository;

import com.accenture.modules.candidat.repository.entity.CandidatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CandidatRepository extends JpaRepository<CandidatEntity, UUID> {
}
