package com.juancarlos.ryclibros.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.juancarlos.ryclibros.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrestamoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrestamoDTO.class);
        PrestamoDTO prestamoDTO1 = new PrestamoDTO();
        prestamoDTO1.setId(1L);
        PrestamoDTO prestamoDTO2 = new PrestamoDTO();
        assertThat(prestamoDTO1).isNotEqualTo(prestamoDTO2);
        prestamoDTO2.setId(prestamoDTO1.getId());
        assertThat(prestamoDTO1).isEqualTo(prestamoDTO2);
        prestamoDTO2.setId(2L);
        assertThat(prestamoDTO1).isNotEqualTo(prestamoDTO2);
        prestamoDTO1.setId(null);
        assertThat(prestamoDTO1).isNotEqualTo(prestamoDTO2);
    }
}
