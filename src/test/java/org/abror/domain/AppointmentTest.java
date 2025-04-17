package org.abror.domain;

import static org.abror.domain.AppointmentTestSamples.*;
import static org.abror.domain.EmployeeTestSamples.*;
import static org.abror.domain.RoomTestSamples.*;
import static org.abror.domain.ServiceForCustomerTestSamples.*;
import static org.abror.domain.ServiceProviderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.abror.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppointmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appointment.class);
        Appointment appointment1 = getAppointmentSample1();
        Appointment appointment2 = new Appointment();
        assertThat(appointment1).isNotEqualTo(appointment2);

        appointment2.setId(appointment1.getId());
        assertThat(appointment1).isEqualTo(appointment2);

        appointment2 = getAppointmentSample2();
        assertThat(appointment1).isNotEqualTo(appointment2);
    }

    @Test
    void serviceProviderTest() throws Exception {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        ServiceProvider serviceProviderBack = getServiceProviderRandomSampleGenerator();

        appointment.setServiceProvider(serviceProviderBack);
        assertThat(appointment.getServiceProvider()).isEqualTo(serviceProviderBack);

        appointment.serviceProvider(null);
        assertThat(appointment.getServiceProvider()).isNull();
    }

    @Test
    void employeeTest() throws Exception {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        appointment.setEmployee(employeeBack);
        assertThat(appointment.getEmployee()).isEqualTo(employeeBack);

        appointment.employee(null);
        assertThat(appointment.getEmployee()).isNull();
    }

    @Test
    void serviceForCustomerTest() throws Exception {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        ServiceForCustomer serviceForCustomerBack = getServiceForCustomerRandomSampleGenerator();

        appointment.setServiceForCustomer(serviceForCustomerBack);
        assertThat(appointment.getServiceForCustomer()).isEqualTo(serviceForCustomerBack);

        appointment.serviceForCustomer(null);
        assertThat(appointment.getServiceForCustomer()).isNull();
    }

    @Test
    void roomTest() throws Exception {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        Room roomBack = getRoomRandomSampleGenerator();

        appointment.setRoom(roomBack);
        assertThat(appointment.getRoom()).isEqualTo(roomBack);

        appointment.room(null);
        assertThat(appointment.getRoom()).isNull();
    }
}
