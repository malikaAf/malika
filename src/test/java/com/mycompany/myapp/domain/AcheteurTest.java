package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcheteurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Acheteur.class);
        Acheteur acheteur1 = new Acheteur();
        acheteur1.setId(1L);
        Acheteur acheteur2 = new Acheteur();
        acheteur2.setId(acheteur1.getId());
        assertThat(acheteur1).isEqualTo(acheteur2);
        acheteur2.setId(2L);
        assertThat(acheteur1).isNotEqualTo(acheteur2);
        acheteur1.setId(null);
        assertThat(acheteur1).isNotEqualTo(acheteur2);
    }
}
