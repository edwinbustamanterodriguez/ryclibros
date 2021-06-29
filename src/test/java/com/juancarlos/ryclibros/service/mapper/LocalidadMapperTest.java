package com.juancarlos.ryclibros.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalidadMapperTest {

    private LocalidadMapper localidadMapper;

    @BeforeEach
    public void setUp() {
        localidadMapper = new LocalidadMapperImpl();
    }
}
