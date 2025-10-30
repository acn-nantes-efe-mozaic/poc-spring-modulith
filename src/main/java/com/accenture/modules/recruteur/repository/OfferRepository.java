package com.accenture.modules.recruteur.repository;

import com.accenture.modules.recruteur.repository.entity.OfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OfferRepository extends JpaRepository<OfferEntity, UUID> {
}
