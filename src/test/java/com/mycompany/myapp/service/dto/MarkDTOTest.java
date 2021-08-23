package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarkDTO.class);
        MarkDTO markDTO1 = new MarkDTO();
        markDTO1.setId(1L);
        MarkDTO markDTO2 = new MarkDTO();
        assertThat(markDTO1).isNotEqualTo(markDTO2);
        markDTO2.setId(markDTO1.getId());
        assertThat(markDTO1).isEqualTo(markDTO2);
        markDTO2.setId(2L);
        assertThat(markDTO1).isNotEqualTo(markDTO2);
        markDTO1.setId(null);
        assertThat(markDTO1).isNotEqualTo(markDTO2);
    }
}
