package com.juancarlos.ryclibros.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UbicacionMapperTest {

    private UbicacionMapper ubicacionMapper;

    @BeforeEach
    public void setUp() {
        ubicacionMapper = new UbicacionMapperImpl();
    }
}
