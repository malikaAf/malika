package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.MarkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Mark} and its DTO {@link MarkDTO}.
 */
@Mapper(componentModel = "spring", uses = { CategoryMapper.class })
public interface MarkMapper extends EntityMapper<MarkDTO, Mark> {
    @Mapping(target = "category", source = "category")
    MarkDTO toDto(Mark s);

    @Named("libelle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    MarkDTO toDtoLibelle(Mark mark);
}
