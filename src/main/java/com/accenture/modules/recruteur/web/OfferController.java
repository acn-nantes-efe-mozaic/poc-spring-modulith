package com.accenture.modules.recruteur.web;

import com.accenture.modules.recruteur.service.domain.Offer;
import com.accenture.modules.recruteur.service.usecase.OfferServiceUseCase;
import com.accenture.modules.recruteur.web.dto.OfferRequestDto;
import com.accenture.modules.recruteur.web.dto.OfferResponseDto;
import com.accenture.modules.recruteur.web.mapper.OfferDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recruteur/offers")
public class OfferController {

    private final OfferServiceUseCase offerService;
    private final OfferDtoMapper offerDtoMapper;

    public OfferController(OfferServiceUseCase offerService, OfferDtoMapper offerDtoMapper) {
        this.offerService = offerService;
        this.offerDtoMapper = offerDtoMapper;
    }

    @PostMapping
    public ResponseEntity<OfferResponseDto> create(@RequestBody OfferRequestDto request) {
        Offer created = offerService.createOffer(request.title(), request.description());
        return ResponseEntity.created(URI.create("/api/recruteur/offers/" + created.getId()))
                .body(offerDtoMapper.toOfferResponseDto(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferResponseDto> getById(@PathVariable("id") UUID id) {
        return offerService.getOfferById(id)
                .map(offer -> ResponseEntity.ok(offerDtoMapper.toOfferResponseDto(offer)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<OfferResponseDto> list() {
        return offerService.getAllOffers().stream()
                .map(offerDtoMapper::toOfferResponseDto)
                .toList();
    }
}
