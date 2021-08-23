package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntreeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entree.class);
        Entree entree1 = new Entree();
        entree1.setId(1L);
        Entree entree2 = new Entree();
        entree2.setId(entree1.getId());
        assertThat(entree1).isEqualTo(entree2);
        entree2.setId(2L);
        assertThat(entree1).isNotEqualTo(entree2);
        entree1.setId(null);
        assertThat(entree1).isNotEqualTo(entree2);
    }
}
