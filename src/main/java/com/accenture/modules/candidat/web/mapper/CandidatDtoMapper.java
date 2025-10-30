package com.accenture.modules.candidat.web.mapper;


import com.accenture.modules.candidat.service.domain.Candidat;
import com.accenture.modules.candidat.web.dto.CandidatRequestDto;
import com.accenture.modules.candidat.web.dto.CandidatResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidatDtoMapper {

    Candidat toCandidat(CandidatRequestDto  candidatRequestDto);
    CandidatResponseDto toCandidatResponseDto(Candidat candidat);
}
