package com.accenture.modules.candidature.repository.mapper;


import com.accenture.modules.candidature.repository.CandidatureEntity;
import com.accenture.modules.candidature.service.domain.Candidature;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidatureEntityMapper {

    Candidature toCandidature(CandidatureEntity candidatureEntity);
    CandidatureEntity toCandidatureEntity(Candidature candidature);
}
