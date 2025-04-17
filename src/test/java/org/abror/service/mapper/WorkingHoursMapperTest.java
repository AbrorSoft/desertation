package org.abror.service.mapper;

import static org.abror.domain.WorkingHoursAsserts.*;
import static org.abror.domain.WorkingHoursTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkingHoursMapperTest {

    private WorkingHoursMapper workingHoursMapper;

    @BeforeEach
    void setUp() {
        workingHoursMapper = new WorkingHoursMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWorkingHoursSample1();
        var actual = workingHoursMapper.toEntity(workingHoursMapper.toDto(expected));
        assertWorkingHoursAllPropertiesEquals(expected, actual);
    }
}
