package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParametreMapperTest {

    private ParametreMapper parametreMapper;

    @BeforeEach
    public void setUp() {
        parametreMapper = new ParametreMapperImpl();
    }
}
