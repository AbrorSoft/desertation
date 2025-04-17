package org.abror.service.mapper;

import static org.abror.domain.ServiceForCustomerAsserts.*;
import static org.abror.domain.ServiceForCustomerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceForCustomerMapperTest {

    private ServiceForCustomerMapper serviceForCustomerMapper;

    @BeforeEach
    void setUp() {
        serviceForCustomerMapper = new ServiceForCustomerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getServiceForCustomerSample1();
        var actual = serviceForCustomerMapper.toEntity(serviceForCustomerMapper.toDto(expected));
        assertServiceForCustomerAllPropertiesEquals(expected, actual);
    }
}
