package com.juancarlos.ryclibros.service.mapper;

import com.juancarlos.ryclibros.domain.*;
import com.juancarlos.ryclibros.service.dto.PrestamoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Prestamo} and its DTO {@link PrestamoDTO}.
 */
@Mapper(componentModel = "spring", uses = { LibroMapper.class, PersonaMapper.class, UserMapper.class })
public interface PrestamoMapper extends EntityMapper<PrestamoDTO, Prestamo> {
    @Mapping(target = "libro", source = "libro", qualifiedByName = "id")
    @Mapping(target = "persona", source = "persona", qualifiedByName = "id")
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    PrestamoDTO toDto(Prestamo s);
}
