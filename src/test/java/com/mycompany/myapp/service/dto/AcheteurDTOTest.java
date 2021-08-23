package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcheteurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcheteurDTO.class);
        AcheteurDTO acheteurDTO1 = new AcheteurDTO();
        acheteurDTO1.setId(1L);
        AcheteurDTO acheteurDTO2 = new AcheteurDTO();
        assertThat(acheteurDTO1).isNotEqualTo(acheteurDTO2);
        acheteurDTO2.setId(acheteurDTO1.getId());
        assertThat(acheteurDTO1).isEqualTo(acheteurDTO2);
        acheteurDTO2.setId(2L);
        assertThat(acheteurDTO1).isNotEqualTo(acheteurDTO2);
        acheteurDTO1.setId(null);
        assertThat(acheteurDTO1).isNotEqualTo(acheteurDTO2);
    }
}
