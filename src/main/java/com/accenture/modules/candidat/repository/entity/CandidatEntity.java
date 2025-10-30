package com.accenture.modules.candidat.repository.entity;

import com.accenture.modules.candidat.service.domain.Candidat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "candidats")
@NoArgsConstructor
@Data
@ToString
public class CandidatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private  int nombreCandidatures;

}
