package com.juancarlos.ryclibros.service.mapper;

import com.juancarlos.ryclibros.domain.*;
import com.juancarlos.ryclibros.service.dto.LibroDTO;
import com.juancarlos.ryclibros.service.dto.PersonaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Libro} and its DTO {@link LibroDTO}.
 */
@Mapper(componentModel = "spring", uses = { CategoriaMapper.class })
public interface LibroMapper extends EntityMapper<LibroDTO, Libro> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numero", source = "numero")
    @Mapping(target = "observaciones", source = "observaciones")
    @Mapping(target = "categoria", source = "categoria", qualifiedByName = "id")
    LibroDTO toDto(Libro s);
}
