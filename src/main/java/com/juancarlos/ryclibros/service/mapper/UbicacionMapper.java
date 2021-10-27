package com.juancarlos.ryclibros.service.mapper;

import com.juancarlos.ryclibros.domain.*;
import com.juancarlos.ryclibros.service.dto.UbicacionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ubicacion} and its DTO {@link UbicacionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UbicacionMapper extends EntityMapper<UbicacionDTO, Ubicacion> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "sector", source = "sector")
    @Mapping(target = "numero", source = "numero")
    @Mapping(target = "serie", source = "serie")
    UbicacionDTO toDtoId(Ubicacion ubicacion);
}
