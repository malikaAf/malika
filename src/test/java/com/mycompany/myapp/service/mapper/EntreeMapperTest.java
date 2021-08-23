package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntreeMapperTest {

    private EntreeMapper entreeMapper;

    @BeforeEach
    public void setUp() {
        entreeMapper = new EntreeMapperImpl();
    }
}
