package com.accenture.modules.shared.events;

import java.util.UUID;

/**
 * Événement émis lorsqu'un candidat est créé.
 * @param id L'identifiant du candidat
 * @param email L'email du candidat
 * @param firstName Le prénom du candidat
 * @param lastName Le nom de famille du candidat
 */
public record CandidatCreeEvent(
    UUID id,
    String email,
    String firstName,
    String lastName
) {}
