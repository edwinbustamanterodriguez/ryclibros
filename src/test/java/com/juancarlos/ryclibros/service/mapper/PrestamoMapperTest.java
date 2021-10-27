package com.juancarlos.ryclibros.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrestamoMapperTest {

    private PrestamoMapper prestamoMapper;

    @BeforeEach
    public void setUp() {
        prestamoMapper = new PrestamoMapperImpl();
    }
}
