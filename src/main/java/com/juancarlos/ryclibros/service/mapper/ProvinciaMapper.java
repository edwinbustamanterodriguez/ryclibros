package com.juancarlos.ryclibros.service.mapper;

import com.juancarlos.ryclibros.domain.*;
import com.juancarlos.ryclibros.service.dto.ProvinciaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Provincia} and its DTO {@link ProvinciaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProvinciaMapper extends EntityMapper<ProvinciaDTO, Provincia> {}
