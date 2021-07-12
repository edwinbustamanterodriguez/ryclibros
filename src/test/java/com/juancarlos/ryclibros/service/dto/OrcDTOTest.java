package com.juancarlos.ryclibros.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.juancarlos.ryclibros.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrcDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrcDTO.class);
        OrcDTO orcDTO1 = new OrcDTO();
        orcDTO1.setId(1L);
        OrcDTO orcDTO2 = new OrcDTO();
        assertThat(orcDTO1).isNotEqualTo(orcDTO2);
        orcDTO2.setId(orcDTO1.getId());
        assertThat(orcDTO1).isEqualTo(orcDTO2);
        orcDTO2.setId(2L);
        assertThat(orcDTO1).isNotEqualTo(orcDTO2);
        orcDTO1.setId(null);
        assertThat(orcDTO1).isNotEqualTo(orcDTO2);
    }
}
