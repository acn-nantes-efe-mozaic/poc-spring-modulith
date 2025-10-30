package com.accenture.modules.candidat.repository.mapper;


import com.accenture.modules.candidat.repository.entity.CandidatEntity;
import com.accenture.modules.candidat.service.domain.Candidat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidatEntityMapper {

    CandidatEntity toCandidatEntity(Candidat candidat);
    Candidat toCandidat(CandidatEntity candidatEntity);
}
