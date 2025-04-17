package org.abror.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ServiceForCustomerAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertServiceForCustomerAllPropertiesEquals(ServiceForCustomer expected, ServiceForCustomer actual) {
        assertServiceForCustomerAutoGeneratedPropertiesEquals(expected, actual);
        assertServiceForCustomerAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertServiceForCustomerAllUpdatablePropertiesEquals(ServiceForCustomer expected, ServiceForCustomer actual) {
        assertServiceForCustomerUpdatableFieldsEquals(expected, actual);
        assertServiceForCustomerUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertServiceForCustomerAutoGeneratedPropertiesEquals(ServiceForCustomer expected, ServiceForCustomer actual) {
        assertThat(expected)
            .as("Verify ServiceForCustomer auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertServiceForCustomerUpdatableFieldsEquals(ServiceForCustomer expected, ServiceForCustomer actual) {
        assertThat(expected)
            .as("Verify ServiceForCustomer relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getDuration()).as("check duration").isEqualTo(actual.getDuration()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertServiceForCustomerUpdatableRelationshipsEquals(ServiceForCustomer expected, ServiceForCustomer actual) {
        assertThat(expected)
            .as("Verify ServiceForCustomer relationships")
            .satisfies(e -> assertThat(e.getServiceProvider()).as("check serviceProvider").isEqualTo(actual.getServiceProvider()))
            .satisfies(e -> assertThat(e.getEmployees()).as("check employees").isEqualTo(actual.getEmployees()));
    }
}
