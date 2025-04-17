package org.abror.domain;

import static org.abror.domain.EmployeeTestSamples.*;
import static org.abror.domain.ServiceForCustomerTestSamples.*;
import static org.abror.domain.ServiceProviderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.abror.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceForCustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceForCustomer.class);
        ServiceForCustomer serviceForCustomer1 = getServiceForCustomerSample1();
        ServiceForCustomer serviceForCustomer2 = new ServiceForCustomer();
        assertThat(serviceForCustomer1).isNotEqualTo(serviceForCustomer2);

        serviceForCustomer2.setId(serviceForCustomer1.getId());
        assertThat(serviceForCustomer1).isEqualTo(serviceForCustomer2);

        serviceForCustomer2 = getServiceForCustomerSample2();
        assertThat(serviceForCustomer1).isNotEqualTo(serviceForCustomer2);
    }

    @Test
    void serviceProviderTest() throws Exception {
        ServiceForCustomer serviceForCustomer = getServiceForCustomerRandomSampleGenerator();
        ServiceProvider serviceProviderBack = getServiceProviderRandomSampleGenerator();

        serviceForCustomer.setServiceProvider(serviceProviderBack);
        assertThat(serviceForCustomer.getServiceProvider()).isEqualTo(serviceProviderBack);

        serviceForCustomer.serviceProvider(null);
        assertThat(serviceForCustomer.getServiceProvider()).isNull();
    }

    @Test
    void employeesTest() throws Exception {
        ServiceForCustomer serviceForCustomer = getServiceForCustomerRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        serviceForCustomer.addEmployees(employeeBack);
        assertThat(serviceForCustomer.getEmployees()).containsOnly(employeeBack);

        serviceForCustomer.removeEmployees(employeeBack);
        assertThat(serviceForCustomer.getEmployees()).doesNotContain(employeeBack);

        serviceForCustomer.employees(new HashSet<>(Set.of(employeeBack)));
        assertThat(serviceForCustomer.getEmployees()).containsOnly(employeeBack);

        serviceForCustomer.setEmployees(new HashSet<>());
        assertThat(serviceForCustomer.getEmployees()).doesNotContain(employeeBack);
    }
}
