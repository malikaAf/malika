package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntreeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntreeDTO.class);
        EntreeDTO entreeDTO1 = new EntreeDTO();
        entreeDTO1.setId(1L);
        EntreeDTO entreeDTO2 = new EntreeDTO();
        assertThat(entreeDTO1).isNotEqualTo(entreeDTO2);
        entreeDTO2.setId(entreeDTO1.getId());
        assertThat(entreeDTO1).isEqualTo(entreeDTO2);
        entreeDTO2.setId(2L);
        assertThat(entreeDTO1).isNotEqualTo(entreeDTO2);
        entreeDTO1.setId(null);
        assertThat(entreeDTO1).isNotEqualTo(entreeDTO2);
    }
}
