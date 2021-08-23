package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.ParametreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Parametre} and its DTO {@link ParametreDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ParametreMapper extends EntityMapper<ParametreDTO, Parametre> {
    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    ParametreDTO toDtoLibelle(Parametre parametre);
}
