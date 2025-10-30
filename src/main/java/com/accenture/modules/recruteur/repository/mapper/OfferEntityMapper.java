package com.accenture.modules.recruteur.repository.mapper;

import com.accenture.modules.recruteur.service.domain.Offer;
import com.accenture.modules.recruteur.repository.entity.OfferEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OfferEntityMapper {

    OfferEntity toOfferEntity(Offer offer);
    Offer toOffer(OfferEntity offerEntity);
}
