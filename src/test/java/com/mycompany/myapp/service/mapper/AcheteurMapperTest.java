package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AcheteurMapperTest {

    private AcheteurMapper acheteurMapper;

    @BeforeEach
    public void setUp() {
        acheteurMapper = new AcheteurMapperImpl();
    }
}
