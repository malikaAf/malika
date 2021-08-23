package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.ExerciceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Exercice} and its DTO {@link ExerciceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExerciceMapper extends EntityMapper<ExerciceDTO, Exercice> {
    @Named("annee")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "annee", source = "annee")
    ExerciceDTO toDtoAnnee(Exercice exercice);
}
