package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExerciceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExerciceDTO.class);
        ExerciceDTO exerciceDTO1 = new ExerciceDTO();
        exerciceDTO1.setId(1L);
        ExerciceDTO exerciceDTO2 = new ExerciceDTO();
        assertThat(exerciceDTO1).isNotEqualTo(exerciceDTO2);
        exerciceDTO2.setId(exerciceDTO1.getId());
        assertThat(exerciceDTO1).isEqualTo(exerciceDTO2);
        exerciceDTO2.setId(2L);
        assertThat(exerciceDTO1).isNotEqualTo(exerciceDTO2);
        exerciceDTO1.setId(null);
        assertThat(exerciceDTO1).isNotEqualTo(exerciceDTO2);
    }
}
