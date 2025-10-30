package com.accenture.modules.recruteur.web.mapper;

import com.accenture.modules.recruteur.service.domain.Offer;
import com.accenture.modules.recruteur.web.dto.OfferRequestDto;
import com.accenture.modules.recruteur.web.dto.OfferResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OfferDtoMapper {

    Offer toOffer(OfferRequestDto offerRequestDto);
    OfferResponseDto toOfferResponseDto(Offer offer);
}
