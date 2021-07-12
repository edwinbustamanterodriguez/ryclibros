package com.juancarlos.ryclibros.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.juancarlos.ryclibros.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrcTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Orc.class);
        Orc orc1 = new Orc();
        orc1.setId(1L);
        Orc orc2 = new Orc();
        orc2.setId(orc1.getId());
        assertThat(orc1).isEqualTo(orc2);
        orc2.setId(2L);
        assertThat(orc1).isNotEqualTo(orc2);
        orc1.setId(null);
        assertThat(orc1).isNotEqualTo(orc2);
    }
}
