package com.juancarlos.ryclibros.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.juancarlos.ryclibros.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocalidadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocalidadDTO.class);
        LocalidadDTO localidadDTO1 = new LocalidadDTO();
        localidadDTO1.setId(1L);
        LocalidadDTO localidadDTO2 = new LocalidadDTO();
        assertThat(localidadDTO1).isNotEqualTo(localidadDTO2);
        localidadDTO2.setId(localidadDTO1.getId());
        assertThat(localidadDTO1).isEqualTo(localidadDTO2);
        localidadDTO2.setId(2L);
        assertThat(localidadDTO1).isNotEqualTo(localidadDTO2);
        localidadDTO1.setId(null);
        assertThat(localidadDTO1).isNotEqualTo(localidadDTO2);
    }
}
