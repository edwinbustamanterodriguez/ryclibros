package com.juancarlos.ryclibros.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrcMapperTest {

    private OrcMapper orcMapper;

    @BeforeEach
    public void setUp() {
        orcMapper = new OrcMapperImpl();
    }
}
