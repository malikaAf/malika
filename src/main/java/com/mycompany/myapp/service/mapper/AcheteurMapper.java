package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.AcheteurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Acheteur} and its DTO {@link AcheteurDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AcheteurMapper extends EntityMapper<AcheteurDTO, Acheteur> {
    @Named("nom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    AcheteurDTO toDtoNom(Acheteur acheteur);
}
