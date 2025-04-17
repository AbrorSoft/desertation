package org.abror.domain;

import static org.abror.domain.EmployeeTestSamples.*;
import static org.abror.domain.ServiceForCustomerTestSamples.*;
import static org.abror.domain.ServiceProviderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.abror.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = getEmployeeSample1();
        Employee employee2 = new Employee();
        assertThat(employee1).isNotEqualTo(employee2);

        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);

        employee2 = getEmployeeSample2();
        assertThat(employee1).isNotEqualTo(employee2);
    }

    @Test
    void serviceProviderTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        ServiceProvider serviceProviderBack = getServiceProviderRandomSampleGenerator();

        employee.setServiceProvider(serviceProviderBack);
        assertThat(employee.getServiceProvider()).isEqualTo(serviceProviderBack);

        employee.serviceProvider(null);
        assertThat(employee.getServiceProvider()).isNull();
    }

    @Test
    void servicesForCustomerTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        ServiceForCustomer serviceForCustomerBack = getServiceForCustomerRandomSampleGenerator();

        employee.addServicesForCustomer(serviceForCustomerBack);
        assertThat(employee.getServicesForCustomers()).containsOnly(serviceForCustomerBack);
        assertThat(serviceForCustomerBack.getEmployees()).containsOnly(employee);

        employee.removeServicesForCustomer(serviceForCustomerBack);
        assertThat(employee.getServicesForCustomers()).doesNotContain(serviceForCustomerBack);
        assertThat(serviceForCustomerBack.getEmployees()).doesNotContain(employee);

        employee.servicesForCustomers(new HashSet<>(Set.of(serviceForCustomerBack)));
        assertThat(employee.getServicesForCustomers()).containsOnly(serviceForCustomerBack);
        assertThat(serviceForCustomerBack.getEmployees()).containsOnly(employee);

        employee.setServicesForCustomers(new HashSet<>());
        assertThat(employee.getServicesForCustomers()).doesNotContain(serviceForCustomerBack);
        assertThat(serviceForCustomerBack.getEmployees()).doesNotContain(employee);
    }
}
