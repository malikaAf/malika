package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SortieDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SortieDTO.class);
        SortieDTO sortieDTO1 = new SortieDTO();
        sortieDTO1.setId(1L);
        SortieDTO sortieDTO2 = new SortieDTO();
        assertThat(sortieDTO1).isNotEqualTo(sortieDTO2);
        sortieDTO2.setId(sortieDTO1.getId());
        assertThat(sortieDTO1).isEqualTo(sortieDTO2);
        sortieDTO2.setId(2L);
        assertThat(sortieDTO1).isNotEqualTo(sortieDTO2);
        sortieDTO1.setId(null);
        assertThat(sortieDTO1).isNotEqualTo(sortieDTO2);
    }
}
