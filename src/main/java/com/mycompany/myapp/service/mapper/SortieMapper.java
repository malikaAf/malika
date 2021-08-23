package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.SortieDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sortie} and its DTO {@link SortieDTO}.
 */
@Mapper(componentModel = "spring", uses = { EntreeMapper.class, AcheteurMapper.class })
public interface SortieMapper extends EntityMapper<SortieDTO, Sortie> {
    @Mapping(target = "entree", source = "entree")
    @Mapping(target = "acheteur", source = "acheteur")
    SortieDTO toDto(Sortie s);
}
