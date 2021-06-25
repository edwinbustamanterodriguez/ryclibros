package com.juancarlos.ryclibros.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.juancarlos.ryclibros.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DevolucionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Devolucion.class);
        Devolucion devolucion1 = new Devolucion();
        devolucion1.setId(1L);
        Devolucion devolucion2 = new Devolucion();
        devolucion2.setId(devolucion1.getId());
        assertThat(devolucion1).isEqualTo(devolucion2);
        devolucion2.setId(2L);
        assertThat(devolucion1).isNotEqualTo(devolucion2);
        devolucion1.setId(null);
        assertThat(devolucion1).isNotEqualTo(devolucion2);
    }
}
