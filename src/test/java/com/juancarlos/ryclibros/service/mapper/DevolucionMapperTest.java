package com.juancarlos.ryclibros.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DevolucionMapperTest {

    private DevolucionMapper devolucionMapper;

    @BeforeEach
    public void setUp() {
        devolucionMapper = new DevolucionMapperImpl();
    }
}
