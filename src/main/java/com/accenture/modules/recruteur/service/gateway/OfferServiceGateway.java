package com.accenture.modules.recruteur.service.gateway;

import com.accenture.modules.recruteur.service.domain.Offer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfferServiceGateway {
    boolean isOffreExiste(UUID offreId);
    
    /**
     * Récupère le nom d'une offre
     * @param offerId L'identifiant de l'offre
     * @return Le nom de l'offre
     * @throws IllegalArgumentException si l'offre n'existe pas
     */
    String getNomOffre(UUID offerId);
}
