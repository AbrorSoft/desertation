package org.abror.service.mapper;

import static org.abror.domain.ServiceProviderAsserts.*;
import static org.abror.domain.ServiceProviderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceProviderMapperTest {

    private ServiceProviderMapper serviceProviderMapper;

    @BeforeEach
    void setUp() {
        serviceProviderMapper = new ServiceProviderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getServiceProviderSample1();
        var actual = serviceProviderMapper.toEntity(serviceProviderMapper.toDto(expected));
        assertServiceProviderAllPropertiesEquals(expected, actual);
    }
}
