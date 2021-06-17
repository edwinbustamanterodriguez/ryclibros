package com.juancarlos.ryclibros.service.mapper;

import com.juancarlos.ryclibros.domain.*;
import com.juancarlos.ryclibros.service.dto.LibroDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Libro} and its DTO {@link LibroDTO}.
 */
@Mapper(componentModel = "spring", uses = { CategoriaMapper.class })
public interface LibroMapper extends EntityMapper<LibroDTO, Libro> {
    @Mapping(target = "categoria", source = "categoria", qualifiedByName = "id")
    LibroDTO toDto(Libro s);
}
