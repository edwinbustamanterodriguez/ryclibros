package com.juancarlos.ryclibros.service.mapper;

import com.juancarlos.ryclibros.domain.*;
import com.juancarlos.ryclibros.service.dto.PersonaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Persona} and its DTO {@link PersonaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PersonaMapper extends EntityMapper<PersonaDTO, Persona> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "apaterno", source = "apaterno")
    @Mapping(target = "amaterno", source = "amaterno")
    PersonaDTO toDtoId(Persona persona);
}
