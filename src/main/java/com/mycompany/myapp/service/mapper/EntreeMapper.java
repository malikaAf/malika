package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.EntreeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Entree} and its DTO {@link EntreeDTO}.
 */
@Mapper(componentModel = "spring", uses = { ArticleMapper.class, FournisseurMapper.class, ExerciceMapper.class, ParametreMapper.class })
public interface EntreeMapper extends EntityMapper<EntreeDTO, Entree> {
    @Mapping(target = "article", source = "article")
    @Mapping(target = "fournisseur", source = "fournisseur")
    @Mapping(target = "exercice", source = "exercice")
    @Mapping(target = "parametre", source = "parametre")
    EntreeDTO toDto(Entree s);

    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    EntreeDTO toDtoLibelle(Entree entree);
}
