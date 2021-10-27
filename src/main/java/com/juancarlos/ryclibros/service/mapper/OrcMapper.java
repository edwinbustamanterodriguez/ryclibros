package com.juancarlos.ryclibros.service.mapper;

import com.juancarlos.ryclibros.domain.*;
import com.juancarlos.ryclibros.service.dto.LibroDTO;
import com.juancarlos.ryclibros.service.dto.OrcDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Orc} and its DTO {@link OrcDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrcMapper extends EntityMapper<OrcDTO, Orc> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numero", source = "numero")
    @Mapping(target = "personas", source = "personas")
    OrcDTO toDto(Orc o);
}
