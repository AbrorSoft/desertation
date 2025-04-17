package org.abror.domain;

import static org.abror.domain.EmployeeTestSamples.*;
import static org.abror.domain.WorkingHoursTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.abror.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkingHoursTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkingHours.class);
        WorkingHours workingHours1 = getWorkingHoursSample1();
        WorkingHours workingHours2 = new WorkingHours();
        assertThat(workingHours1).isNotEqualTo(workingHours2);

        workingHours2.setId(workingHours1.getId());
        assertThat(workingHours1).isEqualTo(workingHours2);

        workingHours2 = getWorkingHoursSample2();
        assertThat(workingHours1).isNotEqualTo(workingHours2);
    }

    @Test
    void employeeTest() throws Exception {
        WorkingHours workingHours = getWorkingHoursRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        workingHours.setEmployee(employeeBack);
        assertThat(workingHours.getEmployee()).isEqualTo(employeeBack);

        workingHours.employee(null);
        assertThat(workingHours.getEmployee()).isNull();
    }
}
