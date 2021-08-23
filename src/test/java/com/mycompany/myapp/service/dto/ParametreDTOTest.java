package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParametreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParametreDTO.class);
        ParametreDTO parametreDTO1 = new ParametreDTO();
        parametreDTO1.setId(1L);
        ParametreDTO parametreDTO2 = new ParametreDTO();
        assertThat(parametreDTO1).isNotEqualTo(parametreDTO2);
        parametreDTO2.setId(parametreDTO1.getId());
        assertThat(parametreDTO1).isEqualTo(parametreDTO2);
        parametreDTO2.setId(2L);
        assertThat(parametreDTO1).isNotEqualTo(parametreDTO2);
        parametreDTO1.setId(null);
        assertThat(parametreDTO1).isNotEqualTo(parametreDTO2);
    }
}
