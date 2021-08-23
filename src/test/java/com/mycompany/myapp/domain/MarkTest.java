package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mark.class);
        Mark mark1 = new Mark();
        mark1.setId(1L);
        Mark mark2 = new Mark();
        mark2.setId(mark1.getId());
        assertThat(mark1).isEqualTo(mark2);
        mark2.setId(2L);
        assertThat(mark1).isNotEqualTo(mark2);
        mark1.setId(null);
        assertThat(mark1).isNotEqualTo(mark2);
    }
}
