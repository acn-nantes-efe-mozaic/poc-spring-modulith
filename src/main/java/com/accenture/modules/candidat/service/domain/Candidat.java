package com.accenture.modules.candidat.service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Candidat {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private int nombreCandidatures;

    public Candidat(String firstName, String lastName, String email, int nombreCandidatures) {
        id = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.nombreCandidatures = nombreCandidatures;
    }


    public static Candidat of(String firstName, String lastName, String email) {
        return new Candidat(firstName, lastName, email, 0);
    }
}