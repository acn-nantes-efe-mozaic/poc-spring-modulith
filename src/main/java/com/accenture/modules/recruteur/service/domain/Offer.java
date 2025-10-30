package com.accenture.modules.recruteur.service.domain;

import java.util.Objects;
import java.util.UUID;

public class Offer {
    private final UUID id;
    private final String title;
    private final String description;
    private final int nbCandidatures;

    public Offer(UUID id, String title, String description, int nbCandidatures) {
        this.id = id;
        this.title = Objects.requireNonNull(title);
        this.description = Objects.requireNonNullElse(description, "");
        this.nbCandidatures = nbCandidatures;
    }


    public static Offer of(String title, String description) {
        return new Offer(null, title, description, 0);
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getNbCandidatures() {
        return nbCandidatures;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", nbCandidatures=" + nbCandidatures +
                '}';
    }
}