package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SortieMapperTest {

    private SortieMapper sortieMapper;

    @BeforeEach
    public void setUp() {
        sortieMapper = new SortieMapperImpl();
    }
}
