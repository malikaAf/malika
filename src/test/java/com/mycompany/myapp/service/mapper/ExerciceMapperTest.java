package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExerciceMapperTest {

    private ExerciceMapper exerciceMapper;

    @BeforeEach
    public void setUp() {
        exerciceMapper = new ExerciceMapperImpl();
    }
}
