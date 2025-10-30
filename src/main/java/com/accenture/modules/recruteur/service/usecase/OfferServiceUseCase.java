package com.accenture.modules.recruteur.service.usecase;

import com.accenture.modules.recruteur.service.domain.Offer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfferServiceUseCase {
    Offer createOffer(String title, String description);

    Optional<Offer> getOfferById(UUID id);

    List<Offer> getAllOffers();
}
