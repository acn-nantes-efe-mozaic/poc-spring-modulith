package com.accenture.modules.recruteur.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "offers")
public class OfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String title;
    private String description;
    private int nbCandidatures;

    public OfferEntity() {
    }

    public OfferEntity(OfferEntity other) {
        if (other != null) {
            this.id = other.id;
            this.title = other.title;
            this.description = other.description;
            this.nbCandidatures = other.nbCandidatures;
        }
    }

    public OfferEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public OfferEntity(UUID id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNbCandidatures() {
        return nbCandidatures;
    }

    public void setNbCandidatures(int nbCandidatures) {
        this.nbCandidatures = nbCandidatures;
    }
}
